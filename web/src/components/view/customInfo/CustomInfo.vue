<template>
  <div class="root-word-info">
    <a-form
      :model="info"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
      :rules="rules"
      ref="infoRef"
      labelAlign="right"
    >
      <a-form-item label="主键" style="display: none" name="id">
        <a-input v-model:value="info.id"></a-input>
      </a-form-item>
      <a-form-item label="客户名" name="username">
        <a-input v-model:value="info.username" showCount :maxlength="255" />
      </a-form-item>
      <a-form-item label="密钥" name="secretKey">
        <a-input v-model:value="info.secretKey" showCount :maxlength="32">
          <template #addonAfter>
            <a-tooltip>
              <template #title> 随机生成密钥 </template>
              <a-button type="link" size="small" @click="generateKey"
                ><template #icon><InteractionOutlined /></template>
              </a-button>
            </a-tooltip>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea
          v-model:value="info.remark"
          showCount
          autoSize
          :maxlength="255"
        ></a-textarea>
      </a-form-item>
      <div class="info-form-util-layout">
        <a-button type="default" @click="resetForm">重置</a-button>
        <a-button type="primary" @click="submitForm">保存</a-button>
      </div>
    </a-form>
  </div>
</template>
<script lang="ts">
import { defineComponent, ref, watch, onMounted } from "vue";
import message from "@/components/ui/Message";
import { InteractionOutlined } from "@ant-design/icons-vue";
import { v4 as uuid } from "uuid";
import { InfoCustomInfoResponse } from "@/types/apiEntity/CustomInfo";
const rules = {
  username: [
    {
      required: true,
      message: "必填",
      trigger: "blur",
    },
    {
      max: 255,
      message: "不符合长度要求",
      trigger: "blur",
    },
  ],
  secretKey: [
    {
      required: true,
      message: "必填",
      trigger: "blur",
    },
    {
      max: 255,
      message: "不符合长度要求",
      trigger: "blur",
    },
  ],
  remark: [
    {
      max: 255,
      message: "不符合长度要求",
      trigger: "blur",
    },
  ],
};

const defaultInfo = () => {
  return {
    id: null,
    username: "",
    secretKey: "",
    remark: "",
  } as InfoCustomInfoResponse;
};

export default defineComponent({
  name: "CustomInfo",
  components: {
    InteractionOutlined,
  },
  props: {
    exist: { type: Object as () => InfoCustomInfoResponse | null },
    initialValue: {
      type: Object,
      default: () => {
        return {};
      },
    },
    labelCol: {
      type: Object as () => {
        span: number;
        offset: number;
        style: object | string;
      },
      default: () => {
        return { style: { width: "70px" } };
      },
    },
    wrapperCol: {
      type: Object as () => { span: number; offset: number },
      default: () => {
        return {};
      },
    },
  },
  setup(props, context) {
    //表单内容
    const info = ref(defaultInfo());
    //表单引用
    const infoRef = ref();
    //初始化方法
    const initData = () => {
      //修改时需要将值覆盖表单对象
      if (props.exist) {
        info.value = { ...defaultInfo(), ...props.exist };
      }
      //需要跟路由的参数进行合并
      info.value = Object.assign(info.value, props.initialValue);
    };
    //提交表单监听
    const submitForm = () => {
      if (!infoRef.value) {
        message.error("未找到infoRef的dom对象!");
        return;
      }
      infoRef.value
        .validate()
        .then(() => {
          //发布提交事件
          context.emit("submit", info.value);
        })
        .catch((res) => {
          //表单校验失败,发布错误事件
          context.emit("error", res.message);
        })
        .finally(() => {
          //noting
        });
    };
    //重置表单
    const resetForm = () => {
      initData();
    };
    //生成uuid
    const generateKey = () => {
      info.value.secretKey = uuid().replace(/-/g, "");
    };
    //监听修改的数据
    watch(
      () => props.exist,
      () => {
        initData();
      }
    );
    //加载好回调
    onMounted(() => {
      //初始化方法
      initData();
    });
    return {
      infoRef,
      info,
      rules,
      initData,
      submitForm,
      resetForm,
      generateKey,
    };
  },
  emits: ["submit", "error"],
});
</script>
<style lang="less">
.parent-options {
  font-weight: bold;
}
.info-form-util-layout {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
</style>
