<template>
  <div>
    <Navbar/>
    <h1>home</h1>
    <div class="container">

      <!-- HostCloset -->
      <host-closet class="host-closet"></host-closet>

      <!-- 버튼 클릭 시 친구 아이콘 html / css tab 버전 -->
      <button class="friend-btn" @click="OpenTab">
        <img class="friend_icon" src="@/assets/friend_icon.png" alt="friend_icon">
      </button>
      <div v-if="status">
        <friend class="tab-box" @close-tab="CloseTab"></friend>
        <button class="close-btn" @click="CloseTab">
          <img class="close-img" src="@/assets/close-icon.png" alt="close-img">
        </button>
      </div>

    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import Friend from '@/components/main/Friend.vue'
import { ref } from 'vue'
import HostCloset from '../components/main/HostCloset.vue'

export default {
  name: 'Main',
  components: { 
    Navbar,
    Friend,
    HostCloset,
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

/* host 옷장 style */
.host-closet {
  width: 543px;
  height: 595px;
  background-color: #FDFAF3;
  left: 582px;
  z-index: 0;
  position: relative;
  border-radius: 25px;
}

.friend-btn {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  position: relative;
  left: 1095px;
  top: 100px;
  background-color: #8ABDBE;
  z-index: 1;
}

.friend_icon {
  width: 50px;
  height: 50px;
  left: 25px;
  top: 25px;
  position: absolute;
}

/* 친구버튼 클릭시 나오는 tab style */
.tab-box {
  top: 300px;
  left: 1100px;
  position: fixed;
  z-index: 2;
}

/* 닫기 이미지 style */
.close-img {
  width: 30px;
  height: 30px;
  top: 265px;
  position: fixed;
  left: 1100px;
}


.close-btn {
  width: 30px;
  height: 30px;
  border-radius: 25px;
  position: fixed;
  top: 265px;
  left: 1100px;
}

</style>