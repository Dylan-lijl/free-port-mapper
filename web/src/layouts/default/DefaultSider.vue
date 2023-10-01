<template>
  <a-layout-sider
    class="layout-sider-style"
    :style="{
      overflow: 'auto',
      height: '100vh',
    }"
    :trigger="null"
    :collapsed="menuCollapsed"
    collapsible
  >
    <div class="logo-line">
      <!-- logo -->
      <img src="/favicon.svg" width="49" height="49" />
      <!-- 项目名 -->
      <span style="font-size: 23px; color: #fff" v-show="!menuCollapsed"
        >端口映射器</span
      >
    </div>
    <!-- 使用a-menu-item渲染 -->
    <a-menu
      theme="dark"
      v-model:openKeys="openKeys"
      :selectedKeys="selectedKeys"
      @click="handleClick"
      mode="inline"
    >
      <template v-for="(menuItem, index) in menus" :key="index">
        <!-- 没有子级菜单 -->
        <template v-if="!(menuItem.children && menuItem.children.length > 0)">
          <a-menu-item :key="menuItem.key" :title="menuItem.label">
            <template #icon v-if="!!menuItem.icon">
              <component :is="menuItem.icon"></component>
            </template>
            <span>{{ menuItem.label }}</span>
          </a-menu-item>
        </template>
        <!-- 含有子级菜单 -->
        <template v-else>
          <a-sub-menu :key="menuItem.key" :title="menuItem.label">
            <template
              v-if="!(menuItem.children && menuItem.children.length > 0)"
            >
              <a-menu-item
                v-for="childMenuItem of menuItem.children"
                :key="childMenuItem.key"
                :title="childMenuItem.label"
              >
                <template #icon v-if="!!childMenuItem.icon">
                  <component :is="childMenuItem.icon"></component>
                </template>
                <span>{{ childMenuItem.label }}</span>
              </a-menu-item>
            </template>
          </a-sub-menu>
        </template>
      </template>
    </a-menu>
  </a-layout-sider>
</template>

<script lang="ts">
/**
 * 左边侧边栏
 */
import { defineComponent, computed } from "vue";
import type { MenuProps } from "ant-design-vue";
import store from "@/store";
import router from "@/router";
import { toTab } from "@/util/menuTabConverter";

export default defineComponent({
  name: "DefaultSider",
  setup() {
    //菜单打开的key
    const openKeys = computed(() => store.getters["system/openTabs"]);
    //计算属性(仓库):菜单选择的key
    const selectedKeys = computed(() => {
      return [store.getters["system/tabActiveKey"]];
    });
    //菜单项被点击
    const handleClick: MenuProps["onClick"] = (e) => {
      //根据key查找对应的菜单
      const findMenu = store.getters["menu/findMenuByKey"](e.key);
      if (findMenu) {
        //将菜单添加到标签页中
        store.commit("system/addOpenTabs", {
          value: toTab(findMenu),
          activate: true,
        });
        //跳转路由
        router.push({ name: findMenu.key });
      }
    };
    //计算属性(菜单)
    const menus = computed(() => {
      return store.getters["menu/menus"];
    });
    //遍历格式化
    return {
      openKeys,
      selectedKeys,
      menus,
      handleClick,
      menuCollapsed: computed(() => store.getters["system/menuCollapsed"]),
    };
  },
});
</script>

<style lang="less">
.logo-line {
  > * {
    vertical-align: middle;
  }
  margin-bottom: 15px;
}
</style>
