
import request from "@/utils/request";

let moduleName = "/sysRole"


export default {


  /**
   *  分页查询
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
   *  添加角色
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
   *  更新角色
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
   *  删除角色
   * @param roleId
   * @returns {AxiosPromise}
   */
  delete(roleId){
    return request({
      url: `${moduleName}/delete/`+roleId,
      method: "delete"
    })
  },


  /**
   *  根据角色ID查询角色
   * @param roleId
   * @returns {AxiosPromise}
   */
  get(roleId){
    return request({
      url: `${moduleName}/get/`+roleId,
      method: "get"
    })
  },


  /**
   *  根据 角色ID 查询当前角色所拥有的菜单权限
   * @returns {AxiosPromise}
   */
  getCurrentRoleHasMenuIdsByRoleId(roleId){
    return request({
      url: `${moduleName}/getCurrentRoleHasMenuIdsByRoleId/`+roleId,
      method: "get"
    })
  },


  /**
   *  保存角色菜单之间的关联
   *      后端收到的是表单参数  因此得将前端的参数 通过拼接 转化为表单参数
   *        如：roleId=1&menuId=10001&menuId=10002&...
   * @param roleId
   * @param menuIds
   */
  saveRoleMenu(roleId,menuIds){
    let params = "roleId="+roleId
    /* 遍历menuIds数组，逐个进行拼接 */
    menuIds.filter(menuId=>{
      params = params + "&menuIds=" + menuId  // 注意字符串中的是menuIds
    })
    return request({
      //  注意这里有一个 ?
      url: `${moduleName}/saveRoleMenu?`+params,
      method: "post"
    })
  },


  /**
   *  根据用户ID查询用户所拥有的 所有角色ID
   * @param userId
   * @returns {AxiosPromise}
   */
  getRoleIdsByUserId(userId){
    return request({
      url: `${moduleName}/getRoleIdsByUserId/` + userId,
      method: "get"
    })
  },


  /**
   *  查询所有角色
   */
  getAllRoles(){
    return request({
      url: `${moduleName}/getAllRoles`,
      method: "get"
    })
  },

  /**
   *  保存 用户 与 角色 之间的关系
   * @param userId
   * @param roleIds
   * @returns {AxiosPromise}
   */
  saveUserRoles(userId,roleIds){
    let params = "userId=" + userId
    // 遍历角色ID数组，取出里面的每一个角色ID，进行拼接操作
    roleIds.filter(roleId=>{
      // 注意：字符串中是 roleIds
      params = params + "&roleIds=" + roleId
    })
    return request({
      // 注意：表单参数拼接在url后面
      url: `${moduleName}/saveUserRoles?` + params,
      method: "post"
    })

  }
}
