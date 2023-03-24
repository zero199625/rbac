import request from "@/utils/request";

let moduleName = "/sysDept"

export default {

  /**
   *  查询所有部门   (根据deptName和status，且delFlag=0)
   * @param params
   * @returns {AxiosPromise}
   */
  listDept(params){
    return request({
      url: `${moduleName}/listDept`,
      method: "get",
      params
    })
  },


  /**
   *  添加部门
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
   *  修改部门
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
   *  删除部门
   * @param deptId
   * @returns {AxiosPromise}
   */
  delete(deptId){
    return request({
      url: `${moduleName}/delete/` + deptId,
      method: "delete"
    })
  },


  /**
   *  根据ID查询部门
   * @param deptId
   * @returns {AxiosPromise}
   */
  get(deptId){
    return request({
      url: `${moduleName}/get/` + deptId,
      method: "get"
    })
  }


}
