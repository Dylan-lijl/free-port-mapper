import { downloadBlob } from '@/util/DownloadFile';
import { ExportConfig, ExportLine } from '@/types/ExportFile';
export default function (config: ExportConfig) {
  const { headers, rows } = config;
  let { filename } = config;
  if (!filename || filename === '') {
    filename = new Date().getTime() + "";
  }
  filename = filename.endsWith(".txt") ? filename : filename + ".txt";
  //构建数组
  const arr = [] as string[][];
  const headerTitles = [] as string[];
  //构建map
  const map = new Map<string, ExportLine>();
  for (const header of headers) {
    const title = header.title ? header.title : header.name;
    //标题全部放进去
    headerTitles.push(title);
    map.set(title, header);
  }
  arr.push(headerTitles);
  //遍历数据
  for (const line of rows) {
    const innerArr = [] as string[];
    for (const title of headerTitles) {
      const header = map.get(title);
      if (header && header.name) {
        let val = line[header.name];
        if (val !== null && header.formatter) {
          val = header.formatter(val);
        }
        innerArr.push(val);
      } else {
        innerArr.push('');
      }
    }
    arr.push(innerArr);
  }
  let str = '';
  //转成字符串
  for (const line of arr) {
    let innerStr = '';
    for (const item of line) {
      innerStr += item + '\t';
    }
    if (innerStr.endsWith('\t')) {
      innerStr = innerStr.substring(0, innerStr.length - 1);
    }
    str += innerStr + '\r\n';
  }
  downloadBlob(new Blob([str], { type: "text/plain;charset=utf-8" }), filename);
}

/**
 * 根据数组导出
 * @param {Array} arr 双重数组 [[],[]]
 */
export function exportByArray(arr, filename = "tmp.txt") {
  if (!filename.endsWith('.txt')) {
    filename = filename + '.txt';
  }
  const BOM = '\uFEFF';
  const columnDelimiter = '\t'; //默认列分隔符','
  const rowDelimiter = '\r\n'; //默认行分隔符 '\r\n'
  if (arr.length <= 0) {
    return;
  }
  let str = '';
  for (const line of arr) {
    if (Array.isArray(line)) {
      let lineStr = ""
      for (let item of line) {
        // 写数据
        item = item === null || item === undefined ? "" : item;
        const value = (typeof item === 'object') ? JSON.stringify(item) : item;
        lineStr += value + columnDelimiter;
      }
      str += lineStr + rowDelimiter;
      continue;
    }
    const value = (typeof line === 'object') ? JSON.stringify(line) : str;
    str += value + rowDelimiter;
  }

  const blob = new Blob([BOM + str], { type: 'text/plain;charset=utf-8;' });
  downloadBlob(blob, filename);
}
/**
 * 导出文件
 * @param {String} str 内容
 * @param {String} filename 文件名
 * @returns 
 */
export function exportAny(str, filename = "tmp.txt") {
  if (!filename.endsWith('.txt')) {
    filename = filename + '.txt';
  }
  if (!str) {
    return;
  }
  const blob = new Blob([str], { type: 'text/plain;charset=utf-8;' });
  downloadBlob(blob, filename);
}