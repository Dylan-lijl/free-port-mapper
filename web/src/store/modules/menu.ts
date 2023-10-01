// 菜单
import {
  CustomInfoIcon,
  ErrorInfoIcon,
  TrafficInfoIcon,
  PostMappingIcon,
  ActionLogIcon,
} from "@/components/icons";
import { HomeOutlined } from "@ant-design/icons-vue";
import { DefineComponent,shallowRef } from 'vue';

interface Menu {
  key: string;
  //标签内容
  label?: string;
  //图标
  icon?: DefineComponent;
  //是否可关闭
  closable?: boolean
}

const index = {
  key: "Index",
  label: "主页",
  icon: HomeOutlined,
  closable: false,
};

const state = {
  //菜单
  values: [
    {
      key: "CustomInfo",
      label: "客户信息",
      icon: shallowRef(CustomInfoIcon),
      closable: true
    },
    {
      key: "ErrorInfo",
      label: "错误信息",
      icon: shallowRef(ErrorInfoIcon),
      closable: true
    },
    {
      key: "TrafficInfo",
      label: "流量信息",
      icon: shallowRef(TrafficInfoIcon),
      closable: true
    },
    {
      key: "PortMapping",
      label: "端口映射",
      icon: shallowRef(PostMappingIcon),
      closable: true
    },
    {
      key: "ActionLog",
      label: "操作日志",
      icon: shallowRef(ActionLogIcon),
      closable: true
    },
  ],
  index
};

const mutations = {
  setMenus(state, values) {
    state.values = values;
  },
};

const actions = {
};

const getters = {
  //菜单
  menus: (state) => {
    const menus: Menu[] = [index, ...state.values];
    return menus;
  },
  //主页
  index: (state) => {
    return state.index;
  },
  //根据key查找对应的菜单
  findMenuByKey: (_, getters) => {
    return (key) => getters.menus.find(item => item.key === key);
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
};
