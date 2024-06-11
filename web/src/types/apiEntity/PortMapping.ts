export interface ListPortMappingResponse {
  id: number | null,
  name: string | null,
  protocol: number | null,
  serverHost: string | null,
  serverPort: number | null,
  clientHost: string | null,
  clientPort: number | null,
  userId: number | null,
  clientProxy: string | null,
  state: number | null,
}

export interface InfoPortMappingResponse {
  id: number | null,
  name: string | null,
  protocol: number | null,
  serverHost: string | null,
  serverPort: number | null,
  clientHost: string | null,
  clientPort: number | null,
  userId: number | null,
  clientProxy: string | null,
  state: number | null,
}