
import request from "@/utils/request";

let moduleName = "/sysMsg"


export default {


  /**
   *  分页查询邮箱
   */
  getByPage(data){
    return request({
      url: `${moduleName}/getByPage`,
      method: "post",
      data
    })
  },

  /**
   *  根据消息ID查询消息
   */
  getByMsgId(msgId){
    return request({
      url: `${moduleName}/getByMsgId/`+msgId,
      method: "get"
    })
  },


  /**
   *  点到面发送消息
   */
  sendAll(data){
    return request({
      url: `${moduleName}/sendAll`,
      method: "post",
      data
    })
  },

  /**
   *  点到点发送消息
   */
  send(data){
    console.log(111)
    console.log(data.msgReceiveId)
    return request({
      url: `${moduleName}/send`,
      method: "post",
      data:data
    })
  },


  /**
   *  更新角色
   */
  update(data){
    return request({
      url: `${moduleName}/update`,
      method: "put",
      data
    })
  },


  /**
   *  将消息放入垃圾箱
   */
  putToDustbin(msgId){
    return request({
      url: `${moduleName}/putToDustbin/`+msgId,
      method: "delete"
    })
  },

  /**
   *  彻底删除消息
   */
  delete(msgId){
    return request({
      url: `${moduleName}/delete/`+msgId,
      method: "delete"
    })
  },


  /**
   *  根据id修改消息      是否已读
   */
  isRead(msgId){
    return request({
      url: `${moduleName}/isRead/`+msgId,
      method: "put"
    })
  },




}
