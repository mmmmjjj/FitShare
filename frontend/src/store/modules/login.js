import axios from 'axios';
import VueCookies from 'vue-cookies';

// 로그인 처리 관련 저장소 모듈
export const login = {
    state: {
        host: 'http://localhost:8081',
        accessToken: null,
        refreshToken: null,
    },
    mutations: {
        loginToken (state, payload){
            VueCookies.set('accessToken', payload.accessToken, '60s');
            VueCookies.set('refreshToken', payload.refreshToken, '1h');
            state.accessToken = payload.accessToken;
            state.refreshToken = payload.refreshToken;
        },
        refreshToken(state, payload) {
            VueCookies.set('accessToken', payload.accessToken, '60s');
            VueCookies.set('refreshToken', payload.refreshToken, '1h');
            state.accessToken = payload;
        },
        removeToken() {
            VueCookies.remove('accessToken');
            VueCookies.remove('refreshToken');
        },
    },
    getters: {
        getToken(state) {
            let accessToken = VueCookies.get('accessToken');
            let refreshToken = VueCookies.get('refreshToken');
            return {
                access: accessToken,
                refresh: refreshToken, 
            };
        }
    },
    actions: {
        login: ({commit}, params) => {
            return new Promise((resove, reject) => {
                axios.post('/v1/auth/login', params).then(res => {
                    resove(res);
                })
                .catch(err => {
                    console.log(err.message);
                    reject(err.message);
                });
            })
        },
        refreshToken: ({commit}) => {
            return new Promise((resolve, reject) => {
                axios.post('/v1/auth/certify').then(res => {
                    commit('refreshToken', res.data.auth_info);
                    resolve(res.data.auth_info);
                }).catch(err => {
                    console.log('refreshToken error : ', err.config);
                    reject(err.config.data);
                })
            })
        },
        logout: ({commit}) => {
            commit('removeToken');
            location.reload();
        }
    },
}