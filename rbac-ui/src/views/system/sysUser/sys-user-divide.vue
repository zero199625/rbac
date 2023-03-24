<template>
  <div>
    <!--
        props：配置(label和children)数据，具体操作在data()中进行
        node-key：唯一标识，用来识别每条数据，一般为主键ID
        ref：表示数据对象，然后到methods中对该数据对象进行处理   this.$refs["ref值"] 或 this.$refs.ref值
     -->
    <el-tree
      :data="roleList"
      show-checkbox
      default-expand-all
      node-key="roleId"
      ref="tree"
      :props="defaultProps">
    </el-tree>
    <el-button type="primary" icon="el-icon-download" @click="storeRoles">保存</el-button>
  </div>
</template>

<script>
import {handleTree} from "@/utils/powernode";

import sysRoleApi from "@/api/system/sysRole"

export default {
  name: "sys-user-divide",
  props: {
    activeObj: {
      type: Object,
      default: null
    }
  },
  watch: {
    activeObj: {
      immediate: true,
      handler: function (newVal){
        /* 将父组件中选中的用户的 userId 传给子组件 */
        this.userId = newVal.userId
        /* 将 数据库中的用户角色关系 映射到 树形结构数据 中 */
        sysRoleApi.getRoleIdsByUserId(this.userId).then(rs=>{
          /*
          *   setCheckedKeys()：通过 keys 设置目前勾选的节点
          * */
          this.$refs.tree.setCheckedKeys(rs.data,true)
        })
      }
    }
  },
  data(){
    return {
      /* 角色数据 */
      roleList: [],
      /* 配置数据 */
      defaultProps: {
        children: "children",
        label: "roleName"
      },
      userId: undefined,
      /* 选中节点的 key 组成的数据 */
      keyList: []
    }
  },
  created() {
    this.getAllRoles()
  },
  methods: {
    /* 根据用户ID获取其所拥有的的角色 */
    getAllRoles(){
      sysRoleApi.getAllRoles().then(rs=>{
        this.roleList = handleTree(rs.data, "roleId")
      })
    },
    /* 保存用户与角色之间的关系 */
    storeRoles(){
      /*
      *   this.$refs.tree 与 this.$refs["tree"]效果相同
      *     getCheckedKeys()：获取被选中的节点的 key 组成的数组
      *     getHalfCheckedKeys()：获取半选状态的节点的 key 组成的数组
      * */
      let checkedKeys = this.$refs.tree.getCheckedKeys();
      let halfCheckedKeys = this.$refs.tree.getHalfCheckedKeys();
      // 将半选状态的 key 数组，添加到全选状态的 key 组成的数组中
      checkedKeys.push(...halfCheckedKeys)
      // 保存用户与角色之间的关系
      sysRoleApi.saveUserRoles(this.userId, checkedKeys).then(rs=>{
        this.$message.success(rs.msg)
        this.$emit("close")
      })
    }
  }
}
</script>

<style scoped>

</style>
