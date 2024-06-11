import message from '@/components/ui/Message';
import store from '@/store';

const tokenHeader = "Authorization";
const tokenPrefix = "Bearer ";

/**
 * 准备阶段拦截
 * @param {Object} config 配置
 * @returns 
 */
export function prepare(config) {
  //从仓库获取token
  const token = store.getters["user/token"];
  if (token) {
    //设置token
    config.headers[tokenHeader] = tokenPrefix + token;
  }
  //如果是json传输格式,需要添加一个默认属性(否则会变成流传输格式而不是json传输格式)
  if (config.data && !(config.data instanceof FormData)) {
    const data = { $placeholder: '' }
    Object.assign(data, config.data);
    config.data = data;
  }
  return config;
}
/**
 * 发送请求错误拦截
 * @param {Object} error 错误
 */
export function error(error) {
  console.log(error)
  message.error({ content: "请求错误:" + error, duration: 3 });
}