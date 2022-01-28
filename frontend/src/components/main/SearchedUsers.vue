<template>
  <div>
    <div v-if="SearchedUsers" class="text-center result-box mt-3">
      검색 결과
    </div>
    <div v-for="SearchedUser in SearchedUsers" :key="SearchedUser.id" class="d-flex mt-3">
      <img :src="SearchedUser.src" alt="profile-img">
      <div class="ms-3 d-flex flex-column profile-box">
        <div class="d-flex justify-content-between name-box">
          {{ SearchedUser.name }}
        </div>
        <div class="d-flex mt-2 email-box">
          {{ SearchedUser.email }}
        </div>
      </div>
      <div class="align-self-center">
        <img src="@/assets/plus_icon.png" alt="plus">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SearchedUsers',
  props: ['SearchedUsers']
}
</script>

<style scoped>
.input-box {
  width: 301px;
  border-radius: 20px;
  text-align: center;
}

input::placeholder {
  text-align: center;
}

.result-box {
  font-size: 12px;
  color: '#565656'
}

.profile-box {
  width: 180px;
}

/* user 이름 font-size */
.name-box {
  font-size: 16px;
}

/* email font-size */
.email-box {
  font-size: 12px;
}

button {
  width: 32px;
}
img {
  width: 50px;
  height: 50px;
}
</style>