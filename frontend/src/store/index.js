import { createStore } from 'vuex';
import createPersisedState from 'vuex-persistedstate';
import { room } from "@/store/modules/room";
import { user } from "@/store/modules/user";
import { friend } from "@/store/modules/friend";

export default createStore({
	state: {
		url: 'http://i6a405.p.ssafy.io:8081/api'
	},
	getters: {
	},
	mutations: {
	},
	actions: {
  },
  plugins: [
    createPersisedState()
  ],
	modules: {
		room,
    user,
		friend,
	}
});
