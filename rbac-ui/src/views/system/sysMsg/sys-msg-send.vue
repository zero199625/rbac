<template>
  <div>
    <el-card style="margin-top: 20px">

      <el-form :model="form"  label-width="25%">
        <el-form-item label="消息标题" prop="msgTitle">
          <el-input v-model="form.msgTitle" placeholder="请输入消息标题" style="width: 50%"></el-input>
        </el-form-item>
        <el-form-item label="消息内容" prop="msgContent">
          <el-input type="textarea" v-model="form.msgContent" placeholder="请输入消息内容" style="width: 50%"></el-input>
        </el-form-item>
        <el-form-item label="收件人">
          <el-input v-model="userName" style="width: 300px"></el-input>
        </el-form-item>

        <el-form-item label-width="25%">
          <el-button type="primary" align="center" @click="doSend">发送给某个用户</el-button>
          <el-button type="primary" align="center" @click="doSendAll" v-if="userId==1">发送给所有用户</el-button>
          <el-button type="warning" align="center" @click="doReset">重置</el-button>
        </el-form-item>

      </el-form>

    </el-card>

    <el-card>

      <el-table :data="pageList.list">
        <el-table-column prop="msgReceiveId" label="收件人">
          <template slot-scope="{row}">
            <div v-for="user in userList" v-if="user.userId==row.msgReceiveId">{{user.userName}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="msgTitle" label="消息标题"></el-table-column>
        <el-table-column prop="msgContent" label="消息内容"></el-table-column>
        <el-table-column prop="msgSendTime" label="发送时间"></el-table-column>
      </el-table>
    </el-card>

  </div>
</template>

<script>
import sysMsgApi from "@/api/system/sysMsg";
import sysUserApi from "@/api/system/sysUser"

export default {
  name: "sys-msg-send",
  data(){
    return {
      /* 表单消息 */
      form: {

      },
      /* 收件人id */
      msgReceiveId: undefined,
      /* 当前用户id */
      userId: undefined,
      /* 用户名称 */
      userName: undefined,
      /* 查询信息 */
      page: {
        pageNumber: 1,
        pageSize: 10,
        params: {
          /* 发件箱 */
          msgMailbox: 1
        }
      },
      /* 响应信息 */
      pageList: {
        list: [],
        totalCount: undefined,
        totalPage: undefined
      },
      /* 所有用户信息 */
      userList: []
    }
  },
  created() {
    this.selectUser()
    this.doSearch()
    this.getAllUserInfo()
  },
  methods: {
    /* 获取所有用户信息 */
    getAllUserInfo(){
      sysUserApi.getAllUser().then(rs=>{
        this.userList = rs.data
      })
    },
    /* 发送给某个人 */
    doSend(){
      this.selectUserByUserName();
    },
    /* 发送消息给所有人 */
    doSendAll(){

      sysMsgApi.sendAll(this.form).then(rs=>{
        this.$message.success(rs.msg)
        this.doReset()
        this.doSearch()
      })
    },
    /* 重置 */
    doReset(){
      this.form = {}
      this.userName = undefined
    },
    /* 根据userName 查找userId */
    selectUserByUserName(){
      /* axios是异步请求，多个线程并行，如果不讲axios请求嵌套在其他的方法中，那么就不会获取到该方法的值，必须嵌套 */
      sysUserApi.getUserIdByUserName(this.userName).then(rs=>{
        this.form.msgReceiveId = rs.data
        sysMsgApi.send(this.form).then(rs=>{
          this.$message.success(rs.msg)
          this.doReset()
          this.doSearch()
        })
      })
    },
    /* 查找当前用户 */
    selectUser(){
      sysUserApi.getUserInfo().then(rs=>{
        this.userId = rs.data.sysUser.userId
      })
    },
    /* 渲染表格信息 */
    doSearch(){
      sysMsgApi.getByPage(this.page).then(rs=>{
        this.pageList.list = rs.data.list
        this.pageList.totalCount = rs.data.totalCount
        this.pageList.totalPage = rs.data.totalPage
      })

    }
  }

}
</script>

<style scoped>

</style>
