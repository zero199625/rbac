<template>
  <div>
    <!--
        el-descriptions标签 不用绑定数据，在data()中定义数据之后，就可以直接使用了
     -->
    <el-descriptions class="margin-top" :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          消息标题
        </template>
        {{data.msgTitle}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          消息内容
        </template>
        {{data.msgContent}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          发送人
        </template>
        admin
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          发送时间
        </template>
        {{data.msgSendTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          是否为系统消息
        </template>
        <el-tag type="primary">{{data.msgType==0?"是":"否"}}</el-tag>
      </el-descriptions-item>


    </el-descriptions>
  </div>
</template>

<script>
import sysMsgApi from "@/api/system/sysMsg"

export default {
  name: "sys-msg-receive-detail",
  props: {
    msgId: {
      type: Number,
      default: null
    }
  },
  watch: {
    msgId: {
      immediate: true,
      handler: function (newVal){
        sysMsgApi.getByMsgId(newVal).then(rs=>{
          console.log(rs.data)
          this.data = rs.data;
        })
      }
    }
  },
  data(){
    return {
      /* 详情数据 */
      data: {},
    }
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
