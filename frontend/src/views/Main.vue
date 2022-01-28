<template>
  <div>
    <Navbar/>
    <h1>home</h1>
    <div class="container">


      <!-- 친구 아이콘 html / css tab 버전 -->
      <div id="circle" @click="OpenTab">
        <img class="friend_icon" src="@/assets/friend_icon.png" alt="friend_icon">
      </div>
      <div v-if="status">
        <friend class="tab-box" @close-tab="CloseTab"></friend>
        <!-- <button class="btn btn-position" @click="CloseTab">닫기</button> -->
        <!-- <button class="btn btn-position" @click="CloseTab">
          <img class="close-img" src="@/assets/close-icon.png" alt="">
        </button> -->
        <img class="close-img" @click="CloseTab" src="@/assets/close-icon.png" alt="close-img">
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

<style>
.container {
  width: 1156px;
  height: 944px;
  border: solid black;
}

#circle {
  background-color: #8ABDBE;
  width: 100px;
  height: 100px;
  border-radius: 50px;
  position: relative;
  left: 1095px;
  top: 700px;
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
  /* position: relative; */
  position: fixed;
}

/* 닫기를 버튼으로 처리했을 때 style */
.btn {
  top: 261px;
  position: fixed;
  left: 1100px;
}

/* 닫기를 이미지로 처리했을 때 style */
.close-img {
  width: 30px;
  height: 30px;
  top: 265px;
  position: fixed;
  left: 1100px;
}
</style>