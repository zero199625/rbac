import request from "@/utils/request";

let moduleName = "/sysMenu"

export default {

  /**
   *  查询所有菜单权限   管理查询所有  普通用户根据权限查询
   * @param params
   * @returns {AxiosPromise}
   */
  listMenu(params){
    return request({
      url: `${moduleName}/listMenu`,
      method: "get",
      params
    })
  },

  /**
   *  添加菜单权限
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
   *  修改菜单权限
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
   *  删除菜单权限
   * @param menuId
   * @returns {AxiosPromise}
   */
  delete(menuId){
    return request({
      url: `${moduleName}/delete/` + menuId,
      method: "delete"
    })
  },


  /**
   *  根据ID查询菜单权限
   * @param menuId
   * @returns {AxiosPromise}
   */
  get(menuId){
    return request({
      url: `${moduleName}/get/` + menuId,
      method: "get"
    })
  },

  /**
   *  只查询菜单和目录
   * @returns {AxiosPromise}
   */
  listMenuMC(){
    return request({
      url: `${moduleName}/listMenuMC`,
      method: "get"
    })
  },


  /**
   *  获取动态路由
   */
  getRouters(){
    return request({
      url: `${moduleName}/getRouters`,
      method: "get"
    })
  }

}



















