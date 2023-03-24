
/*
*   字典类型的 API
* */

import request from '@/utils/request'

const moduleName = '/sysDictType'

export default {

  /**
   *  分页查询
   * @param data
   */
  getByPage(data) {
    return request({
      url: `${moduleName}/getByPage`,
      method: 'post',
      data: data // 由于请求参数是JSON字符串，所以这得用data属性
    })
  },

  /**
   *  添加字典类型
   * @param data
   */
  save(data) {
    return request({
      url: `${moduleName}/save`,
      method: 'post',
      data: data
    })
  },

  /**
   *  删除字典类型
   * @param dictId
   */
  delete(dictId) {
    return request({
      url: `${moduleName}/delete/` + dictId,
      method: 'delete'
    })
  },

  /**
   *  修改字典类型
   * @param data
   */
  update(data) {
    return request({
      url: `${moduleName}/update`,
      method: 'put',
      data: data
    })
  },

  /**
   *  根据dictId查询
   * @param dictId
   * @returns {AxiosPromise}
   */
  get(dictId) {
    return request({
      url: `${moduleName}/get/` + dictId,
      method: 'get'
    })
  },


  /**
   *  同步缓存
   */
  refreshCache(){
    return request({
      url: `${moduleName}/refreshCache`,
      method: "get"
    })
  }

}

