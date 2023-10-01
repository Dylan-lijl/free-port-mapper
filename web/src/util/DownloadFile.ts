/**
 * 
 * @param {Blob} obj 流对象
 * @param {String} filename 文件名称
 */
export function downloadBlob(obj, filename) {
  const url = window.URL.createObjectURL(obj);
  downloadUrl(url, filename);
  window.URL.revokeObjectURL(url);
}
/**
 * 
 * @param {String} url 下载路径
 * @param {String} filename 文件名称
 */
export function downloadUrl(url, filename) {
  const downloadLink = document.createElement('a');
  downloadLink.href = url;
  downloadLink.download = filename;
  document.body.appendChild(downloadLink);
  downloadLink.click();
  document.body.removeChild(downloadLink);
}