<template>
  <div>
    <el-card>
      <el-form :model="page" :inline="true">
        <el-form-item label="角色名称">
          <el-input v-model="page.params.roleName" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="page.params.status">
            <el-option v-for="dictData in statusOptions" :value="dictData.dictValue" :label="dictData.dictLabel"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="doSearch">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" @click="doReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 10px">
      <template>
        <div style="margin-bottom: 5px">
          <el-button type="primary" icon="el-icon-plus" @click="doAdd()">添加</el-button>
        </div>
      </template>
      <el-table :data="dataPage.list">
        <el-table-column label="角色ID" prop="roleId"></el-table-column>
        <el-table-column label="角色名称" prop="roleName"></el-table-column>
        <el-table-column label="角色顺序" prop="roleSort"></el-table-column>
        <el-table-column label="备注" prop="remark"></el-table-column>
        <el-table-column label="角色状态" prop="status">
          <template slot-scope="{row}">
            <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建人" prop="createBy"></el-table-column>
        <el-table-column label="创建时间" prop="createTime" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{row}">
            <el-dropdown>
            <span class="el-dropdown-link">
            <el-link type="primary">操作</el-link> <i class="el-icon-arrow-down el-icon--right"></i>
             </span>
              <el-dropdown-menu slot="dropdown" align="center">
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-edit" @click="doUpdate(row)">修改</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-plus" @click="doDelete(row)">删除</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-download" @click="doRoleSelectMenus(row)">角色菜单分配</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!--   分页信息   -->
      <el-pagination
        :current-page="page.pageNumber"
        :page-sizes="[1, 2, 5, 10]"
        :page-size="page.pageSize"
        :total="dataPage.totalCount"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="pageChange"
        @size-change="sizeChange"
      >
      </el-pagination>
    </el-card>


    <!--  添加弹出框  -->
    <el-dialog :visible.sync="showAddDialog" v-if="showAddDialog" :title="title" :close-on-click-modal="false" :destroy-on-close="true" align="center">
      <!--   使用组件   -->
      <sys-role-add @after="doSearch" @close="doClose"></sys-role-add>
    </el-dialog>

    <!--  更新弹出框  -->
    <el-dialog :visible.sync="showUpdateDialog" v-if="showUpdateDialog" :title="title" :close-on-click-modal="false" :destroy-on-close="true" align="center">
      <!--   使用组件   -->
      <sys-role-update @after="doSearch" @close="doClose" :active-id="activeId"></sys-role-update>
    </el-dialog>

    <!--  角色菜单分配弹出框  -->
    <el-dialog :visible.sync="showRoleMenuDialog" v-if="showRoleMenuDialog" :title="title" :close-on-click-modal="false" :destroy-on-close="true" align="center">
      <!--   使用组件   -->
      <sys-role-select-menu @after="doSearch" @close="doClose" :active-id="activeId"></sys-role-select-menu>
    </el-dialog>

  </div>
</template>

<script>
import sysDictDataApi from "@/api/system/sysDictData"
import sysRoleApi from "@/api/system/sysRole"
// 引入组件
import SysRoleAdd from "./sys-role-add"
import SysRoleUpdate from "./sys-role-update"
import sysRoleMenu from "./sys-role-select-menu"
import SysRoleSelectMenu from "@/views/system/sysRole/sys-role-select-menu";

export default {
  name: "sys-role-list",
  /* 注册组件 */
  components: {
    SysRoleSelectMenu,
    SysRoleAdd,
    SysRoleUpdate,
    sysRoleMenu
  },
  data(){
    return {
      /* 查询(请求)参数 */
      page: {
        pageNumber: 1,
        pageSize: 5,
        params: {
          roleName: undefined,
          status: undefined
        }
      },
      /* 表格(响应)参数 */
      dataPage: {
        list: [],
        totalCount: undefined,
        totalPage: undefined
      },
      /* 状态参数 */
      statusOptions: [],
      /* 是否显示添加弹出框 */
      showAddDialog: false,
      /* 是否显示更新弹出框 */
      showUpdateDialog: false,
      /* 是否显示分配菜单权限的弹出框 */
      showRoleMenuDialog: false,
      /* 传递给子组件的参数 */
      activeId: undefined
    }
  },
  created() {
    this.showStatus()
    this.doSearch()
  },
  methods: {
    /* 获取状态参数 */
    showStatus() {
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions = rs.data
      })
    },
    /* 分页查询 */
    doSearch(){
      sysRoleApi.getByPage(this.page).then(rs=>{
        this.dataPage.list = rs.data.list
        this.dataPage.totalCount = rs.data.totalCount
        this.dataPage.totalPage = rs.data.totalPage
      })
    },
    /* 重置所有表单 */
    doReset(){
      this.page = {
        pageNumber: 1,
        pageSize: 5,
        params: {
          roleName: undefined,
          status: undefined
        }
      }
    },
    /* 添加角色 */
    doAdd(row){
      this.showAddDialog = true
      this.title = "添加角色"
    },
    /* 修改角色 */
    doUpdate(row){
      this.showUpdateDialog = true
      this.title = "修改角色"
      this.activeId = row.roleId
    },
    /* 删除角色 */
    doDelete(row){
      this.$confirm("确定要删除"+row.roleName+"吗？","删除角色",{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(()=>{
        sysRoleApi.delete(row.roleId).then(rs=>{
          this.$message.success(rs.msg)
          this.doSearch()
        })
      })
    },
    /* 角色菜单分配 */
    doRoleSelectMenus(row){
      this.showRoleMenuDialog = true
      this.title = `分配【${row.roleName}】的菜单权限`
      this.activeId = row.roleId
    },
    /* 页码发生编码调用的方法 */
    pageChange(num){
      this.page.pageNumber = num
      this.doSearch()
    },
    /* 每页条数发生变化调用的函数 */
    sizeChange(size) {
      this.page.pageSize = size
      this.doSearch()
    },
    /* 关闭弹出框 */
    doClose(){
      this.showAddDialog = false
      this.showUpdateDialog = false
      this.showRoleMenuDialog = false
      this.title = undefined
    }
  }
}
</script>

<style scoped>

</style>
