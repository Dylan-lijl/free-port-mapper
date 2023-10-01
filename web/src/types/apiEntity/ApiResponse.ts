export interface CommonResult<T> {
  code?: number,
  message?: string,
  data?: T,
  successMethod?: () => void;
  errorMethod?: () => void;
  warningMethod?: () => void;
  catch?: () => void;
  error?: unknown
}

export interface CommonPage<T> {
  pageNum: number,
  pageSize: number,
  totalPage: number,
  total: number,
  list: T[]
}

export interface CreateResponse<T> {
  id: T
}

export interface UpdateResponse {
  success: boolean
}

export interface Params {
  pageNum?: number,
  pageSize?: number,
}