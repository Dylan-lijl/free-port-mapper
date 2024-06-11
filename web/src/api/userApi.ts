import worker from '@/config/http';
import { user } from '@/config/uris';
import { completeUri } from '@/util/UrlUtil';
/**
 * 用户登录
 * @param {Object} data 数据
 * @returns token
 */
export function userLogin(data) {
  return worker({
    url: completeUri(user.login),
    method: 'post',
    data
  });
}
/**
 * 用户登出
 * @param data 数据
 * @returns 是否成功
 */
export function userLogout(data) {
  return worker({
    url: completeUri(user.logout),
    method: 'post',
    data
  });
}
/**
 * 修改密码
 * @param {Object} data 数据
 * @returns void
 */
export function userUpdatePassword(data) {
  return worker({
    url: completeUri(user.updatePassword),
    method: 'post',
    data
  });
}