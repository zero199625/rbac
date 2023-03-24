<template>
  <div>
    <el-input
      placeholder="输入关键字进行过滤"
      v-model="deptName">
    </el-input>

    <!--
        data：数据源
        props：配置选项，具体配置在data()中进行配置
        default-expand-all：是否默认展开
        filter-node-method：对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
        ref：对当前节点进行操作的标识   在methods中使用 this.$refs["ref值"] 拿到表单对象，进行操作
        node-click事件：每当节点被点击，就会执行绑定的方法
    -->
    <el-tree
      class="filter-tree"
      :data="deptList"
      :props="defaultProps"
      :filter-node-method="filterNode"
      ref="tree"
      @node-click="nodeClick"
    >
    </el-tree>
  </div>
</template>

<script>
import sysDictDataApi from "@/api/system/sysDictData";
import sysDeptApi from "@/api/system/sysDept";
import {handleTree} from "@/utils/powernode";
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "sys-user-left-tree",
  components: {
    Treeselect
  },
  watch: {
    /* 监听deptName，一旦deptName发生变化，就执行该方法 */
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  data(){
    return {
      /* 查询参数 */
      deptName: undefined,
      /* 部门数据参数 */
      deptList: [],
      defaultProps: {
        children: 'children',
        label: 'deptName'
      }
    }
  },
  created() {
    this.showDept()
  },
  methods: {
    /* 获取部门信息 */
    showDept(){
      sysDeptApi.listDept(this.deptQuery).then(rs=>{
        /* 将查询到的部门参数转化为 树形结构 */
        this.deptList = handleTree(rs.data,"deptId")
      })
    },
    /* 对树节点进行筛选时，就会调用次函数    这里的value就是输入框中绑定的deptName，data是那一整条数据对象 */
    filterNode(value, data) {
      if (!value) return true;
      /* 如果deptName中，有输入框中输入的内容，返回true */
      return data.deptName.indexOf(value) !== -1;
    },
    /* 每当节点被点击，就会调用该函数  可以当做查询方法   这里的data被选中的节点对应的那一整条数据对象，相当于常见的row */
    nodeClick(data){
      /*
      *   this.$emit()
      *     第一个参数：父组件传给子组件的事件
      *     第二个参数：子组件传给父组件的参数(可变参数)
      *  */
      this.$emit("after",data.deptId)
    },
  }
}
</script>

<style scoped>

</style>
