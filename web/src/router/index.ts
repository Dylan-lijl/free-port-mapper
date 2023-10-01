import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import DefaultLayout from '@/layouts/default/DefaultLayout.vue';
import NProgress from "nprogress"
import 'nprogress/nprogress.css' //这个样式必须引入;
import store from '@/store';
import { toTab } from '@/util/menuTabConverter';

/**
 * 进度条
 */
interface Progress {
  delay?: number
}
/**
 * 路由
 */
const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Layout',
    component: DefaultLayout,
    redirect: 'index',
    meta: {
      keepAlive: true,
    },
    children: [
      {
        path: "index",
        name: 'Index',
        component: () => import("@/views/IndexPage.vue"),
        meta: {
          keepAlive: true,
        }
      },
      {
        path: "actionLog",
        name: 'ActionLog',
        component: () => import("@/views/ActionLogPage.vue"),
        meta: {
          keepAlive: true,
        }
      },
      {
        path: "customInfo",
        name: 'CustomInfo',
        component: () => import("@/views/CustomInfoPage.vue"),
        meta: {
          keepAlive: true,
        }
      },
      {
        path: "errorInfo",
        name: 'ErrorInfo',
        component: () => import("@/views/ErrorInfoPage.vue"),
        meta: {
          keepAlive: true,
        }
      },
      {
        path: "portMapping",
        name: 'PortMapping',
        component: () => import("@/views/PortMappingPage.vue"),
        meta: {
          keepAlive: true,
        }
      },
      {
        path: "trafficInfo",
        name: 'TrafficInfo',
        component: () => import("@/views/TrafficInfoPage.vue"),
        meta: {
          keepAlive: true,
        }
      }
    ]
  },
  {
    path: '/test',
    name: 'Test',
    component: () => import('../views/Test.vue')
  }
]
/**
 * 使用api创建路由
 */
const router = createRouter({
  /**
   * 指定为路由方式
   */
  history: createWebHistory(process.env.BASE_URL),
  routes,
});
/**
 * 路由前置守卫
 */
router.beforeEach((to, from, next) => {
  // 如果路由需要进度条则跳转的稍后显示进度条
  if (to.meta.progress !== undefined) {
    const progress = to.meta.progress as Progress;
    NProgress.start();
    //如果配置延时跳转则设置定时器
    if (progress && progress.delay) {
      setTimeout(() => {
        next();
      }, progress.delay);
    } else {
      next();
    }
  } else {
    next();
  }
});
/**
 * 路由后置守卫
 */
router.afterEach((to) => {
  //不存在的时候添加进去
  if (!store.getters['system/hasByOpenTabsKey'](to.name)) {
    const menu = store.getters["menu/findMenuByKey"](to.name);
    if (menu) {
      store.commit('system/addOpenTabs', { value: toTab(menu), first: false, activate: true });
    }
  }
  //进度条完成
  NProgress.done();
});
export function currentRoute() {
  return router.currentRoute;
}
export default router
