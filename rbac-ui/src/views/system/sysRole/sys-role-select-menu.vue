<template>
  <div>
    <!--
        data：绑定的数据
        show-checkbox：节点是否可被选择  默认false
        default-expand-all：是否默认展开所有节点   默认false
        node-key：唯一标识(一般为主键ID)   每勾选一个节点，当前节点就会有一个独一无二的key，用来识别该节点
        ref：标记数据  通过 this.$refs["ref值"] 来对数据进行操作
        props：在data()中对当前数据进行配置
     -->
    <el-tree
      :data="roleMenuList"
      :show-checkbox="true"
      node-key="menuId"
      ref="tree"
      :props="defaultProps">
    </el-tree>
    <el-button type="primary" size="mini" @click="doStore">保存</el-button>
  </div>
</template>

<script>
import sysRoleApi from "@/api/system/sysRole";
import {handleTree} from "@/utils/powernode";
import sysMenuApi from "@/api/system/sysMenu"

export default {
  name: "sys-role-select-menu",
  props: {
    activeId: {
      type: Number,
      default: undefined
    }
  },
  watch: {
    activeId: {
      immediate: true,
      handler: function (newVal,oldVal){
        /* 根据roleId获取当前角色所拥有的的 菜单，获取的rs.data是菜单数组   */
        sysRoleApi.getCurrentRoleHasMenuIdsByRoleId(newVal).then(rs=>{
          /*
          *   setCheckedKeys：将响应结果中的 菜单数组，与勾选节点的key的数组绑定
          *     第一个参数：与节点相联系 的数组 (必须是数组)
          *     第二个参数：true：仅设置叶子节点的选中状态    默认false
          * */
          // rs.data是菜单数组   如：[10001,10002,10003]
          this.$refs["tree"].setCheckedKeys(rs.data,true)
        })
      }
    }
  },
  data(){
    return {
      /* 角色菜单数据 */
      roleMenuList: [],
      /* 配置数据 */
      defaultProps: {
        children: 'children',
        // label：显示文本
        label: 'menuName'
      },
      /* 选中的节点的key数组 */
      selectKeys: [],

    }
  },
  created() {
    this.getAllMenus()
  },
  methods: {
    /* 获取所有的菜单权限 */
    getAllMenus(){
      sysMenuApi.listMenu(null).then(rs=>{
        /* 将获取的数据转化为树形结构 */
        this.roleMenuList = handleTree(rs.data,"menuId")
      })
    },
    /* 保存角色菜单关系 */
    doStore(){
      /*
      *   getCheckedKeys()：获取选中的节点的 key 所组成的数组
      *   getHalfCheckedKeys()：获取半选中的节点的 key 所组成的数组       半选中：子节点被选中，则当前节点为半选中状态
      * */
      this.selectKeys =  this.$refs["tree"].getCheckedKeys()
      let halfSelectKeys = this.$refs["tree"].getHalfCheckedKeys();
      /*
      * 既要将选中的key返回，还要返回半选中的key
      *     如：selectKeys=[10001,10002,10003...1,101...]  其中10001,10002,10003为选中状态的key， 1,101为半选中状态的key
      * 注意：将数组数据添加到另一个数组中要是用 ... 否则添加不进去
      * */
      this.selectKeys.push(...halfSelectKeys)
      // 保存角色菜单之间的关联
      sysRoleApi.saveRoleMenu(this.activeId, this.selectKeys).then(rs=>{
        this.$message.success(rs.msg)
        this.$emit("after")
        this.$emit("close")
      })
    }
  }
}
</script>

<style scoped>

</style>
