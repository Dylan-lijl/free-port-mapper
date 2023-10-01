<template>
  <span>
    <!-- 点击弹窗帮助信息 -->
    <a-button
      type="link"
      style="padding: 0; color: #e6a23c"
      @click="
        modal.info({
          title: `&quot;${title}&quot;字段说明`,
          content: help,
        })
      "
      ><question-circle-outlined
    /></a-button>
  </span>
</template>

<script lang="ts">
import { QuestionCircleOutlined } from "@ant-design/icons-vue";
import modal from "@/components/ui/Modal";
import { defineComponent } from "vue";
export default defineComponent({
  props: {
    help: { type: String, default: "" },
    title: { type: String, default: "" },
  },
  components: {
    QuestionCircleOutlined,
  },
  setup() {
    return {
      modal,
    };
  },
});
</script>

<style></style>
