/**
 * 拼接完整路径
 * @param {String} url 路径
 * @returns 
 */
export function completeUri(url) {
  if (!url) {
    return null;
  }
  return "/api/" + replaceBothEnds(url, "/");
}
/**
 * 下载文件的url
 * @param {String} path 路径
 * @returns 如果是协议开头则直接返回,不是则转成后端的代理url
 */
export function getServerFilePath(path) {
  if (path && !hasProtocol(path)) {
    if (path.startsWith("#")) {
      //前端资源
      path = "/icons/" + replaceBothEnds(replaceStartsStr(path, '#'), "/");
    } else if (path.startsWith("$")) {
      //andtv图标
      path = replaceBothEnds(replaceBothEnds(replaceStartsStr(path, '$'), "/"), ".svg");
    } else {
      //后端资源
      path = completeUri("file/download") + "?path=" + "/" + replaceBothEnds(path, "/");
    }
  }
  return path;
}


//================================================================
export function replaceEndsStr(val, reg) {
  if (val && val.endsWith(reg) && val.length - reg.length >= 0 && reg.length <= val.length) {
    val = val.substring(0, val.length - reg.length);
  }
  return val;
}
/**
 * 去除第一个
 */
export function replaceStartsStr(val, reg) {
  if (val && val.startsWith(reg) && reg.length > 0 && reg.length <= val.length) {
    val = val.substring(reg.length);
  }
  return val;
}
/**
 * 替换两端
 */
export function replaceBothEnds(val, reg) {
  return replaceEndsStr(replaceStartsStr(val, reg), reg);
}
/**
 * 是否是uri
 * @param {String} v 字符串
 */
export function hasProtocol(str) {
  return /^[a-zA-Z][a-zA-Z0-9+.-]*:\/\//.test(str);
}