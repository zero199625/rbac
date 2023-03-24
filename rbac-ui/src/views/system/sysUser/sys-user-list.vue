<template>
  <div>
    <!--
        gutter：列间距   单位:px
     -->
    <el-row :gutter="5" style="margin-top: 10px">
      <el-col :span="8":xs="24">
        <el-card style="height: 650px">
          <sys-user-left-tree @after="doSearch"></sys-user-left-tree>
        </el-card>
      </el-col>

      <!--
          表格
       -->
      <el-col :span="16":xs="24">
        <el-card>
          <el-form :model="page" :inline="true" size="mini">
            <el-form-item label="用户账号">
              <el-input v-model="page.params.userName" placeholder="请输入用户账号"></el-input>
            </el-form-item>
            <el-form-item label="用户昵称">
              <el-input v-model="page.params.nickName" placeholder="请输入用户昵称"></el-input>
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
        <el-card style="margin-top: 5px">
          <template slot="header">
            <div>
              <el-button type="primary" icon="el-icon-plus" @click="doAdd" size="mini">添加</el-button>
            </div>
          </template>

          <el-table :data="dataPage.list">
            <el-table-column label="用户账号" prop="userName"></el-table-column>
            <el-table-column label="用户昵称" prop="nickName"></el-table-column>
            <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="电话" prop="phone" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="性别" prop="sex">
              <template slot-scope="{row}">
                <div v-for="dictData in sexOptions" v-if="dictData.dictValue==row.sex">{{dictData.dictLabel}}</div>
              </template>
            </el-table-column>
            <el-table-column label="所在部门" prop="deptId" :show-overflow-tooltip="true">
              <template slot-scope="{row}">
                <div v-for="dept in deptList" v-if="dept.deptId==row.deptId">{{dept.deptName}}</div>
              </template>
            </el-table-column>
            <el-table-column label="状态" prop="status">
              <template slot-scope="{row}">
                <div v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status">{{dictData.dictLabel}}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="{row}">
                <el-dropdown>
                   <span class="el-dropdown-link">
                     <el-link icon="el-icon-s-operation" type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                   </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button type="text" icon="el-icon-document" @click="doDetail(row)">详情</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button type="text" icon="el-icon-edit" @click="doUpdate(row)">修改</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button type="text" icon="el-icon-delete" v-if="row.userId!=1" @click="doDelete(row)">删除</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button type="text" icon="el-icon-document" @click="doDivide(row)">分配角色</el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!--   分页信息   -->
          <el-pagination
            :current-page="page.pageNumber"
            :page-size="page.pageSize"
            :page-sizes="[2,5,10,20]"
            :total="dataPage.totalCount"
            layout="total, sizes, prev, pager, next, jumper "
            @current-change="pageChange"
            @size-change="sizeChange">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <!--  添加弹出框  -->
    <el-dialog :visible.sync="showAddDialog" v-if="showAddDialog" :title="title" :destroy-on-close="true" :close-on-click-modal="false" align="center">
      <sys-user-add @after="doSearch" @close="doClose"></sys-user-add>
    </el-dialog>

    <!--  更新弹出框  -->
    <el-dialog :visible.sync="showUpdateDialog" v-if="showUpdateDialog" :title="title" :destroy-on-close="true" :close-on-click-modal="false" align="center">
                                                        <!--   active-obj 首字母小写   -->
      <sys-user-update @after="doSearch" @close="doClose" :active-obj="activeObj"></sys-user-update>
    </el-dialog>

    <!--  详情弹出框  -->
    <el-dialog :visible.sync="showDetailDialog" v-if="showDetailDialog" :title="title" :destroy-on-close="true" :close-on-click-modal="false" align="center">
      <sys-user-detail @close="doClose" :active-obj="activeObj"></sys-user-detail>
    </el-dialog>

    <!--  分配角色弹出框  -->
    <el-dialog :visible.sync="showDivideDialog" v-if="showDivideDialog" :title="title" :destroy-on-close="true" :close-on-click-modal="false" align="center">
      <sys-user-divide @close="doClose" :active-obj="activeObj"></sys-user-divide>
    </el-dialog>

  </div>
</template>

<script>
import sysDictDataApi from "@/api/system/sysDictData"
import sysDeptApi from "@/api/system/sysDept"
import {handleTree} from "@/utils/powernode";
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import sysUserApi from "@/api/system/sysUser"
import SysUserLeftTree from "./sys-user-left-tree"
import SysUserAdd from "./sys-user-add"
import SysUserUpdate from "@/views/system/sysUser/sys-user-update";
import SysUserDetail from "./sys-user-detail"
import SysUserDivide from "@/views/system/sysUser/sys-user-divide";

export default {
  name: "sys-user-list",
  components: {
    SysUserDivide,
    SysUserUpdate,
    Treeselect,
    SysUserLeftTree,
    SysUserAdd,
    SysUserDetail
  },
  data(){
    return {
      /* 查询参数 */
      page: {
        pageNumber: 1,
        pageSize: 10,
        params: {
          userName: undefined,
          nickName: undefined,
          status: undefined
        }
      },
      /* 表格参数 */
      dataPage: {
        list: [],
        totalCount: undefined,
        totalPage: undefined
      },
      /* 状态参数 */
      statusOptions: [],
      /* 性别参数 */
      sexOptions: [],
      /* 部门数据参数 */
      deptList: [],
      /* 打开添加弹出框 */
      showAddDialog: false,
      /* 打开详情弹出框 */
      showDetailDialog: false,
      /* 打开更新弹出框 */
      showUpdateDialog: false,
      /* 打开分配角色弹出框 */
      showDivideDialog: false,
      /* 传递给子组件的 一整条数据 */
      activeObj: undefined,

    }
  },
  created() {
    this.showStatus()
    this.showDept()
    this.showSex()
    this.doSearch()
  },
  methods: {
    /* 获取部门信息 */
    showDept(){
      sysDeptApi.listDept(null).then(rs=>{
        this.deptList = rs.data
      })
    },
    /* 获取状态信息 */
    showStatus(){
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions = rs.data
      })
    },
    /* 获取性别信息 */
    showSex(){
      sysDictDataApi.getDictDataByDictType("sys_user_sex").then(rs=>{
        this.sexOptions = rs.data
      })
    },
    /* 分页查询   这里的deptId是子组件传过来的    有一个现象：如果子组件没有传参数，那么deptId就会变为一个对象 */
    doSearch(deptId){
        // 1.首先判断是否为空
        if (deptId!=null) {
          // 2.不为空，再判断是不是数字
          if (typeof deptId === 'number') {
            // 4.为数字，点了一下左边的树形数据
            this.page.params.deptId = deptId
            sysUserApi.getByPage(this.page).then(rs=> {
              this.dataPage.list = rs.data.list
              this.dataPage.totalCount = rs.data.totalCount
              this.dataPage.totalPage = rs.data.totalPage
            })
          }else {
            // 5.不为数字，说明点了第了一下搜索
            sysUserApi.getByPage(this.page).then(rs=>{
              this.dataPage.list = rs.data.list
              this.dataPage.totalCount = rs.data.totalCount
              this.dataPage.totalPage = rs.data.totalPage
            })
          }
        }else {
          // 3.为空，说明刚刚点进用户管理
          sysUserApi.getByPage(this.page).then(rs=>{
            this.dataPage.list = rs.data.list
            this.dataPage.totalCount = rs.data.totalCount
            this.dataPage.totalPage = rs.data.totalPage
          })
        }
    },
    /* 重置查询参数 */
    doReset(){
      this.page = {
        pageNumber: 1,
        pageSize: 10,
        params: {
          userName: undefined,
          nickName: undefined,
          status: undefined
        }
      }
    },
    /* 打开添加用户弹出框 */
    doAdd(){
      this.showAddDialog = true
      this.title = "添加用户"
    },
    /* 打开查看详情弹出框 */
    doDetail(row){
      this.showDetailDialog = true
      this.title = `【${row.userName}】详情`
      this.activeObj  =row
    },
    /* 打开修改弹出框 */
    doUpdate(row){
      this.showUpdateDialog = true
      this.title = `修改【${row.userName}】用户`
      this.activeObj = row
    },
    /* 删除用户 */
    doDelete(row){
      this.$confirm("确定要删除【"+row.userName+"】吗？","删除用户",{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(()=>{
        sysUserApi.delete(row.userId).then(rs=>{
          this.$message.success(rs.msg)
          this.doSearch()
        })
      })
    },
    /* 打开分配角色弹出框 */
    doDivide(row){
      this.showDivideDialog  =true
      this.title = `给【${row.userName}】分配角色`
      this.activeObj = row
    },
    doClose(){
      this.showAddDialog = false
      this.showUpdateDialog = false
      this.showDetailDialog = false
      this.showDivideDialog = false
      this.title = undefined
    },
    /* 页码发生变化时调用的函数 */
    pageChange(num){
      this.page.pageNumber = num
      this.doSearch()
    },
    /* 每条页数发生变化时调用的函数 */
    sizeChange(size){
      this.page.pageSize = size
      this.doSearch()
    }
  }
}
</script>

<style scoped>

</style>
