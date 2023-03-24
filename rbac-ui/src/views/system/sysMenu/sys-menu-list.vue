<template>
  <div>
    <!--  查询信息  -->
    <el-card>
      <el-form :model="page" :inline="true">
        <el-form-item label="菜单名称">
          <el-input v-model="page.menuName" placeholder="请输入菜单名称"></el-input>
        </el-form-item>
        <el-form-item label="菜单状态">
          <el-select v-model="page.status">
            <el-option v-for="dictData in statusOptions" :value="dictData.dictValue" :label="dictData.dictLabel"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="doSearch">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" @click="doReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!--  表格信息  -->
    <el-card style="margin-top: 20px">
      <template>
        <div style="margin-bottom: 10px">
                                         <!--     两个新增共用一个组件，要么同时传值(值可以不一样)，要么都不传值     -->
          <el-button type="primary" icon="el-icon-plus" @click="doAdd(null)">新增</el-button>
          <el-button type="info" icon="el-icon-sort" @click="doExpandAll">折叠/展开</el-button>
        </div>
      </template>

      <!--
          表格渲染    表格展开时要重新渲染dom，因此得加v-if属性
              row-key：数据源(数组)中 每一条数据 的唯一标识 ，表示要对该条数据进行操作
      -->
      <el-table
        :data="menuList"
        row-key="menuId"
        border
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        v-if="refreshTable"

      >
        <el-table-column prop="menuName" label="菜单名称" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="图标" align="center">
          <template slot-scope="{row}">
            <!--
                svgicon标签：图标标签
                    icon-class：图标值(根据不同的值显示不同的图标)
            -->
            <svgicon :icon-class="row.icon"></svgicon>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" align="center"></el-table-column>
        <!--
           show-overflow-tooltip：当内容过长被隐藏时显示提示文字   默认false
        -->
        <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true" align="center"></el-table-column>
        <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" align="center"></el-table-column>
        <el-table-column prop="path" label="路由地址" :show-overflow-tooltip="true" align="center"></el-table-column>
        <el-table-column prop="status" label="状态" align="center">
          <template slot-scope="{row}">
            <!--  状态  -->
            <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="menuType" label="类型" align="center">
          <template slot-scope="{row}">
            <!--  菜单类型 (0：目录  1：目录  2：权限)  -->
            <el-tag :type="row.menuType==0?'success':row.menuType==1?'primary':'warning'">{{row.menuType==0?"目录":row.menuType==1?"菜单":"权限"}}</el-tag>
          </template>
        </el-table-column>
        <!--   下拉按钮选项     -->
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-dropdown>
                        <span class="el-dropdown-link">
                          <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
              <el-dropdown-menu slot="dropdown" align="center">
                <el-dropdown-item>
                                    <!--     两个新增共用一个组件，要么同时传值(值可以不一样)，要么都不传值     -->
                  <el-button type="text" icon="el-icon-plus" @click="doAdd(scope.row)">新增</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <!--            修改得根据字典编码dictCode进行修改，因此得传参数            -->
                  <el-button type="text" icon="el-icon-edit" @click="doUpdate(scope.row)">修改</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-delete" @click="doDelete(scope.row)">删除</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!--  新增页面  v-if：重新渲染dom   -->
    <el-dialog :visible.sync="showAddDialog" :v-if="showAddDialog" :title="title" :close-on-click-modal="false" :destroy-on-close="true" align="center">
      <!--   组件传递方法，是通过事件传递的，所以用 @     @事件="方法"  -->
      <sys-menu-add @after="doSearch" @close="doClose" :parent-obj="parentObj"></sys-menu-add>
    </el-dialog>


    <!--  修改页面  -->
    <el-dialog :visible.sync="showUpdateDialog" :v-if="showUpdateDialog" :title="title" :close-on-click-modal="false" :destroy-on-close="true" align="center">
      <sys-menu-update @after="doSearch" @close="doClose" :active-obj="activeObj"></sys-menu-update>
    </el-dialog>


  </div>
</template>

<script>
import sysMenuApi from "@/api/system/sysMenu"
import sysDictDataApi from "@/api/system/sysDictData"
// 引入将数据转化为 树形结构的方法
import {handleTree} from "@/utils/powernode"
// 引入图标组件
import Svgicon from "@/components/SvgIcon/index"
// 引入新增组件
import SysMenuAdd from "./sys_menu_add"
// 引入修改组件
import SysMenuUpdate from "./sys_menu_update"

export default {
  name: "sys-menu-list",
  /* 注册组件 */
  components: {
    SysMenuAdd,
    SysMenuUpdate,
    Svgicon
  },
  data(){
    return {
      /* 查询参数 */
      page: {},
      /* 表格数据源 */
      menuList: [],
      /* 状态信息数组 */
      statusOptions: [],
      /* 是否展开 */
      isExpandAll: false,
      /* 控制dom的显示和消失 */
      refreshTable: true,
      /* 是否显示添加弹出框 */
      showAddDialog: false,
      /* 是否显示修改弹出框 */
      showUpdateDialog: false,
      /* 弹出框标题 */
      title: undefined,
      /*
      *   因为传递给子组件的参数有多个，所以这里直接传对象(一条数据，而不再是一个数据)
      * */
      /* 传递给添加组件的 对象参数 */
      parentObj: null,
      /* 传递给修改组件的 对象参数 */
      activeObj: null
    }
  },
  created() {
    this.showStatus()
    this.doSearch()
  },
  methods: {
    /* 获取状态字典数据(两条：dictValue=0:dictLabel="正常"  dictValue=1:dictLabel="停用") */
    showStatus(){
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions = rs.data;
      })
    },
    /* 搜索菜单 */
    doSearch(){
      sysMenuApi.listMenu(this.page).then(rs=>{
        /* 将查询到的数据转换成 树形结构 */
        this.menuList = handleTree(rs.data,"menuId")
      })
    },
    /* 重置查询参数 */
    doReset(){
      this.page = {}
    },
    /* 添加菜单权限 */
    doAdd(row){
      this.showAddDialog = true
      this.title = "添加菜单"
      /* 给添加子组件传参，传一条数据，而不是一个数据 */
      this.parentObj = row
    },
    /* 展开/折叠 */
    doExpandAll(){
      // 取反，如果折叠就展开，如果展开就折叠
      this.isExpandAll = !this.isExpandAll;
      // 重新渲染dom
      this.refreshTable = false
      this.$nextTick(()=>{
        this.refreshTable = true
      })
    },
    /* 修改表单 */
    doUpdate(row){
      this.showUpdateDialog = true
      this.title = "修改表单"
      /* 给修改子组件传参，传一条数据，而不是一个数据 */
      this.activeObj = row
    },
    /* 删除表单 */
    doDelete(row){
      this.$confirm("确定要删除"+row.menuName+"吗?","删除菜单",{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(()=>{
        sysMenuApi.delete(row.menuId).then(rs=>{
          this.$message.success(rs.msg)
          this.doSearch()
        })
      })
    },
    /* 关闭弹出框 */
    doClose(){
      this.showAddDialog = false
      this.showUpdateDialog = false
      this.title = undefined
    }
  }
}
</script>

<style scoped>

</style>
