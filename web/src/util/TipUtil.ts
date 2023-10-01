/**
 * 消息,以下方法逻辑都是当传入的boolean值为true的时候就提示内容然后返回,如果是false则执行回调方法,
 * 如果自己喜欢Promise方式,可以更改此地方
 */
import message from '@/components/ui/Message';
export function messageTip(has = false, type = "info", msg = "当前请求正在进行中,请等待...", callback: () => void) {
  if (has) {
    message[type](msg);
    return;
  }
  if (callback) {
    callback();
  }
}
export function listMessageTip(has = false, callback: () => void, msg = "当前列表请求正在进行中,请等待...") {
  if (has) {
    message.warning(msg);
    return;
  }
  if (callback) {
    callback();
  }
}
export function infoMessageTip(has = false, callback: () => void, msg = "当前详情请求正在进行中,请等待...") {
  if (has) {
    message.warning(msg);
    return;
  }
  if (callback) {
    callback();
  }
}
export function deleteMessageTip(has = false, callback: () => void, msg = "当前删除请求正在进行中,请等待...") {
  if (has) {
    message.warning(msg);
    return;
  }
  if (callback) {
    callback();
  }
}
export function addMessageTip(has = false, callback: () => void, msg = "当前新增请求正在进行中,请等待...") {
  if (has) {
    message.warning(msg);
    return;
  }
  if (callback) {
    callback();
  }
}
export function updateMessageTip(has = false, callback: () => void, msg = "当前新增请求正在进行中,请等待...") {
  if (has) {
    message.warning(msg);
    return;
  }
  if (callback) {
    callback();
  }
}

export function updateStateMessageTip(has = false, callback: () => void, msg = "当前更新状态请求正在进行中,请等待...") {
  if (has) {
    message.warning(msg);
    return;
  }
  if (callback) {
    callback();
  }
}