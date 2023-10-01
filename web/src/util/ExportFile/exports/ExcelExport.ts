import { utils, writeFile } from 'xlsx';
import { ExportConfig, ExportLine } from '@/types/ExportFile';
/**
 * 
 * @param {Object} config 配置信息
 */
export default function (config: ExportConfig) {
  const { headers, rows } = config;
  let { filename } = config;
  if (!filename || filename === '') {
    filename = new Date().getTime() + "";
  }
  filename = filename.endsWith(".xlsx") ? filename : filename + ".xlsx";
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

  const wb = utils.book_new();
  utils.book_append_sheet(wb, utils.aoa_to_sheet(arr), 'sheet');
  writeFile(wb, filename);
}