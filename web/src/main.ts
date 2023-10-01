import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store';
import registerUI from '@/config/ui/GlobalComponents';
import init from '@/config/init';

const app = createApp(App);
//注册全局UI
registerUI(app);
//使用路由以及仓库
app.use(store).use(router);
app.mount('#app').$nextTick(() => init());