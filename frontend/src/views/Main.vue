<template>
  <div>
    <Navbar/>
    <div id="container">
      <div class="container-box">
        <!-- 쇼핑룸, host옷장 -->
        <div class="row">
          <shopping-room-list></shopping-room-list>
          <div class="host-container">
            <host-closet class="host-closet"></host-closet>
            <button class="btn shadow-none" 
              v-if="selectedShoppingRoom" 
              @click="selectedShoppingRoom.isPrivate ? isPrivate=true : goToRoom()"
            >
              입장하기
            </button>
          </div>
        </div>

        <!-- 버튼 클릭 시 친구 아이콘 html / css tab 버전 -->
        <button class="btn friend-btn" @click="OpenTab">
          <img class="friend_icon" src="@/assets/friend_icon.png" alt="friend_icon">
        </button>
        <div v-if="status">
          <friend class="tab-box" @close-tab="CloseTab"></friend>
          <button type="button" class="btn-close friend-close" @click="CloseTab"></button>
        </div>

        <!-- 비밀번호 모달 -->
        <div class="modal overlay" :class="isPrivate ? 'show-modal' : 'hide-modal'" tabindex="-1">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header pt-3 pb-0">
                <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal" @click="closeModal"></button>
              </div>
              <i class="fas fa-lock text-center my-3"></i>
              <div class="modal-body">
                <input type="password" v-model="inputPassword" placeholder="비밀번호를 입력해주세요">
                <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
                <span class="d-flex justify-content-center">
                  <button class="btn shadow-none" @click="checkPassword">입장하기</button>              
                  <button class="btn shadow-none" @click="closeModal" data-bs-dismiss="modal">취 소</button>
                </span>
              </div>
            </div>
          </div>
        </div>
        <!-- Alert 알람 -->
        <div v-if="alert" class="alert alert-warning" role="alert">
          <i class="bi bi-exclamation-triangle-fill"></i>인원수가 초과된 쇼핑룸입니다. 다른 쇼핑룸을 이용해주세요!
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue';
import Friend from '@/components/main/Friend.vue';
import ShoppingRoomList from '@/components/main/ShoppingRoomList.vue';
import HostCloset from '../components/main/HostCloset.vue';
import { ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import axios from '../api/axios';

export default {
  name: 'Main',
  components: { 
    Navbar,
    Friend,
    HostCloset,
    ShoppingRoomList,
  },

  setup() {
    const router = useRouter();
    const store = useStore();
    
    const status = ref(false);
    let isPrivate = ref(false);
    let inputPassword = ref(null);  
    let errorMessage = ref('');
    let alert = ref(false);

    const OpenTab = () => {
      status.value = true
    };

    const CloseTab = () => {
      status.value = false
    };

    // host 옷장
    const selectedShoppingRoom = computed(() => {
        return store.state.room.selectedShoppingRoom
    });
    
    // 입장하기
    const goToRoom = () => {
      const maxCnt = selectedShoppingRoom.value.maxParticipantCount;
      const cnt = selectedShoppingRoom.value.participantCount;
      if (maxCnt <= cnt) {
        alert.value = true
        return;
      }

      axios.get(`shopping-rooms/${selectedShoppingRoom.value.shoppingRoomId}`)
        .then(res => {
          const data = res.data.data 
          router.push({ name: 'ShoppingRoom', params: { roomId: data.shoppingRoomId, token: data.token, mallUrl: data.shoppingRoomUrl, hostId: data.hostId }}) 
        })
        .catch(err => {
          console.log(err.response)
          if (err.response.status == 403) alert.value = true;
        })
    }
    
    watch(alert, () => {
      if (alert.value) setTimeout(() => alert.value = false, 3000);
    });
    
    const checkPassword = () => {
      axios.post(`shopping-rooms/${selectedShoppingRoom.value.shoppingRoomId}/validate`, { password: inputPassword.value })
        .then(res => {
          const data = res.data.data;
          if (data) { 
            goToRoom()
          } else {
            errorMessage.value = '비밀번호가 일치하지 않습니다.'
          }
        })
        .catch(err => console.log(err.response))
    }

    const closeModal = () => {
      isPrivate.value = false;
      inputPassword.value = null;
      if (errorMessage.value) errorMessage.value = '';
    }

    return {
      status, selectedShoppingRoom, isPrivate, inputPassword, errorMessage, alert,
      OpenTab,CloseTab, goToRoom, checkPassword, closeModal,
    }
  }
}
</script>

<style scoped>
.alert {
  position: absolute;
  top: 80px;
  right: 5px;
  padding: 16px 20px;
  animation: slide 0.4s;
}

@keyframes slide {
  from{
    right: -200px;
  }
  to{
    right: 5px;
  }
}

.alert i {
  font-size: 16px;
  margin-right: 20px;
}

#container {
  display: flex;
  justify-content: center;
}

.container-box {
  width: 1200px;
  position: relative;
  padding: 0;
  margin: 80px 153px 0 142px;
}

.row {
  display: flex;
  justify-content: space-between;
}

.host-container {
  width: 543px;
  height: 775px;
  padding: 0;
}

.host-container button {
  width: 184px;
  height: 54px;
  margin: 26px 175px 0;
  background-color: #8ABDBE;
  border-radius: 20px;
  font-weight: bold;
  font-size: 20px;
}

.host-container button:hover {
  background-color: #75b3b4;
}

/* host 옷장 */
.host-closet {
  width: 543px;
  height: 595px;
  background-color: #fdfaf3;
  z-index: 0;
  position: relative;
  border-radius: 16px;
  border: 3px solid #d5d1c6;
  box-shadow: 0px 5px 15px rgb(207 206 206);
}

/* modal */
.show-modal {
  display: block;
}

.hide-modal {
  display: none;
}

.modal-dialog {
  margin-top: 380px;
}

.modal-content {
  background-color: #D3E2E7;
  border-radius: 20px;
  border: none;
}

.modal-title {
  font-size: 20px;
  margin: 5px 0 0 176px;
  color: #1B4D50;
}

i {
  font-size: 70px;
  color: #ad1d4e;
  margin-top: 0;
}

.modal-body {
  display: flex;
  flex-direction: column;
}

.modal-body input {
  border-radius: 5px;
  border: none;
  width: 260px;
  height: 35px;
  margin-left: 103px;
  text-align: center;
}

.modal-body input:focus {
  outline: none;
}

.modal-body button {
  width: 100px;
  margin: 25px 10px 20px;
  border-radius: 10px;
  color: #FDFAF3;
  background-color: #696b6e;
}

.modal-body button:hover {
  color: white;
  background-color: #898b8f;
}

.error-message {
  margin-top: 3px;
  margin-bottom: 0;
  color: red;
  text-align: center;
  font-size: 16px;
}

.overlay {
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 10;
  background-color: rgba(0, 0, 0, 0.377);
}

/* 친구 */
.friend-btn {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  border: none;
  position: fixed;
  right: 100px;
  bottom: 40px;
  background-color: #8ABDBE;
  
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
  right: 80px;
  bottom: 30px;
  position: fixed;
  z-index: 2;
}

.friend-close {
  position: fixed;
  right: 447px;
  bottom: 593px;
}

.btn { 
  box-shadow: 0 0 10px 1px #438185; 
}
</style>