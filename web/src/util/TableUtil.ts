/**
 * 表格工具类
 */

import { Params } from "@/types/apiEntity/ApiResponse";

/**
 * 默认表格滚动配置
 * @returns 表格滚动配置
 */
export function defaultTableScroll() {
  return {
    x: 'max-content'
  }
}
/**
 * 默认分页
 * @returns 默认分页对象
 */
export function defaultPagination() {
  return {
    current: 1,
    pageSize: 10,
    total: 1,
    showSizeChanger: true,
    pageSizeOptions: ["10", "20", "50", "100", "300", "500", "1000", "5000", "10000", "2147483647"],
    showQuickJumper: true,
    defaultPageSize: 10,
  }
}
/**
 * 转换对象
 * @param pagination 分页
 * @param params 参数
 * @returns 对象
 */
export function transform(pagination, params) {
  const result: Params = {
  }
  //设置分页
  if (pagination) {
    result.pageNum = pagination.current;
    result.pageSize = pagination.pageSize;
  }
  //合并参数
  if (params) {
    if (Array.isArray(params)) {
      // 如果 params 是数组，遍历并合并到结果对象
      params.forEach((param) => {
        Object.assign(result, param);
      });
    } else if (typeof params === 'object') {
      // 如果 params 是对象，直接扩展到结果对象
      Object.assign(result, params);
    }
  }
  return result;
}