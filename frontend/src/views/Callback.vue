<template>
  <div></div>
</template>

<script>
import { reactive } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";

export default {
  setup() {
    const route = useRoute();

    const state = reactive({
      code: String,
      kakaoToken: String,
    });

    const login = (token) => {
      let path2 = "http://localhost:8081/api/v1/kakao/login";
      axios
        .get(path2, {
          params: {
            accessToken: token,
          },
        })
        .then((data) => {
          console.log(data);
          // 토큰 저장
          // 이미지, 이름 저장
          // 메인페이지로 리다이렉트
        });
    };

    const getToken = (code) => {
      let path = `http://localhost:8081/api/v1/kakao/auth?code=${code}`;
      axios.get(path).then((data) => {
        state.kakaoToken = data.data.data;
        login(state.kakaoToken);
      });
    };

    state.code = route.query.code;
    getToken(state.code);

    return {
      getToken,
    };
  },
};
</script>

<style></style>
