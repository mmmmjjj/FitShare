import { createWebHistory, createRouter } from "vue-router";
import { useCookies } from "vue3-cookies";
import Login from "@/views/Login.vue";
import Main from "@/views/Main.vue";
import CreateRoom from "@/views/CreateRoom.vue";
import ShoppingRoom from "@/views/ShoppingRoom.vue";

import Callback from "@/views/Callback.vue";

const routes = [
  {
    path: "/",
    name: "Login",
    component: Login,
  },
  {
    path: "/main",
    name: "Main",
    component: Main,
    meta: { requiresAuth: true },
  },
  {
    path: "/create",
    name: "CreateRoom",
    component: CreateRoom,
    meta: { requiresAuth: true },
  },
  {
    path: "/room/:roomId",
    name: "ShoppingRoom",
    component: ShoppingRoom,
    meta: { requiresAuth: true },
  },
  {
    path: "/callback",
    name: "Callback",
    component: Callback,
  },
];
const { cookies } = useCookies();

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

router.beforeEach((to, from, next) => {
  const isLogin = cookies.get("accessToken");
  // 로그인 여부 확인
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (isLogin == null) {
      alert("login please");
      next({ path: "/" });
    } else next();
  } else next();
});
