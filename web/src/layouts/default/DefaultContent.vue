<template>
  <a-layout-content>
    <div class="layout-content-router">
      <!-- 路由  如果 meta 中的 keepAlive 为 false，则不使用 keep-alive -->
      <router-view v-slot="{ Component }">
        <transition>
          <keep-alive :exclude="routeManage.exclude" ref="keepAlive">
            <component :is="Component" />
          </keep-alive>
        </transition>
      </router-view>
    </div>
  </a-layout-content>
</template>

<script lang="ts">
import { defineComponent, computed, ref } from "vue";
import router from "@/router";
import store from "@/store";
export default defineComponent({
  name: "DefaultContent",
  setup() {
    const keepAlive = ref();
    const openKeys = computed(() => store.getters["system/openTabs"]);
    const routeManage = computed(() => {
      const exclude = new Set<string>();
      const f = (routers, keys) => {
        for (const route of routers) {
          const name = route.name;
          if (
            route &&
            route.meta &&
            route.meta.keepAlive &&
            keys &&
            keys.findIndex((item) => item.key === route.name) !== -1
          ) {
            //
          } else {
            exclude.add(name);
          }
          if (route.children) {
            f(route.children, keys);
          }
        }
      };
      f(router.getRoutes(), openKeys.value);
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
