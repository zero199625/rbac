<template>
  <div>
    <el-card style="margin-top: 20px">
      <el-table :data="dataPage.list">
        <el-table-column prop="msgTitle" label="消息标题" align="center"></el-table-column>
        <el-table-column prop="msgSender" label="发送人" align="center">
          <template slot-scope="{row}">
            <div>amdin</div>
          </template>
        </el-table-column>
        <el-table-column prop="msgDeleteTime" label="放入时间" align="center"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{row}">
            <el-button type="text" icon="el-icon-delete" @click="doDelete(row)" align="center">删除</el-button>
          </template>
        </el-table-column>

      </el-table>

      <!--   分页信息   -->
      <el-pagination
        :current-page="page.pageNumber"
        :page-size="page.pageSize"
        :page-sizes="[2,5,10,20]"
        :total="dataPage.totalCount"
        layout="total, sizes, prev, pager, next, jumper "
        @current-change="pageChange"
        @size-change="sizeChange">
      </el-pagination>



    </el-card>

  </div>
</template>

<script>
import sysMsgApi from "@/api/system/sysMsg"

export default {
  name: "sys-msg-dustbin",
  data(){
    return {
      /* 查询信息 */
      page: {
        pageNumber: 1,
        pageSize: 10,
        params: {
          /* 邮箱编号    收件箱 0 */
          msgMailbox: 2
        }
      },
      /* 表格数据 */
      dataPage: {
        list: [],
        totalCount: 0, //总条数
        totalPage: 0  // 总页数
      },
    }
  },
  created() {
    this.doSearch()
  },
  methods: {
    doSearch() {
      sysMsgApi.getByPage(this.page).then(rs=>{
        this.dataPage.list = rs.data.list;
        this.dataPage.totalCount = rs.data.totalCount;
      })
    },
    /* 删除 */
    doDelete(row){
      this.$confirm("确定要删除吗？","删除用户",{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(()=>{
        sysMsgApi.delete(row.msgId).then(rs=>{
          this.$message(rs.msg)
          this.doSearch()
        })
      })
    },
    /* 页码发生变化时调用的函数 */
    pageChange(num){
      this.page.pageNumber = num
      this.doSearch()
    },
    /* 每条页数发生变化时调用的函数 */
    sizeChange(size){
      this.page.pageSize = size
      this.doSearch()
    }
  }


}
</script>

<style scoped>

</style>
