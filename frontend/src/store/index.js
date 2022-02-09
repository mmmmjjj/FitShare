import { createStore } from 'vuex';

import { room } from '@/store/modules/room';
import { login } from '@/store/modules/login'
import { friend } from '@/store/modules/friend'

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

	modules: {
		room,
		login,
		friend,
	}
});


