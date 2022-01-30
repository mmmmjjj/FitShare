<template>
  <div>
    <Navbar/>
    <h1>home</h1>
    <div class="container">

      <!-- 버튼 클릭 시 친구 아이콘 html / css tab 버전 -->
      <button class="friend-btn" @click="OpenTab">
        <img class="friend_icon" src="@/assets/friend_icon.png" alt="friend_icon">
      </button>
      <div v-if="status">
        <friend class="tab-box" @close-tab="CloseTab"></friend>
        <button>
          <img class="close-img" @click="CloseTab" src="@/assets/close-icon.png" alt="close-img">
        </button>
      </div>

    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import Friend from '@/components/main/Friend.vue'
import { ref } from 'vue'

export default {
  name: 'Main',
  components: { 
    Navbar,
    Friend,
  },
  setup() {
    const status = ref(false)

    const OpenTab = () => {
      status.value = true
    };

    const CloseTab = () => {
      status.value = false
    };

    return {
      status,
      OpenTab,
      CloseTab,
    }
  }
}
</script>

<style scoped>
.container {
  width: 1156px;
  height: 944px;
  border: solid black;
}

.friend_icon {
  width: 50px;
  height: 50px;
  left: 25px;
  top: 25px;
  position: absolute;
}

.tab-box {
  top: 300px;
  left: 1100px;
  position: fixed;
}

/* 닫기 이미지 style */
.close-img {
  width: 30px;
  height: 30px;
  top: 265px;
  position: fixed;
  left: 1100px;
}

.friend-btn {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  position: relative;
  left: 1095px;
  top: 700px;
  background-color: #8ABDBE;
}
</style>