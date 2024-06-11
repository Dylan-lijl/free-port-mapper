<template>
  <div class="update-info">
    <a-spin :spinning="requesting.update || requesting.info">
      <!-- 组件 -->
      <PortMapping
        @submit="submitItem"
        :initialValue="initialValue"
        :exist="exist"
        ref="comInfo"
      ></PortMapping>
    </a-spin>
  </div>
</template>
<script lang="ts">
import PortMapping from "./PortMapping.vue";
import { ref, defineComponent, onMounted, watch } from "vue";
import { idNotExist } from "@/util/HttpStatic";
import message from "@/components/ui/Message";
import {
  portMappingUpdate as updateRequest,
  portMappingInfo as infoRequest,
} from "@/api/portMappingApi";
import { infoMessageTip, updateMessageTip } from "@/util/TipUtil";
import { InfoPortMappingResponse } from "@/types/apiEntity/PortMapping";
import { CommonResult, UpdateResponse } from "@/types/apiEntity/ApiResponse";

export default defineComponent({
  props: {
    hint: { type: Boolean, default: true },
    existId: { type: Number, required: true },
    initialValue: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  setup(props, context) {
    //id对应的数据
    const exist = ref<InfoPortMappingResponse | null>(null);
    //dom
    const comInfo = ref();
    //请求控制
    const requesting = ref({ update: false, info: false });
    //初始化id对应的数据
    const initExist = () => {
      //无锁操作
      infoMessageTip(requesting.value.info, () => {
        //加锁
        requesting.value.info = true;
        infoRequest({ id: props.existId })
          .then((res: CommonResult<InfoPortMappingResponse>) => {
            if (res.data) {
              exist.value = res.data;
            }
            if (props.hint) {
              if (res.successMethod) {
                res.successMethod();
              }
            }
          })
          .catch((res: CommonResult<unknown>) => {
            if (props.hint) {
              if (res.catch) {
                res.catch();
              }
            }
          })
          .finally(() => {
            //解锁
            requesting.value.info = false;
          });
      });
    };
    //提交表单
    const submitItem = (data) => {
      //判断是否是不存在的值
      if (data.id === idNotExist) {
        if (props.hint) {
          message.error({ content: "传入的对象不能不存在" });
        }
        context.emit("error");
      }
      //无锁操作
      updateMessageTip(requesting.value.update, () => {
        //加锁
        requesting.value.update = true;
        //更新请求
        updateRequest(data)
          .then((res: CommonResult<UpdateResponse>) => {
            //公共提示成功请求
            if (props.hint) {
              if (res.successMethod) {
                res.successMethod();
              }
            }
            //重置表单
            if (comInfo.value) {
              comInfo.value.resetForm();
            }
            //初始化已存在
            initExist();
            //发布成功事件
            context.emit("success", res);
          })
          .catch((res: CommonResult<unknown>) => {
            if (props.hint) {
              if (res.catch) {
                res.catch();
              }
            }
          })
          .finally(() => {
            requesting.value.update = false;
          });
      });
    };
    onMounted(() => {
      //校验
      if (!props.existId) {
        if (props.hint) {
          message.error("传入的id不能为空");
        }
        context.emit("error");
      }
      initExist();
    });
    watch(
      () => props.existId,
      () => {
        initExist();
      }
    );
    return {
      comInfo,
      exist,
      initExist,
      submitItem,
      requesting,
    };
  },
  emits: ["success", "error"],
  components: {
    PortMapping,
  },
});
</script>
<style lang="less"></style>
