<template>
  <a-layout-header class="header">
    <div class="logo">MyWiki</div>
    <a-menu
        theme="dark"
        mode="horizontal"
        v-model:selectedKeys="selectedKeys1"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user" :style="user.name? {} : {display:'none'}">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin" :style="user.name? {} : {display:'none'}">
        <router-link to="/admin">管理电子书</router-link>
      </a-menu-item>
      <a-menu-item key="/category" :style="user.name? {} : {display:'none'}">
        <router-link to="/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to="/About">关于作者</router-link>
      </a-menu-item>
      <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="loginOut()"
      >
        <a class="login-menu" v-show="user.name">
          <span>退出登录</span>
        </a>
      </a-popconfirm>
      <a class="login-menu"  v-show="user.name">
        <span>您好：{{user.name}}</span>
      </a>
      <a class="login-menu" v-show="!user.name" @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from "axios";
import { message } from 'ant-design-vue'
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup () {
    // 登录后保存
    const user = computed(() => store.state.user);

    // 用来登录
    const loginUser = ref({
      loginName: "jijdy",
      password: "jijdy123"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // 登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");

          // store.state.user = data.message;
          store.commit("setUser",data.content);

          // console.log("{}",store.state.user);
          // console.log("{}",user.value);
        } else {
          message.error(data.message);
        }
      })
    };

    // 退出登录
    const loginOut = () => {
      console.log("退出登录");

      //传到后端的数据用+来连接
      axios.post('/user/loginOut/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出成功！");
          store.commit("setUser", {});

        } else {
          message.error(data.message);
        }
      })
    };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      user,
      loginOut,

    }
  }
});
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}
.login-menu {
  float: right;
  color: navajowhite;
  padding: 10px;
}
</style>
