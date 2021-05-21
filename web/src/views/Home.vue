<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <template #title>
              <span>
                <user-outlined />
                subnav 1
              </span>
          </template>
          <a-menu-item key="1">option1</a-menu-item>
          <a-menu-item key="2">option2</a-menu-item>
          <a-menu-item key="3">option3</a-menu-item>
          <a-menu-item key="4">option4</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <template #title>
              <span>
                <laptop-outlined />
                subnav 2
              </span>
          </template>
          <a-menu-item key="5">option5</a-menu-item>
          <a-menu-item key="6">option6</a-menu-item>
          <a-menu-item key="7">option7</a-menu-item>
          <a-menu-item key="8">option8</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub3">
          <template #title>
              <span>
                <notification-outlined />
                subnav 3
              </span>
          </template>
          <a-menu-item key="9">option9</a-menu-item>
          <a-menu-item key="10">option10</a-menu-item>
          <a-menu-item key="11">option11</a-menu-item>
          <a-menu-item key="12">option12</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <pre>
{{ebooks}}
{{books}}
      </pre>

    </a-layout-content>
  </a-layout>

</template>

<script lang="ts">
import { defineComponent, onMounted,ref,reactive,toRef} from 'vue'; // @ is an alias to /src
import axios from "axios"
export default defineComponent({
  name: 'Home',
  setup() {
    console.log("setup");
    const ebooks = ref();
    const ebooks1 = reactive({bookS: []});
    onMounted(() => {
      console.log("onMounted")
      axios.get("http://localhost:8089/ebook/list?name=vue").then(
          (response =>
          {
            const data = response.data;
            ebooks.value = data.content;
            ebooks1.bookS = data.content;
            console.log(response);
          })
      );
    });
    return {
      ebooks,
      books: toRef(ebooks1,"bookS")
    }
  }
});
//onMounted ,生命周期函数。
//①ref(),表示将数据更改为响应式的数据，可以在后端将数据改变之后直接显示在前端页面。。。response响应
//②reactive(),用法同上，但是在数值传递时进行toRef()转换，键数据转换为响应式的数据。。responseText响应
// return在js中还要加大括号

</script>
