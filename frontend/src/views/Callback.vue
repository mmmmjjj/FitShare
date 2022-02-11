<template>
  <div class="container">
    <div class="loading">
      <span></span>
      <span></span>
    </div>
  </div>
</template>

<script>
import { reactive, onMounted } from "vue";
import axios from "axios";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import { useCookies } from "vue3-cookies";
export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();
    const { cookies } = useCookies();

    const state = reactive({
      kakaoCode: "",
      naverCode: "",
      naverState: "",
    });

    onMounted(() => {
      if(route.query.state){
        console.log("네이버 로그인");
        state.naverCode = route.query.code;
        state.naverState = route.query.state;
        getNaverToken(state.naverCode, state.naverState);
      }
      else{
        console.log("카카오 로그인");
        state.kakaoCode = route.query.code;
        getKakakoToken(state.kakaoCode);
      }
    });

    const getKakaoAccount = async (kakaoToken) => {
      let path = "http://i6a405.p.ssafy.io:8081/api/v1/auth/kakao/login";
      await axios
        .get(path, {
          params: {
            accessToken: kakaoToken,
          },
        })
        .then((res) => {
          if (res.data.statusCode === 201) {
            console.log("카카오 계정 정보");
            console.log(res);
            // Cookie에 'accessToken' 설정
            cookies.set("accessToken", res.data.data.accessToken);

            store.dispatch("login/getId", res.data.data.id, { root: true });
            store.dispatch("login/getName", res.data.data.name, { root: true });
            store.dispatch("login/getProfileURI", res.data.data.profileURI, {
              root: true,
            });
            movePage();
          }
        })
        .catch((error) => {
          console.log(error);
        });
    };

    const getNaverAccount = async (naverToken) => {
      let path = "http://i6a405.p.ssafy.io:8081/api/v1/auth/naver/login";
      await axios
        .get(path, {
          params: {
            accessToken: naverToken,
          },
        })
        .then((res) => {
          if (res.data.statusCode === 201) {
            console.log("네이버 계정 정보");
            console.log(res);
            // Cookie에 'accessToken' 설정
            cookies.set("accessToken", res.data.data.accessToken);
            store.dispatch("login/getId", res.data.data.id, { root: true });
            store.dispatch("login/getName", res.data.data.name, { root: true });
            store.dispatch("login/getProfileURI", res.data.data.profileURI, {
              root: true,
            });
            movePage();
          }
        })
        .catch((error) => {
          console.log(error);
        });
    };

    const getKakakoToken = async (kakaoCode) => {
      let path = `http://i6a405.p.ssafy.io:8081/api/v1/auth/kakao/token?code=${kakaoCode}`;
      await axios
        .get(path)
        .then((res) => {
          if (res.data.statusCode === 200) {
            console.log("카카오 Access Token 생성 성공");
            getKakaoAccount(res.data.data);
          }
        })
        .catch((error) => {
          console.log("카카오 Access Token 생성 실패");
          console.log(error);
        });
    };

    const getNaverToken = async (naverCode, naverState) => {
      let path = `http://i6a405.p.ssafy.io:8081/api/v1/auth/naver/token?code=${naverCode}&state=${naverState} `;
      await axios
        .get(path)
        .then((res) => {
          if (res.data.statusCode === 200) {
            console.log("네이버 Access Token 생성 성공");
            getNaverAccount(res.data.data);
          }
        })
        .catch((error) => {
          console.log("네이버 Access Token 생성 실패");
          console.log(error);
        });
    };

    const movePage = () => {
      router.push({ name: "Main" });
    };

    return {
      getKakakoToken,
      getNaverToken,
      getKakaoAccount,
      getNaverAccount,
      cookies,
      movePage,
    };
  },
};
</script>

<style scoped>
.container {
  line-height: 1.5em;
  margin: 0;
  font-weight: 300;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

a {
  text-decoration: none;
}

.loading {
  /* border : 1px solid red; */
  width: 30px;
  height: 30px;
  position: relative;
}
.loading span {
  position: absolute;
  width: 10px;
  height: 10px;
  background-color: gray;
  top: 0;
  left: 0;
  animation: loading 1.5s infinite;
}
.loading span:nth-child(1) {
  background-color: crimson;
}
.loading span:nth-child(2) {
  animation-delay: 0.8s;
}

@keyframes loading {
  0% {
    top: 0;
    left: 0;
  }
  25% {
    top: 0;
    left: calc(100% - 10px);
    background-color: dodgerblue;
  }
  50% {
    top: calc(100% - 10px);
    left: calc(100% - 10px);
    background-color: orange;
  }
  75% {
    top: calc(100% - 10px);
    left: 0;
    background-color: yellowgreen;
  }
  100% {
    top: 0;
    left: 0;
  }
}
</style>
