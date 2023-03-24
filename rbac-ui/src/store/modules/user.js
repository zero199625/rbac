
/* 引入api包下的user.js文件中的 登录(login)，退出(logout)和获取信息的方法(getInfo)，用于向后端放出请求 */
import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login

  /* 用户点击登录，执行此方法 */
  login({ commit }, userInfo) {
    /* 获取用户名和密码 */
    const { username, password, code } = userInfo
    return new Promise((resolve, reject) => {
      /*
        *   1.执行登录处理(store/modules/user.js) login(username,password)
        * */
      /* 此处执行的是api包下的user.js文件中的login()方法，将用户名、密码和验证码发送给后端，并获得响应数据 */
      login({ username: username.trim(), password: password, code: code }).then(response => {
        /*
        *   3.响应结果到(store/modules/user.js) 获取登录成功的用户信息(将响应结果response存入token)
        * */
        /* 此处的response是后端的响应数据 */
        const { data } = response
        // 将响应数据中的token给到sore中的文件进行储存，告诉服务器用户已经登录
        commit('SET_TOKEN', data)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  /*
  *   获取用户信息  此处获取响应数据是为了获取动态路由，并将角色信息与动态路由进行绑定
  * */
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      /*
      *   点击登录的同时，获取到用户的响应数据，其中包含 permissions 和 user对象
      *       到api包下的user.js文件中，访问getInfo()方法，获取当前登录的用户信息
      * */
      getInfo(state.token).then(response => {
        // 将响应数据给到data
        const { data } = response
        /* 如果没有获取到响应数据 */
        if (!data) {
          reject('Verification failed, please Login again.')
        }
        /* 获取到的响应数据中的 permissions 和 user对象  这里的变量名 要和后端传过来的变量名要一致！！！！！！！！！！ */
        const { permissions, sysUser } = data
        // const { roles, name, avatar, introduction } = data

        // roles must be a non-empty array
        /* 如果没有该角色 */
        /* if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!')
        }*/

        /* 角色存在，将用户信息存储起来 */
        // 用户权限
        commit('SET_ROLES', permissions)
        // 用户对象
        commit('SET_NAME', sysUser.username)
        // 用户头像
        commit('SET_AVATAR', 'https://img1.baidu.com/it/u=3682439061,3052060188&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=707')
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  /* 用户退出 */
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        /* 用户信息置空 */
        commit('SET_TOKEN', '')
        /* 权限置空 */
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
