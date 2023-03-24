<template>
  <div>

    <div>
      主题：{{topicList.title}}
      <span style="margin-left: 100px">作者：{{topicList.author}}</span>
    </div>
    <el-divider></el-divider>
    <div>
      内容：{{topicList.content}}
    </div>
    <el-divider></el-divider>
    <div v-for="reply in replyList" v-if="refresh">
      回复人：{{ reply.author }}
      <span style="margin-left: 100px">回复时间：{{ reply.createDate }}</span>
      <br>
      回复内容： {{ reply.content }}
      <el-divider></el-divider>
    </div>

    回复人：{{replyCount}}人

    <el-form :model="replyInfo" :rules="replyInfoRules" ref="replyInfoRef" label-width="80px" style="margin-top: 20px">
      <el-form-item prop="author" label="回复人">
        <el-input v-model="replyInfo.author" placeholder="请输入回复人姓名"></el-input>
      </el-form-item>
      <el-form-item prop="content" label="回复内容">
        <el-input v-model="replyInfo.content" placeholder="请输入回复内容"></el-input>
      </el-form-item>
    </el-form>

    <el-divider></el-divider>
    <template>
      <el-button type="primary" @click="doReply">回复</el-button>
      <el-button type="primary" @click="doReset">重置</el-button>
    </template>


  </div>
</template>

<script>
import sysReplyApi from "@/api/system/sysReply"
import sysTopicApi from "@/api/system/sysTopic"

export default {
  name: "sys-user-detail",
  props: {
    activeId: {
      type: Number,
      default: null
    }
  },
  watch: {
    activeId: {
      immediate: true,
      handler: function (newVal){
        sysReplyApi.getReplyById(newVal).then(rs=>{
          /* 是一个数组 */
          this.replyList = rs.data
        })
      }
    }
  },
  data(){
    return {
      replyList: [],
      topicList: {},
      replyInfo: {
        author: undefined,
        content: undefined,
        topicId: this.activeId
      },
      replyInfoRules: {
        author: [{
          required: true, message: "请输入回复人", trigger: "blur"
        }],
        content: [{
          required: true, message: "请输入回复内容", trigger: "blur"
        }]
      },
      /* 回复人数量 */
      replyCount: undefined,
      /* 显示表单 */
      showAll: true,
      /* 显示回复消息 */
      refresh: true
    }
  },
  created() {
    this.showTopicList()
    this.getReplyCount()
  },
  methods: {
    /* 获取帖子信息 */
    showTopicList(){
      sysTopicApi.getTopicById(this.activeId).then(rs=>{
        this.topicList = rs.data
      })
    },
    /* 获取回复消息 */
    getReplyInfo(){
      sysReplyApi.getReplyById(this.activeId).then(rs=>{
        this.replyList = rs.data
      })
    },
    /* 回复人数量 */
    getReplyCount(){
      sysReplyApi.replyCount(this.activeId).then(rs=>{
        this.replyCount = rs.data
      })
    },
    /* 回复 */
    doReply(){
      this.$refs["replyInfoRef"].validate(flag=>{
        console.log(this.replyInfo)
        if (flag) {
          sysReplyApi.save(this.replyInfo).then(rs=>{
            this.$message.success(rs.msg)
            this.getReplyInfo()
            this.getReplyCount()
          })
        }
      })
    },
    /* 重置 */
    doReset(){
      this.$refs["replyInfoRef"].clearValidate()
      this.replyInfo = {}
    }

  }
}
</script>

<style scoped>

</style>
