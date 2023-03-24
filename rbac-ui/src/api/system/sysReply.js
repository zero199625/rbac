import request from '@/utils/request';

let moduleName = "/reply"

export default {


  /**
   *  发表帖子
   * @param data
   * @returns {Promise<AxiosResponse<*>>|*}
   */
  save(data) {
    return request({
      url: `${moduleName}/save`,
      method: "post",
      data
    })
  },


  /**
   *  根据ID获取帖子信息
   * @param topicId
   * @returns {Promise<AxiosResponse<*>>|*}
   */
  getReplyById(topicId) {
    return request({
      url: `${moduleName}/getReplyById/` + topicId,
      method: "get"
    })
  },


  /**
   *  根据 topicId查询 回复人数量
   * @param topicId
   * @returns {AxiosPromise}
   */
  replyCount(topicId) {
    return request({
      url: `${moduleName}/count/`+topicId,
      method: "get"
    })
  }

}






















