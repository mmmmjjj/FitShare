<template>
  <div>
    <div v-for="friend in friends" :key="friend.id" class="d-flex mt-3">
      <img :src="friend.src" alt="profile-img">
      <div class="ms-3 d-flex flex-column">
        <div>
          {{ friend.name }}
        </div>
        <div class="d-flex mt-2 button-box">
          <button class="">수락</button>
          <button class="ms-3">거절</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'FriendCheckTab',
  setup() {
    const friends = ref([
      {id: 1, name: '김싸피', src: require('@/assets/friend_icon.png')},
      {id: 2, name: '김싸피', src: require('@/assets/friend_icon.png')},
      {id: 3, name: '김싸피', src: require('@/assets/friend_icon.png')},
      {id: 4, name: '김싸피', src: require('@/assets/friend_icon.png')},
      {id: 5, name: '김싸피', src: require('@/assets/friend_icon.png')},
      {id: 6, name: '김싸피', src: require('@/assets/friend_icon.png')}
    ])

    return {
      friends,
    }
  }
}
</script>

<style scoped>
.button-box {
  font-size: 12px;
}

.button-box > button {
  background: #FDFAF3;
  font-size: 12px;
  border-radius: 15px;
  width: 90px;
}
</style>