import { ColumnType } from 'ant-design-vue/es/table/interface'
import { Convertor } from './Convertor';

// 定义包含额外属性的新接口
export interface Column extends ColumnType<unknown> {
  /**
   * 字段展示
   */
  show?: boolean;
  /**
   * 字段显示
   */
  visible?: boolean;
  /**
   * 排序字段
   */
  _sort?: number;
  /**
   * 是否跳过
   */
  skip?: boolean;
  /**
   * 值转换器
   */
  convertor?: Convertor;
  /**
   * 帮助提示语
   */
  help?: string;
  /**
   * 格式化
   * @param value 值
   * @param item 列
   * @returns 值
   */
  format?: (value: unknown, item: Column) => unknown;
}