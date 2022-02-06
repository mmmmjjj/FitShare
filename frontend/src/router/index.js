import { createWebHistory, createRouter } from "vue-router";
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
  },
  {
    path: "/create",
    name: "CreateRoom",
    component: CreateRoom,
  },
  {
    path: "/room/:roomId",
    name: "ShoppingRoom",
    component: ShoppingRoom,
  },
  {
    path: "/callback",
    name: "Callback",
    component: Callback,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
