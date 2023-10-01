<template>
  <div class="default-layout">
    <a-layout>
      <!-- 侧边栏 -->
      <DefaultSider />
      <a-layout>
        <!-- 标头栏 -->
        <DefaultHeader style="height: auto"></DefaultHeader>
        <!-- 分割线 -->
        <div class="header-liner"></div>
        <!-- 内容栏 -->
        <DefaultContent></DefaultContent>
        <!-- 底部栏 -->
        <!-- <DefaultFooter></DefaultFooter> -->
      </a-layout>
    </a-layout>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, provide } from "vue";
import DefaultSider from "./DefaultSider.vue";
import DefaultHeader from "./DefaultHeader.vue";
import DefaultContent from "./DefaultContent.vue";
// import DefaultFooter from "./DefaultFooter.vue";
/**
 * 默认页面结构
 */
export default defineComponent({
  name: "DefaultLayout",
  components: {
    DefaultSider,
    DefaultHeader,
    DefaultContent,
    // DefaultFooter,
  },
  setup() {
    const menuCollapsed = ref(true);
    provide("menuCollapsed", menuCollapsed);
    return {};
  },
});
</script>

<style lang="less">
.header-liner{
  height: 2px;
  background: linear-gradient(to right, #FF0000, #FF7F00, #FFFF00, #00FF00, #0000FF, #4B0082, #8B00FF);
}
</style>
