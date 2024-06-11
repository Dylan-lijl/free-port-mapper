<template>
  <div class="add-info">
    <a-spin :spinning="requesting.add">
      <!-- 组件 -->
      <PortMapping
        @submit="submitItem"
        :initialValue="initialValue"
        ref="comInfo"
      ></PortMapping>
    </a-spin>
  </div>
</template>
<script lang="ts">
/**
 * 新增组件
 */
import PortMapping from "./PortMapping.vue";
import { portMappingAdd as addRequest } from "@/api/portMappingApi";
import { ref, defineComponent, computed } from "vue";
import { addMessageTip } from "@/util/TipUtil";
import { CommonResult, CreateResponse } from "@/types/apiEntity/ApiResponse";
import router from "@/router";
import message from "@/components/ui/Message";

export default defineComponent({
  props: {
    hint: { type: Boolean, default: true },
    initialValue: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  setup(props, context) {
    const comInfo = ref();
    const requesting = ref({ add: false });
    const userId = computed(() => router.currentRoute.value.query.userId);
    //提交监听
    const submitItem = (data) => {
      //根据路由获取userId
      if (!userId.value) {
        message.error("出现错误,未获取到必要参数(userId)!");
        return;
      }
      data.userId = parseInt(userId.value + "");
      //无锁操作
      addMessageTip(requesting.value.add, () => {
        //加锁
        requesting.value.add = true;
        //请求新增接口
        addRequest(data)
          .then((res: CommonResult<CreateResponse<number>>) => {
            //成功公共回调
            if (res.successMethod) {
              res.successMethod();
            }
            //重置表单
            if (comInfo.value) {
              comInfo.value.resetForm();
            }
            //发布成功事件
            if (props.hint) {
              context.emit("success", res);
            }
          })
          .catch((res) => {
            //公共catch
            if (props.hint) {
              res.errorMethod();
            }
          })
          .finally(() => {
            //解锁
            requesting.value.add = false;
          });
      });
    };
    return {
      comInfo,
      submitItem,
      requesting,
    };
  },
  components: {
    PortMapping,
  },
  emits: ["success", "error"],
});
</script>
<style lang="less"></style>
