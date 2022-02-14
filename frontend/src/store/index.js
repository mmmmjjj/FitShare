import { createStore } from "vuex";
import createPersisedState from "vuex-persistedstate";
import { room } from "@/store/modules/room";
import { user } from "@/store/modules/user";
import { friend } from "@/store/modules/friend";

export default createStore({
  state: {
    url: "https://i6a405.p.ssafy.io/api",
  },
  getters: {},
  mutations: {},
  actions: {},
  plugins: [
    createPersisedState({
      paths: ["user"],
    }),
  ],
  modules: {
    room,
    user,
    friend,
  },
});
