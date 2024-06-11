<template>
  <div>
    <div class="header-line">
      <div class="header-left">
        <a-button type="primary" @click="toggleCollapsed" size="small">
          <MenuUnfoldOutlined v-if="menuCollapsed" />
          <MenuFoldOutlined v-else />
        </a-button>
        <span class="welcome-title-line">欢迎使用端口映射器</span>
      </div>
      <div class="header-right">
        <div>
          <a-button type="link" @click="updatePassword">修改密码</a-button>
          <a-button type="link" @click="logout" :loading="modalVisiable.logout"
            >退出</a-button
          >
        </div>
      </div>
    </div>
    <InputModal
      @success="success"
      name="新密码"
      title="修改密码"
      ref="updatePasswordRef"
      :maxlength="32"
      name-width="65px"
    >
    </InputModal>
  </div>
</template>

<script lang="ts">
/**
 * 标题头
 */
import { defineComponent, computed, ref } from "vue";
import { MenuUnfoldOutlined, MenuFoldOutlined } from "@ant-design/icons-vue";
import store from "@/store";
import {
  userLogout as logoutRequest,
  userUpdatePassword as updatePasswordRequest,
} from "@/api/userApi";
import modal from "@/components/ui/Modal";
import { infoMessageTip } from "@/util/TipUtil";
import router from "@/router";
import InputModal from "@/components/InputModal.vue";
import { CommonResult } from "@/types/apiEntity/ApiResponse";
import UpdatePasswordResponse from "@/types/apiEntity/UpdatePasswordResponse";

export default defineComponent({
  name: "TitleHeader",
  emits: ["changeCollapsed"],
  components: {
    MenuUnfoldOutlined,
    MenuFoldOutlined,
    InputModal,
  },
  setup() {
    //计算属性(菜单是否收起)
    const menuCollapsed = computed(() => store.getters["system/menuCollapsed"]);
    const updatePasswordRef = ref();
    //菜单掌开或隐藏回调
    const toggleCollapsed = () => {
      store.commit("system/setMenuCollapsed", !menuCollapsed.value);
    };
    const modalVisiable = {
      logout: false,
      updatePassword: false,
    };
    //登出
    const logout = () => {
      //弹窗提示
      modal.confirm({
        title: "登出后将需要重新登录,确认要登出吗?",
        onOk: () => {
          infoMessageTip(
            modalVisiable.logout,
            () => {
              modalVisiable.logout = true;
              logoutRequest({}).finally(() => {
                //清除仓库的token
                store.commit("user/clearToken");
                //然后跳转路由到登录界面
                router.push({ name: "Login" });
                modalVisiable.logout = false;
              });
            },
            "正在登出,请等待..."
          );
        },
      });
    };
    const updatePassword = () => {
      //显示修改密码的组件
      updatePasswordRef.value.show();
    };
    //回调然后发送修改密码请求
    const success = (value) => {
      infoMessageTip(
        modalVisiable.updatePassword,
        () => {
          modalVisiable.updatePassword = true;
          updatePasswordRequest({ password: value })
            .then((res: CommonResult<UpdatePasswordResponse>) => {
              if (res.successMethod) {
                res.successMethod();
              }
              //保存新的token,而不需要重新登录
              if(res.data&&res.data.token){
                store.commit("user/saveToken",res.data.token);
              }
            })
            .catch((res: CommonResult<void>) => {
              if (res.catch) {
                res.catch();
              }
            })
            .finally(() => {
              modalVisiable.updatePassword = false;
            });
        },
        "正在请求修改密码,请等待..."
      );
    };
    return {
      toggleCollapsed,
      menuCollapsed,
      logout,
      updatePassword,
      modalVisiable,
      updatePasswordRef,
      success,
    };
  },
});
</script>

<style lang="less">
.header-line {
  display: flex;
  justify-content: space-between;
  .welcome-title-line {
    display: inline-block;
    margin-left: 15px;
    font-size: large;
  }
}
</style>
