<template>
  <nav class="navbar">
    <img @click="goToMain" src="@/assets/logo.png" alt="Fit Share Logo" />
    <div class="nav-end">
      <div class="create" @click="goToCreate">쇼핑룸 생성</div>
      <div class="username">{{ userName }}님</div>
      <img @click="!isVisible ? (isVisible = true) : (isVisible = false)" :src="profileImage" alt="profile image" />
      <div class="dropdown" v-if="isVisible">
        <p @click="logout">로그아웃</p>
      </div>
    </div>
  </nav>
</template>

<script>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

export default {
  name: "Navbar",
  setup() {
    const router = useRouter();
    const store = useStore();

    let isVisible = ref(false);

    const profileImage = computed(() => {
      return store.state.user.user_profileURI;
    });

    const userName = computed(() => {
      return store.state.user.user_name;
    });

    const goToMain = () => {
      router.push({ name: "Main" });
    };

    const goToCreate = () => {
      router.push({ name: "CreateRoom" });
    };

    const logout = () => {
      store.dispatch("user/logout", store.getters["user/refreshToken"]);
      router.push({ name: "Login" });
    };

    return {
      isVisible,
      profileImage,
      userName,
      goToMain,
      goToCreate,
      logout,
    };
  },
};
</script>

<style scoped>
nav {
  height: 60px;
  justify-content: space-between;
  align-items: center;
  padding-top: 3px;
  background-color: #1b4d50;
}

nav > img {
  width: 190px;
  height: 45px;
  margin-left: 40px;
  cursor: pointer;
}

.nav-end {
  display: flex;
  flex-direction: row;
  line-height: 60px;
}

.create {
  font-weight: bold;
  font-size: 18px;
  color: #1b4d50;
  cursor: pointer;
  background-color: #fdfaf3b9;
  border-radius: 10px;
  width: 120px;
  height: 42px;
  line-height: 42px;
  text-align: center;
  margin-top: 6px;
}

.username {
  color: #f5ead2;
  font-weight: bold;
  height: 42px;
  line-height: 55px;
  margin-left: 40px;
}

.nav-end img {
  border: 2px solid #edeef2;
  border-radius: 16px;
  margin: 2px 45px 0 20px;
  width: 50px;
  height: 50px;
  cursor: pointer;
}

.dropdown {
  position: absolute;
  top: 62px;
  right: 10px;
  width: 120px;
  height: 51px;
  font-size: 16px;
  background-color: rgba(255, 255, 255, 0.658);
  border: 1px solid lightgray;
  border-radius: 5px;
  box-shadow: 3px 3px 3px rgb(207, 206, 206);
}

.dropdown p {
  height: 45px;
  margin-bottom: 0px;
  text-align: center;
  cursor: pointer;
  line-height: 45px;
}

.dropdown p:hover {
  color: #81b5b6;
}
</style>
