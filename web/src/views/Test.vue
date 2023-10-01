<template>
  <div class="">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="index" tab="主页">测式主页面</a-tab-pane>
      <a-tab-pane key="export-file" tab="文件导出">
        <a-dropdown>
          <a class="ant-dropdown-link" @click.prevent>
            Hover me
            <DownOutlined />
          </a>
          <template #overlay>
            <a-menu>
              <a-menu-item>
                <a href="javascript:;">1st menu item</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;">2nd menu item</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;">3rd menu item</a>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
        <TableExport
          :columns="columns"
          :data="data"
          :params="params"
          :service="service"
        />
        <TableFieldController v-model="columns"></TableFieldController>
        <a-table :dataSource="data" :columns="columns"
            rowKey="id"
            bordered
            @change="()=>{}"
            resizable
            @resizeColumn="(w, col) => (col.width = w)" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { DownOutlined } from "@ant-design/icons-vue";
import { TableExport, TableFieldController } from "@/components/ExportFile";
import { Column } from "@/types/Column";

export default defineComponent({
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Test",
  components: {
    DownOutlined,
    TableExport,
    TableFieldController,
  },
  props: {},
  setup() {
    const activeKey = ref("index");
    const data = ref([]);
    const params = ref({});
    const columns: Column[] = [
      {
        title: "主键",
        key: "id",
        dataIndex: "id",
        align: "center",
        visible: false,
        resizable: true,
        show: true,
        width: 50,
        _sort: 10,
      },
      {
        title: "类型",
        key: "type",
        dataIndex: "type",
        align: "center",
        width: 100,
        visible: true,
        resizable: true,
        show: true,
        _sort: 15,
        customRender(line: { text; column }) {
          let convertor = line.column.convertor;
          let val = convertor.valueMap[line.text];
          return val ? val : convertor.valueMap["default"];
        },
        format(text, column: Column) {
          if (column.customRender) {
            return column.customRender({
              text,
              column,
              record: null,
              index: 0,
              renderIndex: 0,
              value: 0,
            });
          } else {
            return "null";
          }
        },
        convertor: {
          type: 1,
          valueMap: {
            1: "前缀",
            2: "词根",
            3: "后缀",
            default: "未知",
          },
        },
      },
      {
        title: "备注",
        key: "remark",
        dataIndex: "remark",
        align: "center",
        width: 300,
        visible: true,
        resizable: true,
        show: true,
        _sort: 90,
      },
      {
        title: "操作",
        align: "center",
        key: "operation",
        fixed: "right",
        visible: true,
        show: false,
        skip: true,
      },
    ];
    return {
      activeKey,
      columns,
      data,
      params,
      service: () => {
        return {};
      },
    };
  },
});
</script>
<style lang="less"></style>
