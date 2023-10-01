import router from "@/router";
// 系统变量

const state = {
  //菜单是否收起
  menuCollapsed: false,
  //已打开的标签列表
  openTabs: [],
  //当前激活的标签
  tabActiveKey: '',
  closedTabs: [],
};

interface CloseTab {
  key: string;
  replace?: boolean;
}

const mutations = {
  //设置菜单是否收起
  setMenuCollapsed(state, menuCollapsed) {
    state.menuCollapsed = menuCollapsed;
  },
  //添加已打开标签
  addOpenTabs(state, req) {
    const value = req.value;
    //根据key获取索引
    const index = state.openTabs.findIndex((item) => item.key === value.key);
    if (index !== -1) {
      // 如果 key 已存在于数组中，替换掉对应的对象
      state.openTabs.splice(index, 1, value);
    } else {
      // 否则直接追加到数组中
      req.first ? state.openTabs.unshift(value) : state.openTabs.push(value);
    }
    //切换激活该标签
    if (req.activate) {
      state.tabActiveKey = req.value.key;
    }
  },
  //移除已打开的标签
  removeOpenTabs(state, key) {
    //查找索引
    const index = state.openTabs.findIndex((item) => item.key === key);
    if (index !== -1) {
      // 找到了要删除的项，使用splice方法删除
      state.openTabs.splice(index, 1);
    }
  },
  //切换激活该标签
  setTabActiveKey(state, key) {
    const index = state.openTabs.findIndex((item) => item.key === key);
    if (index !== -1) {
      state.tabActiveKey = key
    }
  },
  //关闭标签
  closeTab(state, param: CloseTab) {
    const index = state.openTabs.findIndex((item) => item.key === param.key);
    if (index !== -1) {
      const removeTabItem = state.openTabs[index];
      if (param.replace && state.openTabs.length > 0 && removeTabItem.key === param.key) {
        // 获取被关闭标签页的前一个标签页的 key
        const prevTabKey =
          index > 0 ? state.openTabs[index - 1].key : state.openTabs[index].key;
        // 设置 tabActiveKey 为前一个标签页的 key
        state.tabActiveKey = prevTabKey;
      }
      state.openTabs.splice(index, 1);
      //添加到删除的数组中
      if (!state.closedTabs.includes(removeTabItem.key)) {
        state.closedTabs.push(removeTabItem.key);
      }
    }
  },
  //跳转key对应的路由
  gotoActiveKey(state, key) {
    if (key) {
      router.push({ name: key });
    }
  }

};

const actions = {
  //激活标签
  submitActiveKey({ commit, getters, rootGetters }) {
    const menu = rootGetters['menu/findMenuByKey'](getters.tabActiveKey);
    if (menu) {
      commit('gotoActiveKey', menu.key);
    }
  }
};

const getters = {
  menuCollapsed: (state) => state.menuCollapsed,
  openTabs: (state) => state.openTabs,
  tabActiveKey: (state) => state.tabActiveKey,
  hasByOpenTabsKey: state => (k) => {
    state.openTabs.findIndex(item => item.key === k);
  },
  closedTabs: state => state.closedTabs,
};

export default {
  namespaced: true, // 命名空间，防止模块内部的命名冲突
  state,
  mutations,
  actions,
  getters,
};
