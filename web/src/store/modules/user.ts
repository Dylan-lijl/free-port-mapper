// 用户仓库
const state = {
  //登录后的token
  token: null,
};

const mutations = {

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
