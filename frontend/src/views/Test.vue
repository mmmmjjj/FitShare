<template>
  <h1>테스트 페이지.</h1>
  <div>
    인가코드 : <br />
    {{ $route.query.code }}
  </div>


  <button @click="logout()">로그아웃</button>
</template>

<script>
import axios from 'axios';
import { onMounted } from '@vue/runtime-core';

export default {
    setup () {
      onMounted(() => {
        getAccessToken();
        getKakaoAccount();
      })

      const getKakaoAccount = () => {
        window.Kakao.API.request({
        url: "/v2/user/me",
        success: async function (response) {
          const kakao_account = response.kakao_account;
          const profile_nickname = kakao_account.profile.nickname; // 카카오 닉네임
          const profile_image = kakao_account.profile_image; // 카카오 프로필 사진
          const account_email = kakao_account.email; // 카카오 계정(이메일)
          const gender = kakao_account.gender; // 카카오 성별
          const age_range = kakao_account.age_range; // 카카오 연령대
          const birthday = kakao_account.birthday; // 카카오 생일

          console.log("kakao_nickname :", profile_nickname);
          console.log("kakao_image : " + profile_image);
          console.log("e-mail :", account_email);
          console.log("gender : " + gender);
          console.log("age_range : " + age_range);
          console.log("birthday : " + birthday);
          
          let path = "http://" + window.location.hostname + ":8080/login";
          axios.post(path, {
            // uid : 
            name: profile_nickname,
            email: account_email,
            // phone
            profile_img: profile_image,
          })
          alert("안녕하세요. " + profile_nickname +"님");
        },
        fail: function (error) {
          console.log(error);
        },
      });
    }
    const getAccessToken = () => {
      if(window.Kakao.Auth.getAccessToken()){
        window.Kakao.API.request({
          url: '/v1/user/unlink',
          success: function(response) {
            console.log("getAccessToken: ");
            console.log(response);

          },
          fail: function(error) {
            console.log(error);
          }
        })
        window.Kakao.Auth.setAccessToken(undefined);
     }
    }

    const storeKakaoAccount = () =>{
      window.Kakao.API.request({
        url: '/v1/user/update_profile',
        data: {
          properties: {
            nickname: '',
            age: ''
          },
        },
        success: function(response){
          console.log(response);
        },
        fail: function(error){
          console.log(error);
        }
      })

    }
    const logout = () => {
      if(!window.Kakao.Auth.getAccessToken()) {
        console.log('Not logged in.');
        return;
      }
      else{
        window.Kakao.Auth.logout(function() {
          console.log(window.Kakao.Auth.getAccessToken());
        })
      }
    }
        return {
            getKakaoAccount,
            getAccessToken,
            storeKakaoAccount,
            logout
        }
    }
}
</script>

<style>

</style>