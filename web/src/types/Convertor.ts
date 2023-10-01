export interface Convertor {
  type: number,
  valueMap?: object | Map<string, unknown>,
  value?: string | unknown,
  values?: string[] | unknown
}