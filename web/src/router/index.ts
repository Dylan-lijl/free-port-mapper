import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import DefaultLayout from '@/layouts/default/DefaultLayout.vue';
import NProgress from "nprogress"
import 'nprogress/nprogress.css' //这个样式必须引入;
import store from '@/store';
import { toTab } from '@/util/menuTabConverter';
//路由组件对应的全路径
const map = new Map<string, string>();
//路由绑定的标志位
const flag = new Map<string, boolean>();
/**
 * 保存路径和是否跟之前的路径有变化
 * @param route 组件
 */
export function record(route) {
  //从map根据route.key获取对应的值
  const fullpath = map.get(route.name);
  //有记录过并且跟路由的路径不同才标记成true
  if (fullpath && fullpath !== route.fullPath) {
    flag.set(route.name, true);
  } else {
    flag.set(route.name, false);
  }
  //保存全路径
  map.set(route.name, route.fullPath);
}
/**
 * 如果是不同路径则执行回调
 * @param f 方法
 */
export function invoke(f: () => void) {
  const currentRoute = router.currentRoute.value;
  if (currentRoute && currentRoute.name) {
    const name = currentRoute.name.toString();
    const value = flag.get(name);
    if (value && f) {
      f();
    }
    flag.set(name, false);
  }
}
/**
 * 根据路由名称进行跳转
 * @param config 跳转配置
 * @param hint 是否强制
 */
export function gotoRouter(config, hint = false) {
  if (config && config.name) {
    const fullPath = map.get(config.name);
    if (fullPath && !hint) {
      const url = new URL(fullPath, window.location.origin);
      const currentQuery = Object.fromEntries(url.searchParams);
      router.push({ path: url.pathname, query: currentQuery });
    } else {
      router.push(config);
    }
  }
}
/**
 * 根据路由名称清除对应的绑定信息
 * @param name 路由名称
 */
export function remove(name) {
  if (name) {
    map.delete(name);
    flag.delete(name);
  }
}
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
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginPage.vue')
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
  record(to);
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
