<template>
  <div>
    <!--  查询  -->
    <el-card>
      <el-form :model="page" :inline="true">
        <el-form-item label="部门名称">
          <el-input v-model="page.deptName" placeholder="请输入部门名称"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="page.status" placeholder="请选择状态">
            <el-option v-for="dictData in statusOptions"
                       :value="dictData.dictValue"
                       :label="dictData.dictLabel">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="doSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="doReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!--  部门表格  -->
    <!--
        编写树形表格：
            1.引入依赖 "@riophae/vue-treeselect": "^0.4.0"
            2.引入自定义的工具方法 handleTree (utils/powernode/handleTree())
            3.编写表格
    -->
    <el-card style="margin-top: 10px">
      <template>
        <div style="margin-bottom: 5px">
          <el-button type="primary" icon="el-icon-plus" @click="toAdd">新增</el-button>
          <el-button type="info" icon="el-icon-sort" @click="doExpandAll">展开/折叠</el-button>
        </div>
      </template>
      <!--
          树形表格
              data：树形表格数据
              row-key：唯一标识，用于让handleTree()方法识别
              default-expand-all：是否默认展开所有行  默认false   true:展开  false：不展开
              tree-props：配置嵌套数据(children、hasChildren)      tree-props = "{children: 'children', hasChildren: 'hasChildren'}"
                  children:描述子记录
              v-if：是否(重新)渲染dom  true:渲染   false：不渲染
      -->
      <el-table
        :data="deptList"
        row-key="deptId"
        border
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        v-if="refreshTable"
      >
        <el-table-column
          prop="deptName"
          label="部门名称"
          width="180">
        </el-table-column>
        <el-table-column
          prop="orderNum"
          label="排序"
          width="180">
        </el-table-column>
        <el-table-column
          prop="leader"
          label="负责人">
        </el-table-column>
        <el-table-column
          prop="phone"
          label="负责人电话">
        </el-table-column>
        <el-table-column
          prop="email"
          label="邮箱">
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态">
          <!--     状态     -->
          <template slot-scope="scope">
            <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==scope.row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>
          </template>
        </el-table-column>
        <!--
            下拉菜单
                hide-on-click：是否在点击菜单项后隐藏菜单   默认true
        -->
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-dropdown :hide-on-click="false">
                <span class="el-dropdown-link">
                  <el-link type="primary">操作</el-link> <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
              <el-dropdown-menu slot="dropdown">
                <!--   type：菜单按钮类型     -->
                  <el-dropdown-item>
                    <el-button type="text" icon="el-icon-plus" @click="toAdd(scope.row)">新增</el-button>
                  </el-dropdown-item>
                <el-dropdown-item>
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

    <!--  新增部门页面    参数为当成parentId的deptId   v-if：是否重新渲染dom -->
    <el-dialog :visible.sync="showAddDialog" v-if="showAddDialog" :title="title" :close-on-click-modal="false" align="center">
      <!--   3.使用新增部门组件   -->
      <sys-dept-add @after="doSearch" @close="doClose" :parent-id="parentId"></sys-dept-add>
    </el-dialog>

    <!--  修改部门页面     参数为deptId  -->
    <el-dialog :visible.sync="showUpdateDialog" v-if="showUpdateDialog" :title="title" :close-on-click-modal="false" align="center">
      <sys-dept-update @after="doSearch" @close="doClose" :active-id="activeId"></sys-dept-update>
    </el-dialog>

  </div>
</template>

<script>
// 引入字典数据api，用于查询status
import sysDictDataApi from "@/api/system/sysDictData";
// 引入树形结构工具
import {handleTree} from "@/utils/powernode"
// 引入部门api
import sysDeptApi from "@/api/system/sysDept"
// 1.引入新增部门组件
import SysDeptAdd from "./sys-dept-add"
// 引入修改部门组件
import SysDeptUpdate from "./sys-dept-update"

export default {
  name: "sys-dept-list",
  /* 2.注册组件 */
  components: {
    SysDeptAdd,
    SysDeptUpdate
  },
  data() {
    return {
      /* 查询参数，没有每页查询，所以不用添加pageNum和pageSize */
      page: {
        deptName: undefined,
        status: undefined
      },
      /* 部门状态 */
      statusOptions: [],
      /* 部门树形表格数据 */
      deptList: [],
      /* 是否展开所有行 */
      isExpandAll: false,
      /* 控制dom的显示和消失 */
      refreshTable: true,
      /* 是否显示新增部门页面 */
      showAddDialog: false,
      /* 是否修改部门 */
      showUpdateDialog: false,
      /* 新增/修改 标题 */
      title: undefined,
      /* 母公司ID，将deptId当做parentId进行查询 */
      parentId: undefined,
      /* 选中的部门的ID  deptId */
      activeId: undefined
    }
  },
  created() {
    this.showStatus()
    this.doSearch()
  },
  methods: {
    /* 显示状态 */
    showStatus() {
      /* 根据字典类型sys_status_type，查询到两条字典数据 */
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs => {
        this.statusOptions = rs.data;
      })
    },
    /* 查询部门 */
    doSearch() {
      sysDeptApi.listDept(this.page).then(rs=>{
        /*
        * handleTree()：将查询到的数据以 树形结构数组 的形式返回    这样数据就会自动以树形的形式渲染表格，而不再是简简单单的平面渲染
        *     第一个参数：查询到的数据
        *     第二个参数：对应table表格的row-key属性
        * deptList是属性结构数据，deptList.children表示其一条子数据
        * */
        this.deptList = handleTree(rs.data,"deptId")
      })
    },
    /* 重置查询参数 */
    doReset() {
      this.page = {}
    },
    /* 新增 部门/子部门 的弹出框 */
    toAdd(row){
      this.showAddDialog = true
      this.title = "添加部门"
      /*
      *   传的是deptId，而不是parentId
      *     当添加 部门时，parentId =undefined
      *     当添加 子部门时，parentId =row.deptId
      * */
      this.parentId = row.deptId
    },
    /* 折叠/展开部门表格 */
    doExpandAll(){
      // 清除原先的dom(表格)
      this.refreshTable = false;
      // 取反，如果展开就折叠，如果折叠就展开
      this.isExpandAll = !this.isExpandAll
      this.$nextTick(()=>{
        // 重新渲染dom(表格)
        this.refreshTable = true;
      })
    },
    /* 修改部门的弹出框 */
    doUpdate(row) {
      this.showUpdateDialog = true
      this.title = "修改部门"
      /* 将参数传递给修改部门的 子组件   传的是deptId，而不是parentId */
      this.activeId = row.deptId;
    },
    /* 删除部门 */
    doDelete(row) {
      this.$confirm("确定删除"+row.deptName+"吗?",{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(()=>{
        sysDeptApi.delete(row.deptId).then(rs=>{
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
