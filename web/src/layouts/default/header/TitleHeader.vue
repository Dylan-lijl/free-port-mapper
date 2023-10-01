<template>
  <div class="header-line">
    <div class="header-left">
      <a-button type="primary" @click="toggleCollapsed" size="small">
        <MenuUnfoldOutlined v-if="menuCollapsed" />
        <MenuFoldOutlined v-else />
      </a-button>
      <span class="welcome-title-line">欢迎使用端口映射器</span>
    </div>
    <div class="header-right">
      <a-button type="link">退出</a-button>
    </div>
  </div>
</template>

<script lang="ts">
/**
 * 标题头
 */
import { defineComponent, computed } from "vue";
import { MenuUnfoldOutlined, MenuFoldOutlined } from "@ant-design/icons-vue";
import store from "@/store";

export default defineComponent({
  name: "TitleHeader",
  emits: ["changeCollapsed"],
  components: {
    MenuUnfoldOutlined,
    MenuFoldOutlined,
  },
  setup() {
    //计算属性(菜单是否收起)
    const menuCollapsed = computed(() => store.getters["system/menuCollapsed"]);
    //菜单掌开或隐藏回调
    const toggleCollapsed = () => {
      store.commit("system/setMenuCollapsed", !menuCollapsed.value);
    };
    return { toggleCollapsed, menuCollapsed };
  },
});
</script>

<style lang="less">
.header-line {
  display: flex;
  justify-content: space-between;
  .welcome-title-line {
    display: inline-block;
    margin-left: 15px;
    font-size: large;
  }
}
</style>
