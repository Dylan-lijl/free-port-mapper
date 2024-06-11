<template>
  <div class="login-layout">
    <!-- 头部 -->
    <div class="title-layout">
      <span class="title-layout-content">Free-Port-Mapper</span>
    </div>
    <!-- 中部 -->
    <div class="body-layout">
      <div class="content-left-layout">
        <a-carousel autoplay dotPosition="bottom" :dots="false">
          <div>
            <h3>
              初始化密码可以使用JVM参数-Dmy-setting.password或者命令行参数--my-setting.password以及指定外部配置文件--spring.config.location
            </h3>
          </div>
          <div>
            <h3>修改的密码可以在命令行启动的classpath底下查找.password文件</h3>
          </div>
        </a-carousel>
      </div>
      <div class="content-right-layout">
        <div class="form-layout">
          <a-input-password
            v-model:value="password"
            placeholder="请输入密码"
            @pressEnter="login"
          />
          <a-button type="primary" @click="login" :loading="modalVisiable.login"
            >登录</a-button
          >
        </div>
      </div>
    </div>
    <!-- 尾部 -->
    <div></div>
  </div>
</template>

<script lang="ts">
/**
 * 登录界面
 */
import { defineComponent, ref } from "vue";
import message from "@/components/ui/Message";
import { userLogin as loginRequest } from "@/api/userApi";
import store from "@/store";
import { CommonResult } from "@/types/apiEntity/ApiResponse";
import LoginResponse from "@/types/apiEntity/LoginResponse";
import router from "@/router";
import { infoMessageTip } from "@/util/TipUtil";

export default defineComponent({
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  setup() {
    const password = ref("");
    const modalVisiable = { login: false };
    const login = () => {
      const v = password.value;
      if (!v || v === "") {
        message.error("请输入密码!");
        return;
      }
      infoMessageTip(
        modalVisiable.login,
        () => {
          modalVisiable.login = true;
          //登录
          loginRequest({
            password: v,
          })
            .then((res: CommonResult<LoginResponse>) => {
              if (res.data && res.data.token) {
                const token = res.data.token;
                if (res.successMethod) {
                  res.successMethod();
                }
                if (token) {
                  //保存token
                  store.commit("user/saveToken", token);
                  //跳转到主页
                  router.push({ name: "Index" });
                }
              }
            })
            .catch((res) => {
              if (res.catch) {
                res.catch();
              }
            })
            .finally(() => {
              modalVisiable.login = false;
            });
        },
        ""
      );
    };
    return {
      password,
      login,
      modalVisiable,
    };
  },
});
</script>

<style lang="less" scoped>
.login-layout {
  height: 100vh;
  flex-direction: column;
  display: flex;
  .title-layout {
    padding: 20px;

    .title-layout-content {
      color: black;
      font-weight: bolder;
    }
  }
  .body-layout {
    display: flex;
    align-items: center; /* 垂直居中对齐 */
    height: 100%;
    .form-layout > * {
      margin-top: 15px;
    }
    .content-right-layout {
      width: 20%;
    }
    .content-left-layout {
      margin-right: 10%;
      padding: 2%;
      width: 60%;
      border: 3px solid; /* 透明边框 */
      border-image: linear-gradient(
          45deg,
          red,
          orange,
          yellow,
          green,
          blue,
          indigo,
          violet
        )
        5 5 round;
    }
  }
}
</style>
