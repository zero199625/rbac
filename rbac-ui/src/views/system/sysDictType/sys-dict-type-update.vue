<template>
  <div>
    <el-form  :model="form" :rules="formRules" ref="formRef" size="mini" label-width="80px">
      <el-form-item prop="dictName" label="字典名称">
        <el-input v-model="form.dictName" placeholder="请输入字典名称"></el-input>
      </el-form-item>
      <el-form-item prop="dictType" label="字典类型">
        <el-input v-model="form.dictType" placeholder="请输入字典类型"></el-input>
      </el-form-item>
      <el-form-item prop="remark" label="备注">
        <el-input type="textarea" v-model="form.remark" placeholder="请输入备注"></el-input>
      </el-form-item>
      <el-form-item label="状态" >
        <el-radio-group v-model="form.status">
          <!--
              取出每一个sys_status_type对应的字典数据
              dicData.dictValue=0时，dicData.dictLabel="正常"
              dicData.dictValue=1时，dicData.dictLabel="停用"
          -->
          <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doUpdate" size="mini">修改</el-button>
        <el-button type="warning" @click="toReset" size="mini">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import sysDictTypeApi from "@/api/system/sysDictType"
import sysDictDataApi from "@/api/system/sysDictData";

export default {
  name: "sys-dict-type-update",
  /* 接收父组件传递过来的参数 */
  props: {
    activeId: {
      // 默认值为undefined
      default: undefined,
      // 参数类型为数字
      type: Number
    }
  },
  /* 监听器，监听传递过来的参数，默认值为undefined，一旦参数发生变化，就执行handler方法 */
  watch: {
    activeId: {
      immediate: true,
      /* 一旦参数发生变化，将参数赋值给表单 */
      handler: function (newVal, oldVal){   // newVal：传过来的值(activeId)   oldVal：之前的值
        /* 根据id查询数据，然后将查询到的数据给到表单 */
        /* 从数据库中查询，而不是直接从父组件中拿，这样就不会出现父组件中数据随修改数据同时变化的情况了(假修改) */
        sysDictTypeApi.get(this.activeId).then(rs=>{
          console.log(rs)
          this.form = rs.data;
        })
      }
    }
  },
  data(){
    return{
      /* 添加表单 */
      form: {
        status: 0,
        dictName: undefined,
        dictType: undefined,
        remark: undefined
      },
      /* 校验规则 */
      formRules:{
        dictName:[{
          required: true, message: "请输入字典名称", trigger: "blur"
        }],
        dictType:[{
          required: true, message: "请输入字典类型", trigger: "blur"
        }],
        remark:[{
          required: true, message: "请输入字典备注", trigger: "blur"
        }]
      },
      /* sys_status_type对应的字典 */
      statusOptions: []
    }
  },
  created() {
    this.showStatus();
  },
  methods: {
    /* 显示状态 */
    showStatus() {
      // 根据字典类型 sys_status_type 获取字典数据
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs => {
        this.statusOptions = rs.data;
      })
    },
    /* 修改 */
    doUpdate(){
      this.$refs["formRef"].validate(flag=>{
        if (flag) {
          sysDictTypeApi.update(this.form).then(rs=>{
            this.$message.success(rs.msg);
            this.$emit("after");
            // 调用父组件的关闭功能
            this.$emit("close");
            // 重置修改表单
            this.toReset();
          })
        }
      })
    },
    /* 重置 */
    toReset(){
      this.form = {status: 0}
    }
  }
}
</script>

<style scoped>

</style>
