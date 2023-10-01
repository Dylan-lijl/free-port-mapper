<template>
  <span class="table-field-controller">
    <a-dropdown
      v-model:open="fieldVisible"
      @click="() => (fieldVisible = !fieldVisible)"
    >
      <a-button type="link" :style="{ color: color }">
        <slot><ControlOutlined />{{ title }}<DownOutlined /></slot>
      </a-button>
      <template #overlay>
        <a-menu>
          <!-- 遍历字段 -->
          <template v-for="column in modelValue" :key="column.key">
            <a-menu-item v-if="column.show" @click="() => updateField(column)">
              <template #icon>
                <!-- 复选框图标 -->
                <span :style="{ color: iconColor, fontSize: iconSize }"
                  ><span v-show="column.visible"><CheckSquareOutlined /></span>
                  <span v-show="!column.visible">
                    <BorderOutlined />
                  </span>
                </span> </template
              ><span :style="{ color: contentColor, fontSize: contentSize }">{{
                column.title
              }}</span>
            </a-menu-item>
          </template>
        </a-menu>
      </template>
    </a-dropdown>
  </span>
</template>
<script lang="ts">
import { defineComponent, ref } from "vue";
import { Column } from "@/types/Column";
import {
  DownOutlined,
  CheckSquareOutlined,
  BorderOutlined,
  ControlOutlined,
} from "@ant-design/icons-vue";

export default defineComponent({
  name: "TableFieldController",
  components: {
    DownOutlined,
    CheckSquareOutlined,
    BorderOutlined,
    ControlOutlined,
  },
  props: {
    //字段
    modelValue: { type: Array as () => Column[], required: true },
    //组件显示的名称
    title: { type: String, default: "字段" },
    iconColor: { type: String, default: "#5FB878" },
    iconSize: { type: String, default: "18px" },
    contentColor: { type: String, default: "#393D49" },
    contentSize: { type: String, default: "15px" },
    color: { type: String, default: "#1890ff" },
  },
  emits: ["update:modelValue"],
  setup(props, context) {
    //字段显示控制
    const fieldVisible = ref(false);
    //更新字段
    const updateField = (column: Column) => {
      column.visible = !column.visible;
      fieldVisible.value = true;
      context.emit("update:modelValue", [...props.modelValue]);
    };
    return {
      fieldVisible,
      updateField,
    };
  },
});
</script>
<style lang="less"></style>
