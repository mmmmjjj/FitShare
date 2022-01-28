<template>
  <div>
    <div v-for="FilteredFriend in FilteredFriends" :key="FilteredFriend.id" class="d-flex mt-3">
      <img :src="FilteredFriend.src" alt="profile-img">
      <div class="ms-3 d-flex flex-column">
        <div class="name-box">
          {{ FilteredFriend.name }}
        </div>
        <div class="d-flex mt-2 button-box">
          <button class="">친구삭제</button>
          <button class="ms-3">채팅하기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FilteredFriendList',
  props: ['FilteredFriends']
}
</script>

<style>
.input-box {
  width: 301px;
  border-radius: 20px;
  text-align: center;
}

button {
  background: #FDFAF3;
  font-size: 12px;
  border-radius: 15px;
  width: 90px;
}

input::placeholder {
  text-align: center;
}

/* user 이름 font-size */
.name-box {
  font-size: 16px;
}

.button-box {
  font-size: 12px;
}
</style>