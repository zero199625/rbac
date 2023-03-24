<template>
  <div>
    <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="form.roleName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="显示顺序">
            <el-input-number v-model="form.roleSort"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="form.remark"></el-input>
      </el-form-item>
      <el-form-item label="状态" align="left">
        <template slot-scope="{row}">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-button type="primary" icon="el-icon-plus" @click="doUpdate">修改</el-button>
      <el-button type="warning" icon="el-icon-refresh" @click="doReset">重置</el-button>
    </el-form>
  </div>
</template>

<script>
import sysDictDataApi from "@/api/system/sysDictData"
import sysRoleApi from "@/api/system/sysRole"

export default {
  name: "sys-role-update",
  /* 接收父组件传过来的值 */
  props: {
    activeId: {
      type: Number,
      default: undefined
    }
  },
  /* 监听activeId的变化，activeId默认为undefined，一旦值发生变化(父组件传值的时候activeId从undefined变为传过来的值)就调用handler方法 */
  watch: {
    activeId: {
      immediate: true,
      handler: function (newVal, oldVal){  //newVal=activeId
        sysRoleApi.get(this.activeId).then(rs=>{
          this.form = rs.data
        })
      }
    }
  },
  data(){
    return {
      form: {
        status: 0,
      },
      formRules: {
        roleName: [{
          required: true, message: "请输入角色名称", trigger: "blur"
        }],
        remark: [{
          required: true, message: "请输入备注", trigger: "blur"
        }]
      },
      /* 状态参数 */
      statusOptions: [],
    }
  },
  created() {
    this.showStatus()
  },
  methods: {
    /* 获取状态参数 */
    showStatus(){
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions = rs.data
      })
    },
    /* 添加角色 */
    doUpdate(){
      this.$refs["formRef"].validate(flag=>{
        if (flag) {
          sysRoleApi.update(this.form).then(rs=>{
            this.$message.success(rs.msg)
            this.$emit("after")
            this.$emit("close")
            this.doReset()
          })
        }
      })
    },
    /* 重置 */
    doReset(){
      /* 消除校验 */
      this.$refs["formRef"].clearValidate()
      this.form = {status: 0}
    }
  }
}
</script>

<style scoped>

</style>
