import axios from 'axios';
import { prepare as requestPrepare, error as requestError } from "./interceptor/requestInterceptor";
import { preproccess, error as responseError } from "./interceptor/responseInterceptor";
//公共配置
const publicConfig = {
  timeout: 15000, // 请求超时时间,15秒
  withCredentials: true   // 设置请求携带cookie  保证session有效性
}
//导出对应实例
export default (config) => {
  //合并公共参数和传入的参数并创建实例
  const worker = axios.create(Object.assign(publicConfig, config));
  // request拦截器
  worker.interceptors.request.use(requestPrepare, requestError);
  // respone拦截器
  worker.interceptors.response.use(preproccess, responseError);
  //调用接口
  return worker(config);
};
