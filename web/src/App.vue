<template>
  <div class="main-app">
    <ApplicationManger />
  </div>
</template>
<script lang="ts" setup>
//导入主界面
import ApplicationManger from "./components/ApplicationManger/index.vue";
//需要忽略的错误信息提示(防止在浏览器弹窗问题: https://github.com/vueComponent/ant-design-vue/issues/6604)
const ignoreErrors = [
  "ResizeObserver loop completed with undelivered notifications",
  "ResizeObserver loop limit exceeded",
];
//添加全局监听错误
window.addEventListener("error", (e) => {
  let errorMsg = e.message;
  ignoreErrors.forEach((m) => {
    if (errorMsg.startsWith(m)) {
      console.error(errorMsg);
      if (e.error) {
        console.error(e.error.stack);
      }
      const resizeObserverErrDiv = document.getElementById(
        "webpack-dev-server-client-overlay-div"
      );
      const resizeObserverErr = document.getElementById(
        "webpack-dev-server-client-overlay"
      );
      if (resizeObserverErr) {
        resizeObserverErr.setAttribute("style", "display: none");
      }
      if (resizeObserverErrDiv) {
        resizeObserverErrDiv.setAttribute("style", "display: none");
      }
    }
  });
});
</script>
<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
body {
  margin: 0;
}
.container {
  .operation-row {
    padding: 3px;
  }

  .table-header-utils {
    text-align: right;
  }
}
/** 子元素边距 */
@children-margin: 5px;
@children-phone-margin: 1px;
/** 最大屏幕宽度 */
@max-phone-width: 768px;
/** 表格上方的工具栏 */
.table-navigation-bar {
  padding: 10px 0;
  display: flex;
  justify-content: space-between;
  /** 手机界面样式 */
  @media (max-width: @max-phone-width) {
    display: block;
  }
  /** 搜索栏 */
  .table-navigation-bar-left {
    > *:not(:first-child) {
      margin-left: @children-margin;
      @media (max-width: @max-phone-width) {
        margin-left: @children-phone-margin;
      }
    }
  }
  /** 工具栏 */
  .table-navigation-bar-right {
    > *:not(:last-child) {
      margin-right: @children-margin;
      @media (max-width: @max-phone-width) {
        margin-right: @children-phone-margin;
      }
    }
  }
}
</style>
