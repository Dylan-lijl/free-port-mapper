import { csv_format_type } from '../FileType';
import { downloadBlob } from '@/util/DownloadFile';
import { ExportConfig, ExportLine } from '@/types/ExportFile';

/**
 * 
 * @param {ExportConfig} config 配置信息
 * @param {string} csvFormatType 格式化方式
 */
export default function exportCsv(config: ExportConfig, csvFormatType = csv_format_type.comma) {
  const { headers, rows } = config;
  let { filename } = config;
  if (!filename || filename === '') {
    filename = new Date().getTime() + "";
  }
  if (Array.isArray(headers) && headers.length > 0) { //表头信息不能为空
    if (!filename.endsWith('.csv')) {
      filename = filename + '.csv';
    }
    const blob = getCsvBlob(headers, rows, csvFormatType);
    downloadBlob(blob, filename);
  }
}
/**
 * 获取blob对象
 * @param {*} headers 
 * @param {*} rows 
 * @returns 
 */
function getCsvBlob(headers: ExportLine[], rows, csvFormatType) {
  const BOM = '\uFEFF';
  const columnDelimiter = ','; //默认列分隔符','
  const rowDelimiter = '\r\n'; //默认行分隔符 '\r\n'
  let csv = headers.reduce((previous, header) => {
    return (previous ? previous + columnDelimiter : '') + (header.title || header.name);
  }, '');
  const formatFunction = getFormatFunction(csvFormatType);
  if (Array.isArray(rows) && rows.length > 0) {
    const columns = headers.map(header => header.name);
    csv = rows.reduce((previous, row) => {
      const rowCsv = columns.reduce((pre, column) => {
        if (Object.prototype.hasOwnProperty.call(row, column)) {
          let cell = row[column];
          if (cell != null) {
            const header = headers.find(item => item.name == column);
            if (header) {
              if (header.formatter != null && typeof (header.formatter) == "function") {
                cell = header.formatter(cell);
              }
            }
            if (cell != null) {
              cell = formatFunction(cell);
              if ((cell + "").includes(",") || (cell + "").includes("\n")) {
                cell = "\"" + cell + "\"";
              }
              return pre ? pre + columnDelimiter + cell : pre + cell;
            }
          }
          return pre ? pre + columnDelimiter : pre + " ";//reduce初始值为''，故第一次迭代时不会在行首加列分隔符。后面的遇到值为空或不存在的列要填充含空格的空白" ",则pre返回true，会加列分隔符
        }
        else {
          return pre ? pre + columnDelimiter : pre + " ";//即使不存在该列也要填充空白，避免数据和表头错位不对应
        }
      }, '');
      return previous + rowDelimiter + rowCsv;
    }, csv);
  }
  const blob = new Blob([BOM + csv], { type: 'text/csv;charset=utf-8;' });
  return blob;
}
/**
 * 获取格式化方法
 */
function getFormatFunction(csvFormatType) {
  switch (csvFormatType) {
    case csv_format_type.comma:
      return commaFun;
    case csv_format_type.double_quotation:
      return doubleQuotationFun;
    case csv_format_type.equal_add_double_quotation:
      return equalAddDoubleQuotationFun;
    default:
      return commaFun;
  }
}
/**
 * 处理,分割,数据原样返回
 */
function commaFun(val) {
  return val;
}
/**
 * 处理""
 */
function doubleQuotationFun(val) {
  return val.replace(/"/g, "\"\"");
}
/**
 * 处理=""
 */
function equalAddDoubleQuotationFun(val) {
  return "=\"" + val.replace(/"/g, "\"\"") + "\"";
}
/**
 * 根据数组导出
 * @param {Array} arr 双重数组 [[],[]]
 */
export function exportByArray(arr, filename = "tmp.csv") {
  if (!filename.endsWith('.csv')) {
    filename = filename + '.csv';
  }
  const BOM = '\uFEFF';
  const columnDelimiter = ','; //默认列分隔符','
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
        let value = (typeof item === 'object') ? JSON.stringify(item) : item;
        //如果含有"或逗号就添加双引号
        if (!(value.indexOf("\"") < 0) || !(value.indexOf(",") < 0) || !(value.indexOf("\n") < 0)) {
          value += "\"" + value + "\"";
        }
        lineStr += value + columnDelimiter;
      }
      str += lineStr + rowDelimiter;
      continue;
    }
    let value = (typeof line === 'object') ? JSON.stringify(line) : str;
    //如果含有"或逗号就添加双引号
    if (!(value.indexOf("\"") < 0) || !(value.indexOf(",") < 0)) {
      value += "\"" + value + "\"";
    }
    str += value + rowDelimiter;
  }

  const blob = new Blob([BOM + str], { type: 'text/csv;charset=utf-8;' });
  downloadBlob(blob, filename);
}