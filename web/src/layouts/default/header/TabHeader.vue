<template>
  <div>
    <!-- 标题栏 -->
    <a-tabs
      :activeKey="tabActiveKey"
      type="editable-card"
      @edit="onEdit"
      size="small"
      :hideAdd="true"
      @tabClick="onTabClick"
      :tabBarStyle="{ margin: 0 }"
    >
      <a-tab-pane
        v-for="pane in openTabs"
        :key="pane.key"
        :closable="pane.closable"
      >
      <template #tab>
        <span>{{ pane.title }}</span>
      </template>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
/**
 * 标签页
 */
import { defineComponent, computed } from "vue";
import store from "@/store";

export default defineComponent({
  name: "TabHeader",
  setup() {
    //编辑回调(只监听删除逻辑)
    const onEdit = (event, action) => {
      console.log(event, action);
      if (action != "remove") {
        return;
      }
      //切换到前一个
      store.commit("system/closeTab", { key: event, replace: true });
      store.dispatch("system/submitActiveKey");
    };
    //点击逻辑
    const onTabClick = (key) => {
      store.commit("system/setTabActiveKey", key);
      store.dispatch("system/submitActiveKey");
    };
    return {
      tabActiveKey: computed(() => store.getters["system/tabActiveKey"]),
      openTabs: computed(() => store.getters["system/openTabs"]),
      onEdit,
      onTabClick,
    };
  },
});
</script>

<style></style>
