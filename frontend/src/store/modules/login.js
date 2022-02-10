
// 로그인 처리 관련 저장소 모듈
export const login = {
    namespaced: true,
    state: () => ({
        user_id: "",
        user_name: "",
        user_profileURI: "",

        id: [],
        name: [],

        accessToken: null,
        refreshToken: null,
    }),

    mutations: {
        SET_USER_ID: (state, user_id) => {
            state.user_id = user_id;
        },
        SET_USER_NAME: (state, user_name) => {
            state.user_name = user_name;
        },
        SET_USER_PROFILE_URI: (state, user_profileURI) => {
            state.user_profileURI = user_profileURI;
        },

        // loginToken (state, payload){
        //     VueCookies.set('accessToken', payload.accessToken, '60s');
        //     VueCookies.set('refreshToken', payload.refreshToken, '1h');
        //     state.accessToken = payload.accessToken;
        //     state.refreshToken = payload.refreshToken;
        // },

        // refreshToken(state, payload) {
        //     VueCookies.set('accessToken', payload.accessToken, '60s');
        //     VueCookies.set('refreshToken', payload.refreshToken, '1h');
        //     state.accessToken = payload;
        // },
        
        // removeToken() {
        //     VueCookies.remove('accessToken');
        //     VueCookies.remove('refreshToken');
        // },
    },
    getters: {
        userData(state) {
           return state.user_name + ' ' + state.user_id
        },
    },
    actions: {
        getId: ({commit}, user_id) => {
            commit("SET_USER_ID", user_id);
        },
        getName: ({commit}, user_name) => {
            commit("SET_USER_NAME", user_name);
        },
        getProfileURI: ({commit}, user_profileURI) => {
            commit("SET_USER_PROFILE_URI", user_profileURI);
        },
        
        
        logout: ({commit}) => {
            commit('removeToken');
            location.reload();
        }
    }
}

