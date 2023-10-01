import worker from '@/config/http';
import { customInfo } from '@/config/uris';
import { completeUri } from '@/util/UrlUtil';
/**
 * 消息模板列表
 * @param {Object} data 数据
 * @returns 列表
 */
export function customInfoList(data) {
  return worker({
    url: completeUri(customInfo.list),
    method: 'post',
    data
  });
}
/**
 * 消息模板新增
 * @param {Object} data 数据
 * @returns 
 */
export function customInfoAdd(data) {
  return worker({
    url: completeUri(customInfo.create),
    method: 'post',
    data
  });
}
/**
 * 消息模板更新
 * @param {Object} data 数据
 * @returns 
 */
export function customInfoUpdate(data) {
  return worker({
    url: completeUri(customInfo.update),
    method: 'post',
    data
  });
}
/**
 * 删除消息模板
 * @param {Object} data 数据
 * @returns 
 */
export function customInfoDelete(data) {
  return worker({
    url: completeUri(customInfo.delete),
    method: 'post',
    data
  });
}
/**
 * 消息模板详情
 * @param {Object} data 数据
 * @returns 详情
 */
export function customInfoInfo(data) {
  return worker({
    url: completeUri(customInfo.info),
    method: 'post',
    data
  });
}/**
 * 状态
 * @param data 数据
 * @returns 是否成功
 */
export function customInfoState(data) {
  return worker({
    url: completeUri(customInfo.state),
    method: 'post',
    data
  });
}
