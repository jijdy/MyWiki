<template>
  <div class="home">
    <div :style="{ padding: '8px 8px 0 8px', background: 'rgb(190, 200, 200)' }">
      <a-button ghost>
        Ghost
      </a-button>
    </div>
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src

export default defineComponent({
  name: 'Home',
  components: {
    HelloWorld,
  },
});
</script>
