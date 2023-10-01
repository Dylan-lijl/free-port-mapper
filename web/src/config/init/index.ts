import store from "@/store";
import { toTab } from '@/util/menuTabConverter';
//初始化方法
export default function init() {
  const index = store.getters['menu/index'];
  if (index) {
    //将主页添加到Tabs
    store.commit('system/addOpenTabs', { value: toTab(index), first: true, activate: true });
  }
}