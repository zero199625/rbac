<template>

  <div>
    <!-- 查询字典类型 -->
    <el-card>
      <el-form :model="page" :inline="true">
        <el-form-item>
          <el-input v-model="page.params.dictName" placeholder="字典名称"></el-input>
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
          <el-button type="success" icon="el-icon-plus" @click="toAdd">添加字典类型</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="doCache">同步缓存</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 字典类型表格 -->
    <el-card style="margin-top: 5px">
      <el-table :data="dataPage.list">
        <el-table-column prop="dictId" label="类型编号" align="center"></el-table-column>
        <el-table-column prop="dictName" label="字典名称" align="center"></el-table-column>
        <el-table-column prop="dictType" label="字典类型" align="center">

          <!--
              通过插槽配置路由地址，根据字典类型(dictType)，点击去到对应的数据管理页面    row表示这一行的数据
                  将dictType放入路由，使用this.$route.params.dictType获取
          -->
          <template slot-scope="scope">
                <router-link :to="'/system/dictData/index/'+scope.row.dictType">{{scope.row.dictType}}</router-link>
<!--            <div>{{ scope.row.dictType }}</div>-->
          </template>


        </el-table-column>
        <el-table-column prop="status" label="状态" align="center">
          <template slot-scope="scope">
            <!--                  <el-tag v-if="scope.row.status==0" type="success">正常</el-tag>
                              <el-tag v-else type="danger">停用</el-tag>-->
            <!--                  <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==scope.row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>-->
            <!--       全局组件     -->
            <dict-tag :value="scope.row.status" :data-list="statusOptions"></dict-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
        <el-table-column prop="remark" label="字典备注" align="center"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-dropdown>
                        <span class="el-dropdown-link">
                          <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
              <el-dropdown-menu slot="dropdown"  align="center">
                <el-dropdown-item>
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
      <!--   分页查询   -->
      <el-pagination
        class="pagination"
        :page-size="page.pageSize"
        :current-page="page.pageNumber"
        :page-sizes="[10,20,30,40]"
        :total="dataPage.totalCount"
        layout="total,sizes,prev,pager,next,jumper"
        @current-change="pageChange"
        @size-change="sizeChange"
      >
      </el-pagination>
    </el-card>

    <!-- v-if 让dom重新渲染   -->
    <el-dialog :visible.sync="showAddDialog" v-if="showAddDialog" :title="title" :close-on-click-modal="false" align="center">
      <!--  @after="doSearch" @close="doClose"   给子组件传递方法，这样子组件就可以调用父组件中的方法了   -->
      <sys-dict-type-add @after="doSearch" @close="doClose"></sys-dict-type-add>
    </el-dialog>

    <el-dialog :visible.sync="showUpdateDialog" v-if="showUpdateDialog" :title="title" :close-on-click-modal="false" align="center">
      <!--  @after="doSearch" @close="doClose"   给子组件传递方法，这样子组件就可以调用父组件中的方法了   -->
      <sys-dict-type-update :active-id="activeId" @after="doSearch" @close="doClose"></sys-dict-type-update>
    </el-dialog>

  </div>
</template>

<script>
import sysDictTypeApi from "@/api/system/sysDictType"
/* 引入添加组件 */
import SysDictTypeAdd from "./sys-dict-type-add"
/* 引入修改组件 */
import SysDictTypeUpdate from "./sys-dict-type-update"
import sysDictDataApi from "@/api/system/sysDictData";

export default {
  name: 'SysDictTypeList',
  /* 注册组件 */
  components: {
    SysDictTypeAdd,
    SysDictTypeUpdate
  },
  data() {
    return {
      /* 记录查询参数 */
      page: {
        pageNumber: 1,
        pageSize: 10,
        params: {
          dictName: undefined,
          status: undefined
        }
      },
     /* /!* 记录状态 *!/
      statusOptions: [],*/
      /* 记录表格数据 */
      dataPage: {
        list: [],
        totalCount: 0, //总条数
        totalPage: 0  // 总页数
      },
      showAddDialog: false,
      showUpdateDialog: false,
      title: undefined,
      /* 传递给子组件的参数 */
      activeId: undefined,
      /* sys_status_type对应的字典数据 */
      statusOptions: []
    }
  },
  created() {
    this.doSearch()
    this.showStatus()
  },
  methods: {
    /* 显示状态 */
    showStatus() {
      // 根据字典类型 sys_status_type 获取字典数据
      sysDictDataApi.getDictDataByDictType("sys_status_type").then(rs => {
        this.statusOptions = rs.data;
      })
    },
    /* 查询字典类型 */
    doSearch() {
      sysDictTypeApi.getByPage(this.page).then(rs=>{
        this.dataPage.list = rs.data.list;
        this.dataPage.totalCount = rs.data.totalCount;
      })
    },
    /* 重置 */
    doReset() {
      this.page.params.dictName = undefined;
      this.page.params.status = undefined;
    },
    /* 添加字典类型(显示弹出框) */
    toAdd() {
      this.showAddDialog = true
      this.title = "添加字典类型"
    },
    /* 关闭(添加、修改)弹出框 */
    doClose(){
      this.showAddDialog = false
      this.showUpdateDialog = false
      this.title = undefined
    },
    /* 修改，打开修改对话框 */
    toUpdate(row) {
      this.showUpdateDialog = true
      this.title = "修改字典类型"
      /* 将参数传递给子组件 */
      this.activeId = row.dictId;
    },
    /* 删除 */
    toDelete(row) {
      this.$confirm("确定要删除【"+row.deptName+"】记录吗?",{
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(()=>{
        sysDictTypeApi.delete(row.dictId).then(rs=>{
          this.$message.success(rs.meg);
          this.doSearch();
        })
      })
    },
    /* 页码发生变化 */
    pageChange(num) {
      this.page.pageNumber = num;
      this.doSearch();
    },
    /* 每条页数发生变化 */
    sizeChange(size) {
      this.page.pageSize = size;
      this.doSearch();
    },
    /* 同步缓存 */
    doCache() {
      sysDictTypeApi.refreshCache().then(rs=>{
        this.$message.success(rs.msg)
      })
    }
  }
}
</script>

<style scoped>

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}

</style>
