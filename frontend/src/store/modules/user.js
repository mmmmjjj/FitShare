import axios from "../../api/axios";
import { useRouter } from "vue-router";
import { useCookies } from "vue3-cookies";

// 로그인 처리 관련 저장소 모듈
export const user = {
  namespaced: true,
  state: () => ({
    user_id: "",
    user_name: "",
    user_profileURI: "",
    refreshToken: "",
    isLogin: false,
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
    SET_REFRESH_TOKEN: (state, refreshToken) => {
      state.refreshToken = refreshToken;
    },
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    INIT_USER: (state) => {
      state.user_id = "";
      state.user_name = "";
      state.user_profileURI = "";
      state.refreshToken = "";
      state.isLogin = false;
    },
  },
  getters: {
    profileURI({ user_profileURI }) {
      return user_profileURI;
    },
    userName({ user_name }) {
      return user_name;
    },
    refreshToken({ refreshToken }) {
      return refreshToken;
    },
    isLogin({ isLogin }) {
      return isLogin;
    },
    userData(state) {
      return state.user_name + " " + state.user_id;
    },
  },
  actions: {
    setId: ({ commit }, user_id) => {
      commit("SET_USER_ID", user_id);
    },
    setName: ({ commit }, user_name) => {
      commit("SET_USER_NAME", user_name);
    },
    setProfileURI: ({ commit }, user_profileURI) => {
      commit("SET_USER_PROFILE_URI", user_profileURI);
    },
    setRefreshToken: ({ commit }, refreshToken) => {
      commit("SET_REFRESH_TOKEN", refreshToken);
    },
    setIsLogin: ({ commit }, isLogin) => {
      commit("SET_IS_LOGIN", isLogin);
    },
    /* 로그아웃 */
    async logout({ commit }, refreshToken) {
      await axios
        .get("auth/logout", {
          params: {
            refreshToken: refreshToken,
          },
        })
        .then(() => {
          commit("SET_USER_ID", "");
          commit("SET_USER_NAME", "");
          commit("SET_USER_PROFILE_URI", "");
          commit("SET_REFRESH_TOKEN", "");
          commit("SET_IS_LOGIN", false);
          const { cookies } = useCookies();
          cookies.remove("accessToken");
        });
    },
    /* AccessToken 갱신 요청 */
    async refreshToken({ commit }, refreshToken) {
      await axios
        .post("auth/refresh", refreshToken)
        .then((result) => {
          commit("SET_REFRESH_TOKEN", result.data);
        })
        .catch((error) => {
          console.log(error);
          const router = useRouter();
          if (error == 403) {
            // 다시 로그인하라는 메시지 출력 후 로그인으로 이동
            router.push({ name: "Login" });
          }
        });
    },
    initUser: ({ commit }) => {
      commit("INIT_USER");
    },
  },
};
