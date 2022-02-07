import { createStore } from 'vuex';
import room from '@/store/modules/room';

export default createStore({
	state: {
		url: 'http://i6a405.p.ssafy.io:8081'
	},
	getters: {
	},
	mutations: {
	},
	actions: {
	},
	modules: {
		room
	},
});
