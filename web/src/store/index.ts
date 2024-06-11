import { createStore } from 'vuex';
import system from './modules/system';
import menu from './modules/menu';
import user from './modules/user';
export interface RootState {
  name?: string;
}
// 创建仓库实例
const store = createStore({
  modules: {
    system,
    menu,
    user
  }
});

export default store;
