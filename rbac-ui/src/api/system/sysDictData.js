
import request from "@/utils/request";

let moduleName = "/sysDictData"

export default {

  /**
   *  分页查询字典数据   请求参数是JSON字符串
   * @param data
   * @returns {AxiosPromise}
   */
  getByPage(data){
    return request({
      url: `${moduleName}/getByPage`,
      method: "post",
      data
    })
  },

  /**
   *   添加字典数据   请求参数是JSON字符串
   * @param data
   * @returns {AxiosPromise}
   */
  save(data){
    return request({
      url: `${moduleName}/save`,
      method: "post",
      data
    })
  },


  /**
   *  修改字典数据      请求参数是JSON字符串
   * @param data
   * @returns {AxiosPromise}
   */
  update(data){
    return request({
      url: `${moduleName}/update`,
      method: "put",
      data
    })
  },

  /**
   *  根据ID查询字典数据
   * @param dictCode
   * @returns {AxiosPromise}
   */
  get(dictCode){
    return request({
      url: `${moduleName}/get/`+dictCode,
      method: "get"
    })
  },


  /**
   *  根据ID删除字典数据
   * @param dictCode
   * @returns {AxiosPromise}
   */
  delete(dictCode){
    return request({
      url: `${moduleName}/delete/`+dictCode,
      method: "delete",
    })
  },

  /**
   *  根据字典类型 dictType 查询字典数据 !!!!!!!!!!!!!!!!!!!!!!!!!
   * @param dictType
   * @returns {AxiosPromise}
   */
  getDictDataByDictType(dictType){
    return request({
      url: `${moduleName}/getDictDataByDictType/`+dictType,
      method: "get"
    })
  }

}



























