import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import  * as Icons from '@ant-design/icons-vue'
import axios from "axios";
import {Tool} from "@/utils/tool";
axios.defaults.baseURL = process.env.VUE_APP_SERVER;

//axios前端信息拦截器，用于打印前端信息日志全局使用
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);

    const token = store.state.user.token;
    //为req请求信息添加token请求头，以便后端能够拿到token并进行判断
    if (Tool.isNotEmpty(token)) {
        config.headers.token = token;
        // console.log("请求headers增加token:", token);
    }

    return config;
},error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
    console.log('返回结果：',response);
    return response;
},error => {
    return Promise.reject(error);
});
//Promise.reject 逻辑就不会向后走，也就不会进入到业务代码中

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app')

//全局使用图标，即全局配置
const icons : any = Icons;
for (const i in icons) {
    app.component(i,icons[i]);
}

console.log("环境:" + process.env.NODE_ENV);
console.log("服务器:" + process.env.VUE_APP_SERVER);
