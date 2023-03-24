<template>
  <div>
    <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
      <el-form-item label="上级菜单">
        <!--
            v-model：双向绑定数据，与普通的绑定一样，将点击选中的parentId传递给 data中定义的 数组数据form
            normalizer：对树形结构数据进行操作，绑定的是一个方法     底数据进行转化
        -->
        <treeselect
          v-model="form.parentId"
          :options="menuList"
          :normalizer="normalizer"
          :show-count="true"
          :disabled="isAble"
          placeholder="主类目"
        ></treeselect>
      </el-form-item>
      <el-form-item label="菜单类型" align="left">
        <el-radio-group v-model="form.menuType" :disabled="isAble">
          <el-radio :label="0">目录</el-radio>
          <el-radio :label="1">菜单</el-radio>
          <el-radio :label="2">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="菜单图标" v-if="form.menuType!=2">
        <el-input v-model="form.icon" placeholder="点击选择图标"></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="菜单名称" prop="menuName">
            <el-input v-model="form.menuName" placeholder="请输入菜单名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="显示排序" prop="orderNum" align="left">
            <el-input-number v-model="form.orderNum" :min="1" :max="100"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="path" v-if="form.menuType!=2">
            <!--  slot="label"  表示以文本形式展示   -->
            <span slot="label">
              <!--
                  el-tooltip标签：提示文字标签
                      content：提示内容
                      placement：提示位置   (top/bottom/left/right)
                  i标签：小图标标签
                      class：图标名称   如：el-icon-question表示问号
               -->
              <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
              路由地址
            </span>
            <el-input v-model="form.path" placeholder="请输入路由地址"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组件路径" v-if="form.menuType==1">
            <span slot="label">
              <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
              组件路径
            </span>
            <el-input v-model="form.component" placeholder="请输入组件路径"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="权限字符" v-if="form.menuType!=0">
            <span slot="label">
              <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
              权限字符
            </span>
            <el-input v-model="form.perms" placeholder="请输入权限字符"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="路由参数" v-if="form.menuType==1">
            <span slot="label">
              <el-tooltip content="访问路由的默认传递参数，如：`{'id': 1, 'name': 'ry'}`" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
              路由参数
            </span>
            <el-input placeholder="请输入路由参数"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="显示状态" align="left" v-if="form.menuType!=2">
            <span slot="label">
              <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
              显示状态
            </span>
            <el-radio-group v-model="form.visible">
              <el-radio v-for="dictData in visibleOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <!--     状态     -->
          <el-form-item label="菜单状态" align="left" v-if="form.menuType!=2">
            <span slot="label">
              <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
              菜单状态
            </span>
            <el-radio-group v-model="form.status">
              <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-button type="primary" align="center" @click="doAdd">添加</el-button>
      <el-button type="warning" align="center" @click="doReset">重置</el-button>
    </el-form>
  </div>
</template>

<script>
/* 引入vue-treeselect组件 */
import Treeselect from "@riophae/vue-treeselect"
/* 引入vue-treeselect组样式 */
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
// 引入将数据转化为 树形结构的方法
import {handleTree} from "@/utils/powernode"
import sysDictDataApi from "@/api/system/sysDictData"
import sysMenuApi from "@/api/system/sysMenu"

export default {
  name: "sys_menu_add",
  /* 注册组件 */
  components: {
    Treeselect
  },
  /* 接收父组件传过来的参数 */
  props: {
    parentObj: {
      type: Object,
      default: null
    }
  },
  watch: {
    parentObj: {
      immediate: true,
      handler: function (newVal,oldVal){  // newVal=parentObj
        /* 如果从右边点的添加，则上级菜单就是其点击的菜单  如果从上面点的，上级菜单就是主类目 */
        if (newVal.menuType==null) { // parentObj为空说明是从上面点的，上级菜单是主类目
          this.isAble = false
          this.form = {
            status: 0,
            menuType: 0,
            visible: 0,
            parentId: 0
          }
        }else {// 从右边点的添加
          this.isAble = true
          // 如果点的一级菜单，上级菜单是一级菜单
          if (newVal.menuType==0) {
            this.form = {
              status: 0,
              /* 指定要添加的菜单类型 */
              menuType: 1,
              visible: 0,
              /* 指定上级菜单 */
              parentId: newVal.menuId
            }
          } else if (newVal.menuType==1) {// 如果点的二级菜单，上级菜单是二级菜单
            this.form = {
              status: 0,
              /* 指定要添加的菜单类型 */
              menuType: 2,
              visible: 0,
              /* 指定上级菜单 */
              parentId: newVal.menuId
            }
          } else { // 三级菜单不能添加子菜单

          }
        }

        /*if (newVal != null) {
          // 二级菜单 || 三级菜单
          if (newVal.menuType == 0) {
            // 二级菜单
            this.form = {
              status: 0,
              visible: 0,
              menuType: 1,
              parentId: newVal.menuId
            }
          } else if (newVal.menuType == 1) {
            // 三级菜单
            this.form = {
              menuType: 2,
              parentId: newVal.menuId
            }
          }
        } else {
          // 空值时 默认目录（一级菜单）
          this.form = {
            status: 0,
            visible: 0,
            menuType: 0,
            parentId: 0
          }
        }*/
      }
    }
  },
  data(){
    return{
      /* 表单数据 */
      form: {
        status: 0,
        menuType: 0,
        visible: 0
      },
      /* 表单校验数据 */
      formRules:{
        menuName: [{
          required: true, message: "请输入菜单名称", trigger: "blur"
        }],
        orderNum: [{
          required: true, message: "请输入显示排序", trigger: "blur"
        }],
        path: [{
          required: true, message: "请输入路由地址", trigger: "blur"
        }]
      },
      /* status数据 数组*/
      statusOptions: [],
      /* visible数据 数组 */
      visibleOptions: [],
      /* menuType数据 数组 */
      menuTypeOptions: [],
      /* 树形结构 数据源 */
      menuList: [],
      isAble: false
    }
  },
  created() {
    this.showStatus()
    this.showVisible()
    this.showMenuList()
  },
  methods: {
    /* 获取 status 相关信息  dictValue=0: -> dictLabel="正常"  dictValue=1: -> dictLabel="停用" */
    showStatus(){
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions = rs.data
      })
    },
    /* 获取 visible 相关信息  dictValue=0: -> dictLabel="显示"  dictValue=1: -> dictLabel="隐藏" */
    showVisible(){
      sysDictDataApi.getDictDataByDictType("sys_show_hide").then(rs=>{
        this.visibleOptions = rs.data
      })
    },
    /* 显示树形结构数据 */
    showMenuList(){
      /* 由于三级菜单不能添加子菜单，因此只查询目录和菜单 */
      sysMenuApi.listMenuMC().then(rs=>{
        /* 将查询到的 数组形式的数据 转换成 树形结构数据 */
        let dataList = handleTree(rs.data,"menuId")
        // 最顶层目录   查询到的数据作为其children
        /*
          *  menuId :  顶层菜单ID             对应于id属性
          *  menuName : 顶层菜单名称          对应于label属性
          *  children：子节点                 对应于children属性
          * */
        this.menuList = [{
          "menuId": 0,
          "menuName": "主类目",
          children: dataList
        }]
       /* this.menuList = handleTree(rs.data,"menuId")*/
      })
    },
    /* 操作并返回 树形结构数据 */
    normalizer(node){
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      }
    },
    /* 添加菜单 */
    doAdd(){
      this.$refs["formRef"].validate(flag=>{
        if (flag) {
          sysMenuApi.save(this.form).then(rs=>{
            this.$message.success(rs.msg)
            this.$emit("after")
            this.$emit("close")
            this.doReset()
          })
        }
      })
    },
    /* 重置菜单 */
    doReset(){
      this.$refs["formRef"].clearValidate()
      this.form = {
        status: 0,
        visible: 0,
        menuType: this.form.menuType,
        parentId: this.form.parentId
      }
    }
  }
}
</script>

<style scoped>

</style>
