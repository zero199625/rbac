<template>
  <div>
    <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
      <el-form-item label="部门" prop="deptId">
        <treeselect
          v-model="form.deptId"
          :options="deptList"
          :normalizer="normalizer"
          :show-count="true"
          placeholder="请选择上级部门"
        >
        </treeselect>
      </el-form-item>
      <el-row>
        <el-col :span="11">
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="form.userName" placeholder="请输入用户名"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11" style="margin-left: 30px">
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="form.nickName" placeholder="请输入昵称"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11" style="margin-left: 30px">
          <el-form-item label="电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入电话"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" align="left">
            <el-radio-group v-model="form.sex">
              <el-radio v-for="dictData in sexOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" align="left">
            <el-radio-group v-model="form.status">
              <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="primary" icon="el-icon-plus" @click="doAdd">添加</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="doReset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import sysDeptApi from "@/api/system/sysDept";
import sysDictDataApi from "@/api/system/sysDictData";
import sysUserApi from "@/api/system/sysUser"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import {handleTree} from "@/utils/powernode";

export default {
  name: "sys-user-add",
  components: {
    Treeselect
  },
  data(){
    return {
      /* 表单参数 */
      form: {
        status: 0,
        sex: 0
      },
      /* 表单校验参数 */
      formRules: {
        deptId: [{
          required: true, message: "请输入部门名称", trigger: "blur"
        }],
        userName: [{
          required: true, message: "请输入用户名称", trigger: "blur"
        }],
        nickName: [{
          required: true, message: "请输入部门昵称", trigger: "blur"
        }],
        email: [{
          required: true, message: "请输入邮箱", trigger: "blur"
        }],
        phone: [{
          required: true, message: "请输入电话", trigger: "blur"
        }],
      },
      /* 状态参数 */
      statusOptions: [],
      /* 性别参数 */
      sexOptions: [],
      /* 部门数据参数 */
      deptList: [],
    }
  },
  created() {
    this.showStatus()
    this.showDept()
    this.showSex()
  },
  methods: {
    /* 获取部门信息 */
    showDept(){
      sysDeptApi.listDept(null).then(rs=>{
        this.deptList = handleTree(rs.data,"deptId")
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
    normalizer(node){
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    },
    /* 添加用户 */
    doAdd(){
      this.$refs["formRef"].validate(flag=>{
        if (flag) {
          sysUserApi.save(this.form).then(rs=>{
            this.$message.success(rs.msg)
            this.$emit("after")
            this.$emit("close")
            this.doReset()
          })
        }
      })
    },
    /* 重置表单参数 */
    doReset(){
      this.$refs["formRef"].clearValidate()
      this.form = {
        status: 0,
        sex: 0
      }
    }
  }
}
</script>

<style scoped>

</style>
