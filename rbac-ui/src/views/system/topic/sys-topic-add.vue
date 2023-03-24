<template>
  <div>
    <el-form :model="form" :rules="formRules" ref="formRef" size="mini" label-width="80px">
      <el-form-item prop="title" label="主题">
        <!--    字典类型不能更改    -->
        <el-input v-model="form.title" placeholder="请输入主题"></el-input>
      </el-form-item>
      <el-form-item prop="author" label="作者">
        <el-input v-model="form.author" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="content" label="内容">
        <el-input type="textarea" v-model="form.content" placeholder="请输入帖子内容"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="toAdd" size="mini">发布</el-button>
        <el-button type="warning" @click="toReset" size="mini">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>


<script>
import sysTopicApi from "@/api/system/sysTopic"

export default {
  name: "sys-dict-data-add",
  data(){
    return{
      form: {},
      formRules: {
        title:[{
          required: true, message: "请输入主题", trigger: "blur"
        }],
        author: [{
          required: true, message: "请输入作者", trigger: "blur"
        }],
        email: [{
          required: true, message: "请输入邮箱", trigger: "blur"
        }],
        content: [{
          required: true, message: "请输入内容", trigger: "blur"
        }],
      }
    }
  },
  methods: {
    /* 添加 */
    toAdd(){
      this.$refs["formRef"].validate(flag=>{
        if (flag) {
          sysTopicApi.save(this.form).then(rs=>{
            this.$message.success(rs.msg)
            this.$emit("after")
            this.$emit("close")
          })
        }
      })
    },
    toReset(){
      this.$refs["formRef"].clearValidate()
      this.form = {}
    }
  }
}
</script>

<style scoped>

</style>
