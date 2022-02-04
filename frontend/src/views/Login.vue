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
        <a id="kakako_login" @click="kakaoLogin()">
          <img
            src="//k.kakaocdn.net/14/dn/btqCn0WEmI3/nijroPfbpCa4at5EIsjyf0/o.jpg"
            width="350"
            alt="kakao_login_image"
          />
        </a>
        <a id="naver_login" @click="naverLogin()">
          <img src="https://static.nid.naver.com/oauth/big_g.PNG?version=js-2.0.0"
          width="350"
          alt="naver_login_image">
        </a>
      </div>
      
      
    </div>
  </div>
</template>

<script>

export default {
  name: "Login",
  setup () {
   const kakaoLogin = () => {
      window.Kakao.Auth.login({
        scope: "profile_nickname, profile_image, account_email, gender, age_range, birthday",
        success: function (authObj){
          console.log(authObj);
          window.Kakao.Auth.setAccessToken(authObj.access_token);
          const params = {
            redirectUri: "http://localhost:8080/test"
          };
          window.Kakao.Auth.authorize(params);
        }
      });
    }
    
    const naverLogin = () => {
      const naver_id_login = new window.naver_id_login("Client Id", "callback URL");
      const state = naver_id_login.getUniqState();
      naver_id_login.setState(state);
      naver_id_login.init_naver_id_login();
    }

    return{
      kakaoLogin,
      naverLogin,
    }
  }
};
</script>

<style>
#container{

}
.common_bg{
  float: left;
  font-weight: bold;
  text-align: center;
  font-size: 24px;
  height: 1024px;
  width: 70%;
  background-color: #B3E2E7;
}
#login{
  float: right;
  width: 30%;
  height: 1024px;
  border: 1px solid white;
  border-radius: 30px 0 0 30px;
}
#login h1{
  padding-top: 30%;
  text-align: center;
  font-weight: bold;
  font-size: 48px;
}
#loginform{
  padding-top: 60%;
  padding-left: 10%;
}
#span{
  font-size: 24px;
  font-weight: bold;
}
#logo{
  padding-top: 100px;
  font-size: 96px;
  font-family: 'Times New Roman', Times, serif;
  color: #FFFF99;
  font-weight: bold;
  text-shadow: 2px 2px 2px gray;;
  
}
#content{
  padding-top: 100px;
  padding-left: 25%;
  color: #FFFFFF;
  font-size: 30px;
  line-height: 200%;
  text-align: left;
}
img{
  cursor: pointer;
}
</style>
