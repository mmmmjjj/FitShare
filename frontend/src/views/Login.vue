<template>
  <div id="container">
    <div class="common_bg">
      <h1 id="logo"><i>FIT SHARE</i></h1>
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
        <span id="span">간편 로그인</span><br />

        <div id="imageButton">
          
          <div id="kakao">
            <a
              href="https://kauth.kakao.com/oauth/authorize?client_id=9596c9c79f92bc2d9fbdeacfee238961&redirect_uri=http://localhost:8080/callback&response_type=code">
              <img
                src="//k.kakaocdn.net/14/dn/btqCn0WEmI3/nijroPfbpCa4at5EIsjyf0/o.jpg"
                alt="kakao_login_image"
              />
            </a>
          </div>
        </div>

        <div id="naver">
          <a href="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=skkqxLCLHDQrwOlhytTS&redirect_uri=http://localhost:8080/callback&state=1234">
            <img
              src="https://static.nid.naver.com/oauth/big_g.PNG?version=js-2.0.0"
              alt="naver_login_image"
            />
          </a>
          
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { onMounted } from "vue";
export default {
  name: "Login",
  setup() {

  },
};
</script>

<style scoped>
#container {
}
.common_bg {
  float: left;
  font-weight: bold;
  text-align: center;
  font-size: 24px;
  height: 1024px;
  width: 70%;
  background-color: #b3e2e7;
}
#login {
  float: right;
  width: 30%;
  height: 1024px;
  border: 1px solid white;
  border-radius: 30px 0 0 30px;
  background-color: white;
}
#login h1 {
  padding-top: 30%;
  text-align: center;
  font-weight: bold;
  font-size: 48px;
}
#loginform {
  padding-top: 60%;
  padding-left: 10%;
}
#span {
  font-size: 24px;
  font-weight: bold;
}
#kakao{
  margin-bottom: 10px;
}
#logo {
  padding-top: 100px;
  font-size: 96px;
  font-family: "Times New Roman", Times, serif;
  color: #ffff99;
  font-weight: bold;
  text-shadow: 2px 2px 2px gray;
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
  width: 350px;
  cursor: pointer;
}
</style>
