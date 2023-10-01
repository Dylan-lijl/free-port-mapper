/* 输入框 */
<template>
  <span class="input-modal">
    <a-modal
      v-model:open="visible"
      :title="title"
      @ok="success"
      @cancel="cancel"
      :cancel-text="cancelText"
      :okText="okText"
    >
      <div class="value-layout">
        <!-- 左边的标题 -->
        <div
          class="value-key-layout"
          :style="{ width: nameWidth }"
          v-if="showName"
        >
          <slot name="name">
            <span v-if="name">{{ name }}</span> </slot
          >：
        </div>
        <slot
          ><a-input
            v-model:value="value"
            :maxlength="maxlength"
            :minlength="minlength"
          ></a-input
        ></slot>
      </div>
    </a-modal>
  </span>
</template>
<script lang="ts">
import { defineComponent, ref,unref } from "vue";
export default defineComponent({
  props: {
    //自动关闭
    autoVisible: { type: Boolean, default: true },
    //左边的名称
    name: { type: String, default: "请输入" },
    nameWidth: { type: String, default: "20px" },
    //是否显示左边的名称
    showName: { type: Boolean, default: true },
    //标题
    title: { type: String, default: "请输入" },
    //确认按钮文本
    okText: { type: String, default: "确认" },
    //取消按钮文本
    cancelText: { type: String, default: "取消" },
    minlength: { type: Number, default: 0 },
    maxlength: { type: Number, default: 255 },
  },
  setup(props,context) {
    //显示隐藏
    const visible = ref(false);
    //值
    const value = ref("");
    //打开弹窗方法
    const show = () => {
      visible.value = true;
    };
    //隐藏方法
    const hidden = () => {
      visible.value = false;
    };
    //回调监听
    const success = () => {
      //先调用方法,然后再关闭
      context.emit("success", unref(value));
      if (props.autoVisible) {
        hidden();
      }
      value.value = "";
    };
    //取消回调
    const cancel = () => {
      //先调用方法,然后再关闭
      context.emit("cancel", unref(value));
      if (props.autoVisible) {
        hidden();
      }
      value.value = "";
    };
    return {
      visible,
      value,
      show,
      hidden,
      success,
      cancel,
    };
  },
});
</script>
<style lang="less" scoped>
.value-layout {
  display: flex;
  align-items: center;
  .value-key-layout {
    font-weight: bold;
  }
}
</style>
