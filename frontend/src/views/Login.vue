<template>
  <div id="container">
    <div class="common_bg">
      <img id="logo" src="@/assets/logo.png" alt="Fit Share Logo" />
      <div id="content">
        Find 3D Objects, Mockups and<br />
        Illustrations here.<br /><br />
        Fit Share for all the usrs to find their<br />
        friends and also use video conference to<br />
        match their clothes by virtual styling
      </div>
    </div>

    <div id="login">
      <h1>로그인</h1>

      <div id="loginform">
        <div id="imageButton">
          <div id="kakao">
            <a
              href="https://kauth.kakao.com/oauth/authorize?client_id=9596c9c79f92bc2d9fbdeacfee238961&redirect_uri=https://i6a405.p.ssafy.io/callback&response_type=code"
            >
              <img src="//k.kakaocdn.net/14/dn/btqCn0WEmI3/nijroPfbpCa4at5EIsjyf0/o.jpg" alt="kakao_login_image" />
            </a>
          </div>
        </div>

        <div id="naver">
          <a
            href="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=skkqxLCLHDQrwOlhytTS&redirect_uri=https://i6a405.p.ssafy.io/callback&state=1234"
          >
            <img src="https://static.nid.naver.com/oauth/big_g.PNG?version=js-2.0.0" alt="naver_login_image" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import { useCookies } from "vue3-cookies";
export default {
  name: "Login",
  setup() {
    const store = useStore();
    const { cookies } = useCookies();
    store.dispatch("user/initUser");
    cookies.remove("accessToken");
  },
};
</script>

<style scoped>
.common_bg {
  float: left;
  font-weight: bold;
  text-align: center;
  font-size: 24px;
  height: 100vh;
  width: 70%;
  background-color: #b3e2e7;
}
#login {
  float: right;
  margin-left: -5%;
  width: 35%;
  height: 100vh;
  border: 1px solid white;
  border-radius: 30px 0 0 30px;
  background-color: white;
}
#login h1 {
  margin-top: 150px;
  text-align: center;
  font-weight: bold;
  font-size: 48px;
}
#loginform {
  margin-top: 30vh;
  text-align: center;
}
#span {
  font-size: 24px;
  font-weight: bold;
}
#kakao {
  margin-bottom: 10px;
}
#logo {
  margin-top: 100px;
  width: 570px;
  height: 150px;
  cursor: unset;
}
#content {
  padding-top: 100px;
  padding-left: 25%;
  color: #ffffff;
  font-size: 30px;
  line-height: 200%;
  text-align: left;
}

img {
  width: 300px;
  cursor: pointer;
}
</style>
