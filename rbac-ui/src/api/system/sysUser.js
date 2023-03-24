import request from "@/utils/request";
import {use} from "element-ui";

let moduleName = "/sysUser"

export default {


  /**
   *  分页查询用户
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
   *  添加用户
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
   *  更新用户
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
   *  删除用户
   * @param userId
   * @returns {AxiosPromise}
   */
  delete(userId){
    return request({
      url: `${moduleName}/delete/` + userId,
      method: "delete"
    })
  },


  /**
   *  重置密码
   * @param userId
   * @returns {AxiosPromise}
   */
  resetPwd(userId){
    return request({
      url: `${moduleName}/resetPwd/` + userId,
      method: "get"
    })
  },


  /**
   *  根据用户ID查询用户数据
   * @param userId
   * @returns {AxiosPromise}
   */
  get(userId){
    return request({
      url: `${moduleName}/get/` + userId,
      method: "get"
    })
  }






}
