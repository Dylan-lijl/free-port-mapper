// 用户仓库
const state = {
  //登录后的token
  token: null,
};

const mutations = {
  saveToken(state, t) {
    //保持到当前仓库同时保存到本地仓库
    state.token = t;
    localStorage.setItem('token', t);
  },
  clearToken(state) {
    //清除
    state.token = null;
    localStorage.removeItem('token');
  },
  restore(state) {
    const t = localStorage.getItem('token');
    if (t) {
      state.token = t;
    }
  }
};

const actions = {
};

const getters = {
  token: (state) => state.token,
};

export default {
  namespaced: true, // 命名空间，防止模块内部的命名冲突
  state,
  mutations,
  actions,
  getters,
};
