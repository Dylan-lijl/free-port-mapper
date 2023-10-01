<template>
  <a-layout-content>
    <div class="layout-content-router">
      <!-- 路由  如果 meta 中的 keepAlive 为 false，则不使用 keep-alive -->
      <router-view v-slot="{ Component }">
        <transition>
          <keep-alive :exclude="routeManage.exclude" :max="10" ref="keepAlive">
            <component :is="Component" />
          </keep-alive>
        </transition>
      </router-view>
    </div>
  </a-layout-content>
</template>

<script lang="ts">
import { defineComponent, computed, ref,  } from "vue";
import router from "@/router";
import store from "@/store";
export default defineComponent({
  name: "DefaultContent",
  setup() {
    const keepAlive = ref();
    const openKeys = computed(() => store.getters["system/openTabs"]);
    const routeManage = computed(() => {
      const exclude = new Set<string>();
      const f = (routers) => {
        for (const route of routers) {
          if (
            route &&
            route.meta &&
            route.meta.keepAlive &&
            openKeys.value &&
            openKeys.value.findIndex((item) => item.key === route.name) !== -1
          ) {
            //n
          } else {
            exclude.add(route.name);
          }
          if (route.children) {
            f(route.children);
          }
        }
      };
      f(router.getRoutes());
      return {
        exclude: Array.from(exclude),
      };
    });
    return {
      routeManage,
      openKeys,
      keepAlive,
    };
  },
});
</script>

<style>
.layout-content-router {
  margin: 5px;
}
</style>
