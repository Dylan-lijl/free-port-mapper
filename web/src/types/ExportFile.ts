import { Column } from "./Column"
import { Convertor } from "./Convertor";
export interface ExportConfig {
  /**
   * 表头关系以及值转换
   */
  headers: ExportLine[],
  /**
   * 数据
   */
  rows: Column[],
  /**
   * 文件名称
   */
  filename?: string,

}

export interface ExportLine {
  /**
   * 列字段名
   */
  name: string,
  sort: number | undefined | null,
  /**
   * 标题
   */
  title?: string,
  /**
   * 格式化
   * @param value 字段值
   * @returns 格式化后的字段值
   */
  formatter?: (value: unknown) => unknown,
  /**
   * 转换器
   */
  convertor?: Convertor | null,
}