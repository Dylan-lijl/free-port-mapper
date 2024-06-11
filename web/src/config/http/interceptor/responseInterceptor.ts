import message from '@/components/ui/Message';
import stateMachine from '../stateMachine';
import { CommonResult } from '@/types/apiEntity/ApiResponse';
//排序
stateMachine.sort((item1, item2) => item1.sort - item2.sort);
/**
 * 预处理
 * @param {Object} response 返回
 */
export function preproccess(response) {
  //根据状态进行对应的逻辑处理
  const res = response.data;
  //注册res公共方法
  registerMethod(res, res.message);
  //状态结果处理器
  for (const stateItem of stateMachine) {
    //判断是否能够处理
    if (stateItem.answer(res.code)) {
      //调用方法进行处理
      return stateItem.method(res)
    }
  }
}
/**
 * 请求失败处理
 * @param {Object} error 错误
 */
export function error(error) {
  console.log('err' + error)
  const res = { error }
  registerMethod(res, error.message)
  return Promise.reject(res);
}
/**
 * 为返回的对象添加公共方法注册功能
 * 为什么需要这样做？
 * 因为 Axios 返回的是一个 Promise 对象，无法轻松获取最终的执行结果以便决定是否调用公共方法。
 * 如果我们将 Promise 改为回调方式，那么我们可以根据返回值来确定是否执行公共方法。
 * 但在 Promise 的情况下，我们只能为响应对象（res）添加一些公共方法，供接口调用者自行选择是否执行它们。
 * 回调方式可以根据返回值是否等于 false 来决定是否执行，从而解放开发者，使其无需关心回调方法，
 * 只需返回 false、不返回任何值或返回 true 来决定是否执行公共方法。
 * 
 * 如果有更好的方式请提issue
 * @param res 结果
 * @param msg 消息
 */
function registerMethod(res: CommonResult<unknown>, msg: string) {
  if (res) {
    //注册一些方法
    res.successMethod = () => {
      if (res.skip) {
        return
      }
      message.success(msg);
    }
    res.errorMethod = () => {
      if (res.skip) {
        return
      }
      message.error(msg);
    }
    res.warningMethod = () => {
      if (res.skip) {
        return
      }
      message.warning(msg);
    }
    res.catch = () => {
      if (res.skip) {
        return
      }
      if (res.errorMethod) {
        res.errorMethod();
      }
    }
  }
}