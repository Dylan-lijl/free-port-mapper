<template>
  <span class="table-export">
    <a-dropdown>
      <a-button type="link" :style="{ color: color }"
        ><DownloadOutlined />导出<DownOutlined
      /></a-button>
      <template #overlay>
        <a-menu>
          <!-- 前端导出 -->
          <a-sub-menu key="current" title="当前页数据">
            <a-menu-item key="csv" @click="() => exportFile('1', '1')"
              >CSV</a-menu-item
            >
            <a-menu-item key="excel" @click="() => exportFile('1', '2')"
              >excel</a-menu-item
            >
            <a-menu-item key="txt" @click="() => exportFile('1', '3')"
              >文本</a-menu-item
            >
          </a-sub-menu>
          <!-- 调用后端导出 -->
          <a-sub-menu key="all" title="全部数据">
            <a-menu-item key="csv" @click="() => exportFile('2', '1')"
              >CSV</a-menu-item
            >
            <a-menu-item key="excel" @click="() => exportFile('2', '2')"
              >excel</a-menu-item
            >
            <a-menu-item key="txt" @click="() => exportFile('2', '3')"
              >文本</a-menu-item
            >
          </a-sub-menu>
        </a-menu>
      </template>
    </a-dropdown>
    <!-- 文件名称提示框 -->
    <input-modal
      title="请输入文件名称"
      ref="filenameInput"
      name="文件名称"
      nameWidth="90px"
      @success="exportMethod"
    ></input-modal>
  </span>
</template>
<script lang="ts">
import { DownOutlined, DownloadOutlined } from "@ant-design/icons-vue";
import InputModal from "@/components/InputModal.vue";
import { defineComponent, ref, computed, unref } from "vue";
import { Column } from "@/types/Column";
import {
  csvExport as exportCsv,
  excelExport as exportExcel,
  txtExport as exportTxt,
} from "@/util/ExportFile";
import { ExportConfig, ExportLine } from "@/types/ExportFile";
import message from "@/components/ui/Message";
//常量
const values = {
  current: "1",
  all: "2",
  csv: "1",
  excel: "2",
  txt: "3",
};

export default defineComponent({
  name: "TableExport",
  components: {
    DownOutlined,
    InputModal,
    DownloadOutlined,
  },
  props: {
    //字段
    columns: { type: Array as () => Column[], required: true },
    //数据
    data: { type: Array, required: true },
    //参数
    params: { type: Object, required: true },
    //接口
    service: { type: Function, required: true },
    //颜色
    color: { type: String, default: "#1890ff" },
  },
  setup(props) {
    // 文件名称
    const filename = ref("");
    //导出的类型(前端导出|后端导出)
    const exportType = ref("1");
    //文件类型
    const fileType = ref("1");
    //文件名称dom
    const filenameInput = ref();
    /**
     * 导出当前页
     */
    const exportFile = (etype, ftype) => {
      //打开对话框
      if (filenameInput.value) {
        filenameInput.value.show();
        fileType.value = ftype;
        exportType.value = etype;
      }
    };
    //合并后的表头
    const mergeColumns = computed(() => {
      let arr: Column[] = [];
      for (let item of props.columns) {
        //跳过不显示的和需要跳过的
        if (!item.visible || item.skip) {
          continue;
        }
        arr.push(item);
      }
      // 进行排序
      arr.sort((obj, obj2) => {
        // 如果都有 _sort 值，则升序排序
        if (obj._sort !== undefined && obj2._sort !== undefined) {
          return obj._sort - obj2._sort;
        }
        // 如果只有 obj 有 _sort 值，则 obj 排在前面
        if (obj._sort !== undefined) {
          return -1;
        }
        // 如果只有 obj2 有 _sort 值，则 obj2 排在前面
        if (obj2._sort !== undefined) {
          return 1;
        }
        // 如果都没有 _sort 值，保持自然排序
        return 0;
      });

      return arr;
    });
    //生成导出参数
    const buildExportConfig = computed(() => {
      //先过滤再排序
      let headers = [] as ExportLine[];
      for (const index in mergeColumns.value) {
        const item = unref(mergeColumns.value[index]);
        //封装头对象
        headers.push({
          name: item.key ? item.key.valueOf() + "" : "column" + index,
          sort: item._sort,
          title: item.title ? item.title.valueOf() + "" : "column" + index,
          formatter(value) {
            return item.format ? item.format(value, item) : value;
          },
          convertor: item.convertor ? item.convertor : null,
        } as ExportLine);
      }
      return {
        headers,
        rows: unref(props.data) as Column,
        filename: unref(filename),
      };
    });
    //导出方法
    const exportMethod = (value) => {
      filename.value = value;
      const config = unref(buildExportConfig) as ExportConfig;
      if (exportType.value === values.current) {
        //前端导出
        switch (fileType.value) {
          case values.csv:
            //构建所需参数
            exportCsv(config);
            break;
          case values.excel:
            exportExcel(config);
            break;
          case values.txt:
            exportTxt(config);
            break;
        }
      } else if (exportType.value === values.all) {
        //接口导出,构建请求对象
        let data = Object.assign({}, props.params.value);
        data._export = {
          filename: filename.value,
          fileType: fileType.value,
        };
        if (mergeColumns.value.length <= 0) {
          message.error("导出失败,无标题项!");
          return;
        }
        data._export.titles = config.headers;
        //调用方法 (下载文件逻辑需要在api拦截器以及后端导出插件查看:/config/http/response)
        props.service(data).then((res) => {
          message.info("正在下载文件:" + res.data._name);
        });
      }
    };
    return {
      filename,
      exportType,
      fileType,
      exportFile,
      exportMethod,
      filenameInput,
    };
  },
});
</script>
<style lang="less"></style>
