<template>
  <div>
    <el-form :model="form" :rules="formRules" ref="formRef" size="mini" label-width="80px">
      <el-form-item prop="dictType" label="字典类型">
        <!--    字典类型不能更改    -->
        <el-input v-model="form.dictType" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item prop="dictLabel" label="字典标签">
        <el-input v-model="form.dictLabel" placeholder="请输入字典标签"></el-input>
      </el-form-item>
      <el-form-item prop="dictValue" label="字典键值">
        <el-input v-model="form.dictValue" placeholder="请输入字典键值"></el-input>
      </el-form-item>
      <el-form-item prop="dictSort" label="字典排序">
        <el-input v-model="form.dictSort" placeholder="请输入字典排序"></el-input>
      </el-form-item>
      <el-form-item prop="dictSkin" label="皮肤属性">
        <el-select v-model="form.dictSkin">
          <!--     遍历皮肤数组，当选择文本时，form.dictSkin = skin.value    -->
          <el-option v-for="skin in skinOptions" :value="skin.value" :label="skin.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" placeholder="请输入备注"></el-input>
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
        <el-button type="primary" @click="toAdd" size="mini">添加</el-button>
        <el-button type="warning" @click="toReset" size="mini">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import sysDictDataApi from "@/api/system/sysDictData";

export default {
  name: "sys-dict-data-add",
  /* 父组件中传递的参数 */
  props: {
    dictType: {
      type: String,
      default: undefined
    }
  },
  watch:{
    dictType:{
      immediate:true,
      handler:function (newVal,oldVal){
        this.form.dictType = newVal
      }
    }
  },
  data(){
    return{
      form: {
        status: 0
      },
      formRules: {
        dictLabel:[{
          required: true, message: "请输入字典标签", trigger: "blur"
        }],
        dictValue: [{
          required: true, message: "请输入字典键值", trigger: "blur"
        }],
        dictSort: [{
          required: true, message: "请输入字典排序", trigger: "blur"
        }],
        remark: [{
          required: true, message: "请输入备注", trigger: "blur"
        }],
      },
      /* sys_status_type对应的字典 */
      statusOptions: [],
      /* 皮肤数组，当 值value="success"时，文本 label="成功" */
      skinOptions: [
        {
          "label":"成功",
          "value":"success"
        },
        {
          "label":"报警",
          "value":"warning"
        },
        {
          "label":"错误",
          "value":"error"
        },
        {
          "label":"默认",
          "value":"primary"
        },
        {
          "label":"危险",
          "value":"danger"
        },
      ]
    }
  },
  created() {
    this.showStatus()
  },
  methods: {
    /* 添加 */
    toAdd(){
      this.$refs["formRef"].validate(flag=>{
        if (flag) {
          sysDictDataApi.save(this.form).then(rs=>{
            this.$message.success(rs.msg);
            this.$emit("after")
            this.$emit("close")
            this.doReset()
          })
        }
      })
    },
    /* 重置字典数据 */
    toReset(){
      this.form = {status: 0}
    },
    /*    状态码(0、1)
    * 根据sys_status_type获取数据
    *     当dictValue为0时，dictLabel为正常
    *     当dictValue为1时，dictLabel为停用
    * 这就是status状态的由来
    * */
    showStatus(){
      // 根据字典类型 sys_status_type 获取字典数据
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions = rs.data;
      })
    }
  }
}
</script>

<style scoped>

</style>
