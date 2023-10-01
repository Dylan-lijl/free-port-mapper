export interface ListCustomInfoResponse {
  id: number,
  username?: string,
  secretKey?: string,
  state?: number,
  createTime?: Date,
  updateTime?: Date,
  remark?: string
}

export interface InfoCustomInfoResponse {
  id: number | null,
  username?: string | null,
  secretKey?: string | null,
  state?: number | null,
  createTime?: Date | null,
  updateTime?: Date | null,
  remark?: string | null
}