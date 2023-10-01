<template>
  <div class="application-manger">
    <!-- UI国际化 -->
    <ConfigProvider :locale="locale">
      <div class="application-manger-content">
        <!-- 路由 -->
        <router-view>
        </router-view>
      </div>
    </ConfigProvider>
  </div>
</template>

<script lang="ts">
/**
 * 程序管理
 * 具体实现请看路由到的主界面(views/IndexPage.vue)
 */
import { defineComponent, ref } from "vue";
import { ConfigProvider } from "ant-design-vue";
import zhCN from 'ant-design-vue/es/locale/zh_CN';

export default defineComponent({
  name: "ApplicationManger",
  components: {
    ConfigProvider,
  },
  setup() {
    //设置国际化为中文
    const locale = ref(zhCN);
    return {
      locale
    };
  },
});
</script>

<style></style>
