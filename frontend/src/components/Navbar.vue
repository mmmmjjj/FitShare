<template>
  <nav class="nav">
    <img @click="goToMain" src="@/assets/logo.png" alt="Fit Share Logo">
    <div class="nav-end">
      <div class="create" @click="goToCreate">쇼핑룸 생성</div>
      <img @click="!isVisible ? isVisible=true : isVisible=false" :src="profileImage" alt="profile image">
      <div class="dropdown" v-if="isVisible">
        <p>마이 프로필</p>
        <p>Logout</p>
      </div>
    </div>
  </nav>
</template>

<script>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

export default {
    name: 'Navbar',
    setup() {
      const router = useRouter();
      const store = useStore();

      let isVisible = ref(false);

      const profileImage = computed(() => {
        return store.state.login.user_profileURI
      });

      const goToMain = () => {
        router.push({ name: 'Main' }) 
      };

      const goToCreate = () => {
        router.push({ name: 'CreateRoom' }) 
      };

      return {
        isVisible, profileImage, goToMain, goToCreate
      }
    }
}
</script>

<style scoped>
nav {
  height: 73px;
  border-bottom: 3px solid #8ABDBE;
  justify-content: space-between;
  padding-top: 3px;
}

nav > img {
  width: 230px;
  height: 55px;
  margin: 3px 0 0 40px;
  cursor: pointer;
}

.nav-end {
  display: flex;
  flex-direction: row;
  line-height: 66px;
}

.create {
  font-weight: bold;
  font-size: 24px;
  color: rgb(66, 65, 65);
  cursor: pointer;
}

.nav-end img {
  border: 2px solid #EDEEF2;
  border-radius: 16px;
  margin: 2px 45px 0 60px;
  width: 60px;
  cursor: pointer;
}

.dropdown {
  position: absolute;
  top: 74px;
  right: 5px;
  width: 141px;
  height: 110px;
  font-size: 20px;
  background-color: white;
  border: 1px solid lightgray;
  border-radius: 5px;
  box-shadow: 4px 4px 4px rgb(207, 206, 206)
}

.dropdown:after{
  content: '';
  position: absolute;
  border-style: solid;
  border-width: 0 11px 15px;
  border-color: #FFFFFF transparent;;
  display: block;
  width: 0;
  z-index: 1;
  top: -8px;
  left: 60px;
}

p { 
  height: 55px;
  margin-bottom: 0px;
  text-align: center;
  cursor: pointer;
  line-height: 55px;
}

p:hover {
  color: #81b5b6;
}

</style>