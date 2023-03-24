<!--
    字典数据页面   根据字典类型(dictType)进行编辑
-->

<template>
  <div>
    <!--  字典数据查询  -->
    <el-card>
      <el-form :model="page" :inline="true">
        <el-form-item>
          <el-input v-model="page.params.dictLabel" placeholder="字典标签"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="page.params.status" placeholder="请选择状态">
            <!-- 数据库中，status和dictLabel相对应 -->
            <!--            <el-option :value="0" label="正常"></el-option>
                        <el-option :value="1" label="停用"></el-option>-->
            <el-option v-for="dictData in statusOptions" :label="dictData.dictLabel" :value="dictData.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="doSearch">查询</el-button>
          <el-button type="warning" icon="el-icon-refresh" @click="doReset">重置</el-button>
          <!--      添加字典数据是根据字典类型dictType来添加的，因此得传参      -->
          <el-button type="success" icon="el-icon-plus" @click="toAdd">添加字典数据</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 字典数据表格 -->
    <el-card style="margin-top: 20px">
      <el-table :data="dataPage.list">
        <el-table-column prop="dictCode" label="字典编码" align="center"></el-table-column>
        <el-table-column prop="dictLabel" label="字典标签" align="center"></el-table-column>
        <el-table-column prop="dictValue" label="字典类型" align="center"></el-table-column>
        <el-table-column prop="dictSort" label="字典排序" align="center"></el-table-column>
        <el-table-column prop="dictType" label="字典类型" align="center"></el-table-column>
        <el-table-column prop="status" label="状态" align="center">
          <template slot-scope="scope">
            <!--                  <el-tag v-if="scope.row.status==0" type="success">正常</el-tag>
                              <el-tag v-else type="danger">停用</el-tag>-->
            <!--                  <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==scope.row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>-->
                  <!--       全局组件     -->
            <dict-tag :value="scope.row.status" :data-list="statusOptions"></dict-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="字典备注" align="center"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-dropdown>
                        <span class="el-dropdown-link">
                          <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
              <el-dropdown-menu slot="dropdown" align="center">
                <el-dropdown-item>
                  <!--            修改得根据字典编码dictCode进行修改，因此得传参数            -->
                  <el-button type="text" icon="el-icon-edit" @click="toUpdate(scope.row)">修改</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-delete" @click="toDelete(scope.row)">删除</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!--  添加字典数据页面(根据字典类型添加)    -->
      <el-dialog :visible.sync="showAddDialog" v-if="showAddDialog" :title="title" :close-on-click-modal="false">
        <sys-dict-data-add :dict-type="dictType" @after="doSearch" @close="doClose"></sys-dict-data-add>
      </el-dialog>

      <!--   修改字典数据类型(根据字典编码dictCode修改)   -->
      <el-dialog :visible.sync="showUpdateDialog" v-if="showUpdateDialog" :title="title" :close-on-click-modal="false">
        <sys-dict-data-update :active-id="activeId" @after="doSearch" @close="doClose"></sys-dict-data-update>
      </el-dialog>
    </el-card>

  </div>
</template>

<script>
// 导入字典数据api
import sysDictDataApi from "@/api/system/sysDictData"
/* 引入添加字典数据子组件 */
import SysDictDataAdd from "./sys-dict-data-add"
/* 引入修改字典数据子组件 */
import SysDictDataUpdate from "./sys-dict-data-update"

export default {
  name: "sys-dict-type-list",
  /* 注册子组件 */
  components: {
    SysDictDataAdd,
    SysDictDataUpdate
  },
  data() {
    return {
      /* (根据参数)分页查询字典数据 */
      page: {
        pageNumber: 1,
        pageSize: 100,
        params: {
          dictType: undefined,
          dictLabel: undefined,
          status: undefined
        }
      },
      /* 表单字典数据和分页信息接收 */
      dataPage: {
        list: []
      },
      /* 添加字典数据弹出框 */
      showAddDialog: false,
      /* 修改字典类型弹出框 */
      showUpdateDialog: false,
      /* 弹出框标题 */
      title: undefined,
      /* 传递给修改子组件的参数 */
      activeId: undefined,
      /* 传递给添加子组件的参数 */
      dictType: undefined,
      /* sys_status_type对应的字典数据 */
      statusOptions: []
    }
  },
  created() {
    this.doSearch();
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
    /* 分页查询 */
    doSearch() {
      /* 路由传参： this.page.params.dictType = this.$route.params.dictType  从路由中获取到字典类型dictType，而不是组件传参 */
      // 从路由中获取字典类型dictType
      this.page.params.dictType = this.$route.params.dictType
      sysDictDataApi.getByPage(this.page).then(rs => {
        this.dataPage.list = rs.data.list;
      })
    },
    /* 重置查询参数 */
    doReset() {
      this.page.params = {}
    },
    /* 点击添加字典数据 */
    toAdd() {
      this.showAddDialog = true
      this.title = "添加字典数据"
      /* 将dictType传递给添加子组件 */
      this.dictType = this.page.params.dictType
    },
    /* 修改字典数据(根据dictCode) */
    toUpdate(row) {
      this.showUpdateDialog = true
      this.title = "修改字典数据"
      /* 将dictCode传递给修改子组件 */
      this.activeId = row.dictCode
    },
    /* 删除字典数据 */
    toDelete(row) {
      this.$confirm("确定删除"+row.dictLabel+"吗?",{
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(()=>{
        sysDictDataApi.delete(row.dictCode).then(rs=>{
          this.$message.success(rs.msg)
          this.doSearch()
        })
      })
    },
    /* 关闭弹出框(添加和修改) */
    doClose() {
      this.showAddDialog = false
      this.showUpdateDialog = false
      this.title = undefined
    }
  }
}
</script>

<style scoped>

</style>
