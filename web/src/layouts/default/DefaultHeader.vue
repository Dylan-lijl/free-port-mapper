<template>
  <a-layout-header style="background-color: #ffffff; padding-inline:5px">
    <div>
      <!-- 标题栏 -->
      <TitleHeader @changeCollapsed="handleChangeCollapsed" />
      <!-- 标签栏 -->
      <TabHeader />
    </div>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import TabHeader from "./header/TabHeader.vue";
import TitleHeader from "./header/TitleHeader.vue";
export default defineComponent({
  name: "DefaultHeader",
  components: {
    TabHeader,
    TitleHeader,
  },
  emits: ["changeCollapsed"],
  setup(_, context) {
    //继续传递
    const handleChangeCollapsed = (collapsed) => {
      context.emit("changeCollapsed", collapsed);
    };
    return { handleChangeCollapsed };
  },
});
</script>

<style>

</style>
