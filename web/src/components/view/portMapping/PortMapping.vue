<template>
  <div class="port-mapping-info">
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
      <a-form-item label="代理名称" name="name">
        <a-input v-model:value="info.name" showCount :maxlength="255" />
      </a-form-item>
      <a-form-item label="协议" name="protocol">
        <a-select v-model:value="info.protocol">
          <a-select-option :key="0" :value="0">TCP</a-select-option>
          <a-select-option :key="1" :value="1">UDP</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="服务端主机名" name="serverHost">
        <a-input v-model:value="info.serverHost" showCount :maxlength="255" />
      </a-form-item>
      <a-form-item label="服务端端口" name="serverPort">
        <a-input-number v-model:value="info.serverPort" :max="65534" :min="0" />
      </a-form-item>
      <a-form-item label="客户端主机名" name="clientHost">
        <a-input v-model:value="info.clientHost" showCount :maxlength="255" />
      </a-form-item>
      <a-form-item label="客户端端口" name="clientPort">
        <a-input-number v-model:value="info.clientPort" :max="65534" :min="0" />
      </a-form-item>
      <a-form-item label="客户端代理" name="clientProxy">
        <a-input v-model:value="info.clientProxy" showCount :maxlength="255" />
      </a-form-item>
      <a-form-item label="状态" name="state">
        <a-switch
          v-model:checked="info.state"
          :checkedValue="1"
          :unCheckedValue="0"
          checkedChildren="启用"
          unCheckedChildren="禁用"
        ></a-switch>
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
import { InfoPortMappingResponse } from "@/types/apiEntity/PortMapping";
import message from "@/components/ui/Message";
const rules = {
  name: [
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
  protocol: [
    {
      required: true,
      message: "必填",
      trigger: "blur",
    },
  ],
  serverPort: [
    {
      required: true,
      message: "必填",
      trigger: "blur",
    },
  ],
  clientHost: [
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
  clientPort: [
    {
      required: true,
      message: "必填",
      trigger: "blur",
    },
  ],
};

const defaultInfo = () => {
  return {
    id: null,
    name: "",
    protocol: 0,
    serverHost: "0.0.0.0",
    serverPort: null,
    clientHost: "",
    clientPort: null,
    clientProxy: "",
    state: 0,
  } as InfoPortMappingResponse;
};

export default defineComponent({
  name: "CustomInfo",
  components: {},
  props: {
    exist: { type: Object as () => InfoPortMappingResponse | null },
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
        return { style: { width: "120px" } };
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
