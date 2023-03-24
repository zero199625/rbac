
<!--
    当添加 部门时，parentId =undefined        母部门可以更改
    当添加 子部门时，parentId =row.deptId     母部门不能更改
-->

<template>
  <div>
    <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
      <el-form-item prop="parentId" label="上级部门" style="width: 95%">
        <!--
            v-model：双向数据绑定    与prop属性(其所处列对应于数据源的属性)中的值相对应  如 ： prop="parentId" -> v-model="form.parentId"
            options：渲染树形结构的 数据源
            normalizer：用于将options的值，转换为符合vue-treeselect要求的数据格式    分配id、label还有children
            show-count：是否显示子节点数量   默认false
            multiple：是否可以多选  默认false
         -->
        <treeselect
          v-model="form.parentId"
          :options="deptList"
          :normalizer="normalizer"
          :show-count="true"
          placeholder="请选择是上级部门"
          :disabled="isAble"
        >
        </treeselect>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="deptName" label="部门名称">
            <el-input v-model="form.deptName" placeholder="请输入部门名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" style="margin-left: 20px">
          <el-form-item prop="orderNum" label="显示排序">
            <el-input-number v-model="form.orderNum" :min="1" :max="100"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="leader" label="负责人">
            <el-input v-model="form.leader" placeholder="请输入负责人"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" style="margin-left: 20px">
          <el-form-item prop="phone" label="联系电话">
            <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" style="margin-left: 20px">
          <el-form-item prop="status" label="部门状态">
            <el-radio-group v-model="form.status">
              <!--       状态       -->
              <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div align="center">
      <el-button type="primary" @click="doSearch">添加</el-button>
      <el-button type="warning" @click="doReset">重置</el-button>
    </div>
  </div>
</template>

<script>
// 引入vue-treeselect组件
import Treeselect from "@riophae/vue-treeselect"
// 引入vue-treeselect样式
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

import sysDictDataApi from "@/api/system/sysDictData"
import sysDeptApi from "@/api/system/sysDept";
// 引入 转化为树形结构数据的方法
import {handleTree} from "@/utils/powernode"

export default {
  name: "sys-dept-add",
  // 2.注册组件
  components: {
    Treeselect
  },
  props: {
    parentId: {
      type: Number,
      default: undefined
    }
  },
  watch: {
    parentId: {
      immediate: true,
      handler: function (newVal, oldVal){  //newVal=parentId
        /* 让监听器之间只监听一次，这样母部门能改的可以一直改，不能改的永远不能改 */
        if (this.num==1) {
          return
        }
        this.form.parentId = this.parentId;
        if (newVal==null) { // 从右边下拉菜单添加，不能更改
          this.isAble = false
        }else {
          this.isAble = true
        }
        this.num++;
      }
    }
  },
  data(){
    return{
      /* 让监听器只生效一次 */
      num: 0,
      /* 表单数据 */
      form: {
        status: 0
      },
      /* 表单数据校验规则 */
      formRules: {
        parentId:[{
          required: true, message: "请选择上级部门", trigger: "blur"
        }],
        deptName:[{
          required: true, message: "请选择部门名称", trigger: "blur"
        }],
        orderNum:[{
          required: true, message: "请选择排序", trigger: "blur"
        }],
        leader:[{
          required: true, message: "请选择负责人", trigger: "blur"
        }],
        phone:[{
          required: true, message: "请选择联系电话", trigger: "blur"
        }],
        email:[{
          required: true, message: "请选择邮箱", trigger: "blur"
        }]
      },
      /* 树形下拉表单 数据源 */
      deptList: [],
      /* 状态数据 */
      statusOptions: [],
      /* 是否能够更改上级部门 */
      isAble: false
    }
  },
  created() {
    this.showStatus()
    this.showDeptList()
  },
  methods: {
    /* 获取状态数据 */
    showStatus(){
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions = rs.data
      })
    },
    /* 获取树形结构的数据源 */
    showDeptList(){
      // 1.获取所有部门信息
      sysDeptApi.listDept(null).then(rs=>{
        // 2.将获取到的数据，转化为 树形结构
        this.deptList =  handleTree(rs.data,"deptId")
      })
    },
    /* 将options的值，转换为符合vue-treeselect要求的数据格式 ， 操作treeselect标签 */
    normalizer(node){
      // 当子节点为空，也就是children=[]时，去掉子节点   (vue-treeselect中，即使为children属性分配一个空数组，依然会显示有分支，这时就可以使用normalizer去掉children属性)
      if (node.children && !node.children.length) {
        delete node.children
      }
      /*
      *   node：数据源(deptList)中的每个节点(每条数据对象)
      *   id：每个节点的id
      *   label：每个节点显示的文本
      *   children：每个节点的子节点
      * */
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    },
    /* 添加部门 */
    doSearch(){
      this.$refs["formRef"].validate(flag=>{
        if (flag) {
          sysDeptApi.save(this.form).then(rs=>{
            this.$message.success(rs.msg)
            this.$emit("after")
            this.$emit("close")
            this.doReset()
          })
        }
      })
    },
    /* 重置表单 */
    doReset(){
      /* 移除校验结果 */
      this.$refs["formRef"].clearValidate()
      /* 重置表单 */
      // isAble=true：说明点击的是右边的添加，因此不能重置母部门
      if (this.isAble) {
        this.form = {
          status: 0,
          parentId: this.form.parentId
        }
        return
      }
      // 点击的是上面的添加，可以重置母部门
      this.form = {
        status: 0
      }
    }
  }
}
</script>

<style scoped>

</style>

