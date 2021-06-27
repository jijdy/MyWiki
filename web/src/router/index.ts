import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'
import Doc from '../views/doc.vue'
import User from "../views/admin/admin-user.vue"
import store from "@/store";
import {Tool} from "@/utils/tool";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/admin/user',
    name: 'user',
    component: User,
      meta: {
          loginRequire: true
      }
  },
  {
    path: '/about',
    name: 'About',
    component: About,
      meta: {
          loginRequire: true
      }
  },
  {
  path: '/admin',
      name: 'AdminEbook',
      component: AdminEbook,
      meta: {
          loginRequire: true
      }
  },
  {
  path: '/category',
      name: 'AdminCategory',
      component: AdminCategory,
      meta: {
          loginRequire: true
      }
  },
  {
  path: '/doc',
      name: 'AdminDoc',
      component: AdminDoc
  },
  {
  path: '/isDoc',
      name: 'Doc',
      component: Doc
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
    // 要不要对meta.loginRequire属性做监控拦截
    if (to.matched.some(function (item) {
        console.log(item, "是否需要登录校验：", item.meta.loginRequire);
        return item.meta.loginRequire
    })) {
        const loginUser = store.state.user;
        if (Tool.isEmpty(loginUser)) {
            console.log("用户未登录！");
            next('/');
        } else {
            next();
        }
    } else {
        next();
    }
});
export default router
