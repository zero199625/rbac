<template>
  <div>
    <!--
        el-descriptions标签 不用绑定数据，在data()中定义数据之后，就可以直接使用了
     -->
    <el-descriptions class="margin-top" :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户ID
        </template>
        {{data.userId}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          部门名称
        </template>
        <el-tag v-for="dept in deptList" v-if="dept.deptId==data.deptId" type="warning">{{dept.deptName}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          用户名称
        </template>
        {{data.userName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          用户类型
        </template>
        <el-tag type="success">{{data.userType==0?"超级用户":"普通用户"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          邮箱
        </template>
        {{data.email}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          性别
        </template>
        <el-tag type="primary">{{data.sex==0?"男":data.sex==1?"女":"未知"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          电话
        </template>
        <el-tag type="primary">{{data.phone}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          状态
        </template>
        <el-tag type="success">{{data.status==0?"正常":"停用"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          创建人
        </template>
        {{data.createBy}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          创建时间
        </template>
        {{data.createTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          更新人
        </template>
        {{data.updateBy}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          更新时间
        </template>
        {{data.updateTime}}
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
import sysDictDataApi from "@/api/system/sysDictData"
import sysDeptApi from "@/api/system/sysDept"



export default {
  name: "sys-user-detail",
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
        this.data = JSON.parse(JSON.stringify(newVal))
      }
    }
  },
  data(){
    return {
      data: {},
      /* 部门数据 */
      deptList: [],
      /* 状态参数 */
      statusOptions: [],
      /* 性别参数 */
      sexOptions: [],
    }
  },
  created() {
    this.showDept()
  },
  methods: {
    /* 获取部门数据 */
    showDept(){
      sysDeptApi.listDept(null).then(rs=>{
        this.deptList = rs.data
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
  }
}
</script>

<style scoped>

</style>
