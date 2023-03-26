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
        <el-table-column prop="msgReceiveTime" label="收件时间" align="center"></el-table-column>
        <el-table-column prop="msgRead" label="是否已读" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.msgRead==0?'danger':'success'">{{row.msgRead==0?"未读":"已读"}}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="{row}">
            <el-dropdown>
                   <span class="el-dropdown-link">
                     <el-link icon="el-icon-s-operation" type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                   </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-document" @click="doDetail(row)">详情</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-delete" @click="doDelete(row)">删除</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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


      <!--  详情弹出框  -->
      <el-dialog :visible.sync="showDetailDialog" v-if="showDetailDialog" :title="title" :destroy-on-close="true" :close-on-click-modal="false" align="center">
        <sys-msg-receive-detail @close="doClose" :msg-id="msgId"></sys-msg-receive-detail>
      </el-dialog>


    </el-card>

  </div>
</template>

<script>
import sysMsgApi from "@/api/system/sysMsg"
import SysMsgReceiveDetail from "@/views/system/sysMsg/sys-msg-receive-detail";

export default {
  name: "sys-msg-receive",
  components: {
    SysMsgReceiveDetail,
  },
  data(){
    return {
      /* 查询信息 */
      page: {
        pageNumber: 1,
        pageSize: 10,
        params: {
          /* 邮箱编号    收件箱 0 */
          msgMailbox: 0
        }
      },
      /* 表格数据 */
      dataPage: {
        list: [],
        totalCount: 0, //总条数
        totalPage: 0  // 总页数
      },
      /* 显示详细数据弹出框 */
      showDetailDialog: false,
      /* 弹窗狂标题 */
      title: undefined,
      /* 消息ID，传递给子组件 */
      msgId: undefined
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
    /* 打开查看详情弹出框 */
    doDetail(row){
      this.showDetailDialog = true
      this.title = `详情`
      console.log()
      this.msgId = row.msgId
      /* 一旦打开详情，则标记为已读 */
      sysMsgApi.isRead(row.msgId).then(rs=>{
        this.$message.success(rs.msg)
      })
      this.doSearch()
    },
    /* 删除 */
    doDelete(row){
      this.$confirm("确定要删除吗？","删除用户",{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(()=>{
        sysMsgApi.putToDustbin(row.msgId).then(rs=>{
          this.$message.success(rs.msg)
          this.doSearch()
        })
      })
    },
    /* 关闭详情弹出框 */
    doClose(){
      this.showAddDialog = false
      this.title = undefined
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
