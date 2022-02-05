<template>
  <div>
    <Navbar/>
    <div class="container">
      <!-- 쇼핑룸, host옷장 -->
      <div class="row">
        <shopping-room-list :shopping-room-list="shoppingRoomList" @change-host-closet="changeHostCloset"></shopping-room-list>
        <div class="host-container">
          <!-- <host-closet class="host-closet" :selected-shopping-room="selectedShoppingRoom"></host-closet> -->
          <host-closet class="host-closet" :selected-shopping-room="selectedShoppingRoom"></host-closet>
          <button class="btn shadow-none" @click="goToRoom()">입장하기</button>
        </div>
      </div>

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

    <!-- 비밀번호 입력 모달 -->
    <div class="modal overlay" tabindex="-1" :class="isPrivate ? 'show-modal' : 'hide-modal'">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <p class="modal-title fw-bold">비공개 쇼핑룸</p>
            <button type="button" class="btn-close shadow-none ms-2" data-bs-dismiss="modal" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <input type="password" v-model="inputPassword" placeholder="비밀번호를 입력해주세요">
            <p v-if="errorMessage" class="error-message">비밀번호 오류! 다시 입력해주세요</p>
            <span class="d-flex justify-content-center">
              <button class="btn shadow-none" @click="checkPassword">입장하기</button>              
              <button class="btn shadow-none" @click="closeModal" data-bs-dismiss="modal">취 소</button>
            </span>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import Friend from '@/components/main/Friend.vue'
import ShoppingRoomList from '@/components/main/ShoppingRoomList.vue'
import HostCloset from '../components/main/HostCloset.vue'
import { reactive, ref, toRefs } from 'vue'
import { useRouter } from 'vue-router';
// import { useStore } from "vuex";
import axios from 'axios'

export default {
  name: 'Main',
  
  components: { 
    Navbar,
    Friend,
    HostCloset,
    ShoppingRoomList,
  },

  setup() {
    const router = useRouter()
    // const store = useStore();
    
    const status = ref(false)
    const shoppingRoomList = ref([])
    let selectedShoppingRoom = ref({})
    
    let isPrivate = ref(false)
    let inputPassword = ref(null)  
    let errorMessage = ref(false)

    const state = reactive({ 
      confirmPassword: null,  // 비밀번호 api 사용시 필요없음
      roomId: null,
      token: null, 
      mallUrl: null,
    })

    // methods
    const OpenTab = () => {
      status.value = true
    };

    const CloseTab = () => {
      status.value = false
    };
    
    // 쇼핑룸목록 불러오기 
    function getShoppingRoomList () {
        axios({
          method: 'get',
          url: 'http://i6a405.p.ssafy.io:8081/api/v1/shopping-rooms/',
          headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw` }
        })
          .then(res => {
            shoppingRoomList.value = res.data.data
            selectedShoppingRoom.value = res.data.data[0]  // 첫번째 쇼핑룸
          })
    }

    // 선택한 host 옷장으로 변경
    function changeHostCloset(roomInfo) {
      selectedShoppingRoom.value = roomInfo
    }

    // 입장하기
    function goToRoom() {
      isPrivate.value = false
      
      axios({
        method : 'get',
        url: 'http://i6a405.p.ssafy.io:8081/api/v1/shopping-rooms/54',  // roomId는 임시로 바꿔서 테스트
        // url: `http://i6a405.p.ssafy.io:8081/api/v1/shopping-rooms/${selectedShoppingRoom.shoppingRoomId}`,
        headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw` }
      })
        .then(res => {
          const data = res.data.data 
          console.log(res.data.data)
          router.push({ name: 'ShoppingRoom', params: { roomId: data.shoppingRoomId, token: data.token, mallUrl: data.ShoppingRoomUrl }}) 

          // isPrivate 여부 => F: token, roomId, mallurl (username 필요X) / T: isPrivate, password
          // if (!data.isPrivate) {  // public
          //   router.push({ name: 'ShoppingRoom', params: { roomId: data.shoppingRoomId, token: data.token, mallUrl: data.ShoppingRoomUrl }})
          // } else { 
          //   isPrivate.value = true
          //   confirmPassword.value = data.password  // 비번 API X 경우
          // }
        })
        .catch(err => console.log(err))
    }

    // 비밀번호 확인
    function checkPassword() {
      // password를 goToRoom에서 받는 경우 
      // if (password.value == state.confirmPassword) {
      //   isPrivate.value = false
      //   router.push({ name: 'ShoppingRoom', params: { roomId: state.roomId, token: state.token, mallUrl: state.mall_url }})
      // } else {
      //   errorMessage.value = true
      //   password.value = null
      // }

      // API 사용시 
      // axios({
      //   method: 'post',
      //   url: '',
      //   data: { password: inputPassword.value },
      //   headers: { Authorization: `Bearer 토큰`}
      // })
      //   .then(res => {
      //     const data = res.data.data
      //     router.push({ name: 'ShoppingRoom', params: { roomId: data.shoppingRoomId, token: data.token, mallUrl: data.ShoppingRoomUrl }}) 
      //   })
    }

    function closeModal() {
      isPrivate.value = false
      inputPassword.value = null
    }

    // created
    getShoppingRoomList()

    // created (store 사용시 )
    // function getList() {
    //   console.log('start')
    //   store.dispatch('mall/loadShoppingMallList')
    // }
    // getList()

    return {
      status, shoppingRoomList, selectedShoppingRoom, isPrivate, inputPassword, errorMessage, ...toRefs(state),
      OpenTab,CloseTab,
      changeHostCloset,getShoppingRoomList,
      goToRoom, checkPassword, closeModal,
      // getList, 
    }
  }
}
</script>

<style scoped>
.container {
  width: 1156px;
  height: 775px;
  position: relative;
  padding: 0;
  margin: 87px 142px 89px;
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

/* host 옷장 style */
.host-closet {
  width: 543px;
  height: 595px;
  background-color: #FDFAF3;
  /* left: 582px; */
  z-index: 0;
  position: relative;
  border-radius: 16px;
}

/* modal */
.show-modal {
  display: block;
}
.hide-modal {
  display: none;
}

.modal-dialog {
  margin-top: 400px;
}

.modal-content {
  background-color: #D3E2E7;
  border-radius: 20px;
  border: none;
}

.modal-title {
  font-size: 20px;
  margin: 5px 0 0 176px;
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
  margin: 25px 10px 0;
  border: 2px solid white;
  border-radius: 10px;
  font-weight: bold;
  color: white;
}

.modal-body button:hover {
  background-color: rgba(170, 169, 169, 0.116);
}

.error-message {
  margin-top: 0;
  margin-bottom: 0;
  color: #589B9A;
  text-align: center;
  font-size: 14px;
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
  position: relative;
  left: 1095px;
  bottom: 49px;
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