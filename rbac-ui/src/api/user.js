import request from '@/utils/request'

/*
*   向后端发送请求的方法
* */

/*
*   2. api/user.js login接口    用户登录，将响应数据返回给store包下的user.js文件中的login()方法
* */
export function login(params) { // data中包含用户名、密码和验证码
  return request({
    url: '/login',
    method: 'post',
    params: params
  })
}

/*
*   获取用户信息
* */
export function getInfo() {
  return request({
    url: '/getUserInfo',
    method: 'get'
  })
}

/*
*   用户退出
* */
export function logout() {
  return request({
    url: '/logout',
    method: 'get'
  })
}

/*
*   获取验证码
* */
export function getCaptcha() {
  return request({
    url: '/captcha/getCaptcha',
    method: 'get'
  })
}
