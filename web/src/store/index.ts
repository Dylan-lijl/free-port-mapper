import { createStore } from 'vuex';
import system from './modules/system';
import menu from './modules/menu';
export interface RootState {
  name?: string;
}
// 创建仓库实例
const store = createStore({
  modules: {
    system,
    menu
  }
});

export default store;
