import message from '@/components/ui/Message';
import { downloadUrl } from "@/util/DownloadFile";
import { getServerFilePath } from '@/util/UrlUtil';
/**
 * 处理200状态
 */
const state_200 = {
  state: 200,
  answer(code) { return code >= this.state && code < 300 },
  method: res => {
    //导出文件逻辑
    if (res.code === 204 && res.data._uri && res.data._uri !== '') {
      //下载文件
      downloadUrl(getServerFilePath(res.data._uri), res.data._name);
    }
    return res;
  },
  sort: 0,
}
/**
 * 处理401状态
 */
const state_401 = {
  state: 401,
  sort: 1,
  answer(code) { return this.state === code },
  method: res => {
    message.error({ content: "请先登录,在继续访问!", duration: 3 });
    return Promise.reject(res);
  }
}
/**
 * 处理403状态
 */
const state_403 = {
  state: 403,
  sort: 2,
  answer(code) { return this.state === code },
  method: res => {
    message.warning({ content: res.message, duration: 3 });
    return Promise.reject(res);
  }
}
/**
 * 处理默认状态
 */
const state_default = {
  state: null,
  sort: 3,
  answer: () => true,
  method: res => {
    console.log("请求失败:" + res.message);
    return Promise.reject(res)
  }
}

export default [
  state_200, state_401, state_403, state_default
]