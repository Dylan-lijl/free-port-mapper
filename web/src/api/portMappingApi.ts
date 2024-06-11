import worker from '@/config/http';
import { portMapping } from '@/config/uris';
import { completeUri } from '@/util/UrlUtil';
/**
 * 消息模板列表
 * @param {Object} data 数据
 * @returns 列表
 */
export function portMappingList(data) {
  return worker({
    url: completeUri(portMapping.list),
    method: 'post',
    data
  });
}
/**
 * 消息模板新增
 * @param {Object} data 数据
 * @returns 
 */
export function portMappingAdd(data) {
  return worker({
    url: completeUri(portMapping.create),
    method: 'post',
    data
  });
}
/**
 * 消息模板更新
 * @param {Object} data 数据
 * @returns 
 */
export function portMappingUpdate(data) {
  return worker({
    url: completeUri(portMapping.update),
    method: 'post',
    data
  });
}
/**
 * 删除消息模板
 * @param {Object} data 数据
 * @returns 
 */
export function portMappingDelete(data) {
  return worker({
    url: completeUri(portMapping.delete),
    method: 'post',
    data
  });
}
/**
 * 消息模板详情
 * @param {Object} data 数据
 * @returns 详情
 */
export function portMappingInfo(data) {
  return worker({
    url: completeUri(portMapping.info),
    method: 'post',
    data
  });
}/**
 * 状态
 * @param data 数据
 * @returns 是否成功
 */
export function portMappingState(data) {
  return worker({
    url: completeUri(portMapping.state),
    method: 'post',
    data
  });
}
