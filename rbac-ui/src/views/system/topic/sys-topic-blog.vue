<template>
  <div>
    <el-card>
      <template>
        <el-button type="primary" @click="doAdd">发帖</el-button>
      </template>
      <el-table :data="topicList">
        <el-table-column prop="topicId" label="序号"></el-table-column>
        <el-table-column prop="title" label="主题"></el-table-column>
        <el-table-column prop="content" label="内容"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="createDate" label="发布时间"></el-table-column>
        <el-table-column prop="clickAmount" label="点击量"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{row}">
            <el-link type="primary" @click="doDetail(row)">详情</el-link>
          </template>
        </el-table-column>
      </el-table>
    </el-card>


    <el-dialog :visible.sync="showAddDialog" v-if="showAddDialog" :title="title" :close-on-click-modal="false">
      <sys-topic-add @after="doSearch" @close="doClose"></sys-topic-add>
    </el-dialog>


    <el-dialog :visible.sync="showDetailDialog" v-if="showDetailDialog" :title="title" :close-on-click-modal="false">
      <sys-topic-detail :active-id="activeId"></sys-topic-detail>
    </el-dialog>

    <el-pagination
      class="pagination"
      :page-size="page.pageSize"
      :current-page="page.pageNumber"
      :page-sizes="[10,20,30,40]"
      :total="total"
      layout="total,sizes,prev,pager,next,jumper"
      @current-change="pageChange"
      @size-change="sizeChange"
    >
    </el-pagination>


  </div>

</template>

<script>
import sysTopicApi from "@/api/system/sysTopic"
import SysTopicAdd from "./sys-topic-add"
import SysTopicDetail from "@/views/system/topic/sys-topic-detail";

export default {
  name: "sys-topic-blog",
  components: {
    SysTopicDetail,
    SysTopicAdd
  },
  data(){
    return {
      page: {
        pageNumber: 1,
        pageSize: 10
      },
      /* 帖子信息 */
      topicList: [],
      showAddDialog: false,
      title: undefined,
      showDetailDialog: undefined,
      activeId: undefined,
      total: undefined
    }

  },
  created() {
    this.doSearch()
  },
  methods: {
    /* 分页查询 */
    doSearch(){
      sysTopicApi.getByPage(this.page).then(rs=>{
        this.topicList = rs.data.list
        this.total = rs.data.totalCount
      })
    },
    /* 查看详情 */
    doDetail(row){
      this.showDetailDialog = true
      this.title = "详情"
      this.activeId = row.topicId
      sysTopicApi.update(row.topicId).then(rs=>{
        this.doSearch()
      })
    },
    /* 发帖 */
    doAdd(){
      this.showAddDialog = true
      this.title = "发帖"
    },
    /* 重置 */
    doClose(){
      this.showAddDialog = false
      this.title = undefined
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
  }
}
</script>

<style scoped>

</style>
