<template>
  <div id="container">
    <div class="common_bg">
      <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
         <div class="carousel-indicators">
          <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
          <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
          <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>
        <div class="carousel-inner">
          <div class="carousel-item active"> 
            <img src="@/assets/login_1.png" class="d-block" alt="">
            <p >
              Fit Share는 화상회의를 통한 쇼핑, 가상피팅 플랫폼입니다. <br />
              온라인 쇼핑의 새로운 경험을 체험해 보세요.
            </p>
          </div>
          <div class="carousel-item">
            <img src="@/assets/login_2.png" class="d-block" alt="...">
            <p>
              혼자만이 아닌 친구들과 함께하는 쇼핑을 제공합니다.<br /> 
              친구를 찾아 새로운 쇼핑을 경험해보세요.
            </p>
          </div>
          <div class="carousel-item">
            <img src="@/assets/login_3.png" class="d-block" alt="...">
            <p>
              친구의 라이브 쇼핑룸에 참여하여 함께 즐겨 보세요.<br /> 
              스타일링을 비교하고 추천해 볼 수 있습니다.
            </p>
          </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Next</span>
        </button>
      </div>
    </div>

    <div id="login">
      <img id="logo" src="@/assets/logo.png" alt="Fit Share Logo" />
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
.carousel {
  margin-right: 100px;
  height: 100vh;
}

.carousel-indicators button {
  background-color: gray;
}

.carousel-inner {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.carousel-item {
  height: 60%;
  position: relative;
}

.carousel-item img {
  height: 100%;
  width: 100%;
  margin-top: 100px;
  object-fit: contain;
}

.carousel-item p {
  margin-top: 10px;
  line-height: 65px;
  left: 300px;
  font-size: 33px;
  text-align: left;
  display: inline-block;
  color: #1b4d50;
  min-width: 570px;
}

.common_bg {
  float: left;
  font-weight: bold;
  text-align: center;
  font-size: 24px;
  height: 100vh;
  width: 70%;
  background-color: #f4f1ee;
}
#login {
  float: right;
  margin-left: -5%;
  width: 35%;
  height: 100vh;
  border: 1px solid white;
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
}
#login h1 {
  margin-top: 200px;
  text-align: center;
  font-weight: bold;
  font-size: 48px;
}
#loginform {
  margin-top: 5vh;
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
  margin-top: 200px;
  width: 550px;
  height: 140px;
  cursor: unset;
}

img {
  width: 300px;
  cursor: pointer;
}

</style>
