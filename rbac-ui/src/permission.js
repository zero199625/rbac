import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

/* 白名单，碰到白名单里面的路径，直接放行 */
const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

/*
    路由守卫  相当于过滤器
*/
router.beforeEach(async(to, from, next) => {
  // start progress bar
  /* 开始加载进度条 */
  NProgress.start()

  // set page title
  /* 设置页面标题 */
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  /* 获取用户登录的token */
  const hasToken = getToken()

  /*
  *   4.在向后端发送请求之前，先到这里进行判断
  * */
  // 判断当前用户是否携带token
  if (hasToken) { // 有token表示已经登录
    /* 在有token的情况下，去访问登录界面时 */
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      /* 转到主页面 */
      next({ path: '/' })
      // 结束进度条
      NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      // 如果携带token，但是没有访问登录界面，获取角色
      // determine whether the user has obtained his permission roles through getInfo
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      /* 如果有角色，放行，登陆之后，每次访问在这里就会放行*/
      if (hasRoles) {
        next()
      } else {
        /* 如果没有角色，获取用户角色信息，登录之后第一次会走这里，在获取角色之后就不用走这里了   */
        try {
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']

          /*
          *   到store包下的user.js文件中，调用getInfo()方法，获取用户权限
          * */
          const { permissions } = await store.dispatch('user/getInfo')

          // generate accessible routes map based on roles
          /*
          *   根据当前角色获取动态路由
          *     到store包下的permission.js文件中，调用generateRoutes()方法，根据权限获取动态路由
          * */
          const accessRoutes = await store.dispatch('permission/generateRoutes', permissions)

          // dynamically add accessible routes
          /* 添加动态路由，这样当前用户就拥有了 权限，下次直接放行 */
          router.addRoutes(accessRoutes)

          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          /* 执行路由 */
          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          // 结束进度条
          NProgress.done()
        }
      }
    }
  } else { // 如果没有携带token(没有登录)
    /* has no token*/
    // 如果要去的地址在白名单上，放行
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      // 不在白名单上，重定向到登录界面
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
