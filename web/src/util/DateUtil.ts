const week = {
  "0": "/u65e5",
  "1": "/u4e00",
  "2": "/u4e8c",
  "3": "/u4e09",
  "4": "/u56db",
  "5": "/u4e94",
  "6": "/u516d"
};
/**
 * 格式化日期
 * @param {Object} value 这个值可以是时间类型也可以是时间戳类型
 * @param {Object} defautlValue 当value值不存在则返回
 * @param {Object} fmt 无值时就将value转变成date直接返回
 * @returns 格式化值或者默认值也有可能是Date类型
 */
export function format(value: string | number | Date, 
  defautlValue?: string | number | Date | null | undefined,
   fmt = "yyyy-MM-dd HH:mm:ss") {
  if (!value) {
    return defautlValue;
  }
  if (!(value instanceof Date)) {
    value = new Date(value);
  }
  if (!fmt) {
    return value;
  }
  const o = {
    "M+": value.getMonth() + 1,
    "d+": value.getDate(),
    "h+": value.getHours() % 12 == 0 ? 12 : value.getHours() % 12,
    "H+": value.getHours(),
    "m+": value.getMinutes(),
    "s+": value.getSeconds(),
    "q+": Math.floor((value.getMonth() + 3) / 3),
    "S": value.getMilliseconds()
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (value.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  if (/(E+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[value.getDay() + ""]);
  }
  for (const k in o) {
    if (new RegExp("(" + k + ")").test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
  }
  return fmt;
}