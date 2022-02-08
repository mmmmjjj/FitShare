<template>
  <div class="create-container">
    <div id="new-room"> 
			<div id="dialog" class="vertical-center">
        <div class="d-flex flex-row title">
          <i class="bi bi-house-door me-4"></i>
          <h1>New Room</h1>
        </div>
				<div class="form-group">
          <!-- 입장가능 인원 -->
          <div class="row">
            <label class="counts"><i class="bi bi-asterisk"></i>입장 가능 인원</label>
            <div class="dropdown-wrapper">
              <div class="selected-item form-select" @click="isCntVisible = !isCntVisible">  <!-- :class="isFocused ? 'focus-outline': '' " , changeOutline() -->
                <p v-if="selectedCnt">{{ selectedCnt }}</p>
                <p v-else>인원수를 선택하세요</p>
              </div>
              <div v-show="isCntVisible" class="dropdown-popover">
                <div class="options">
                  <ul class="scroll cnt-ul">
                    <li v-for="(cnt, index) in counts" :key="index" :value="cnt" @click="selectCnt(cnt)">{{ cnt }}</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>

          <!-- 쇼핑몰 사이트 검색 -->
          <div class="row mall-container" :class="isMall ? '' : 'mall-margin-change'">
            <label><i class="bi bi-asterisk"></i>쇼핑몰 사이트</label>
            <div class="dropdown-wrapper">
              <div class="selected-item form-select" @click="isMallListVisible = !isMallListVisible">
                <p v-if="selectedItem">{{ selectedItem.name }}</p>
                <p v-else>쇼핑몰을 선택하세요</p>
              </div>
              <div v-if="isMallListVisible" class="dropdown-popover">
                <input @keyup="searchMall()" v-model="searchQuery" class="search-mall-input" type="text" placeholder="검색하세요">
                <span v-if="searchedMalls.length === 0"><p class="text-center">검색결과 없음</p></span>
                <div class="options">
                  <ul class="scroll">
                    <li v-for="mall in searchedMalls" :key="mall.id" :value="mall.name" @click="selectItem(mall)">
                      {{ mall.name }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <!-- 사이트 직접 입력 -->
          <div class="d-flex flex-row-reverse" v-if="!isMall">
            <div class="mall-input-container">
              <div class="mall-input mb-2">
                <label><i class="bi bi-asterisk"></i>사이트 이름</label>
                <input v-model="inputMallName" class="form-control text-input shadow-none" type="text">
              </div>
              <div class="mall-input">
                <label><i class="bi bi-asterisk"></i>사이트 주소</label>
                <input v-model="inputMallUrl" class="form-control text-input shadow-none" type="text">
              </div>
            </div>
          </div>

          <!-- 공개범위 설정 -->
          <div class="row private-container" :class="isPrivate ? 'private-margin-change' : ''">
            <label>공개범위</label>
            <span class="form-radio">
              <span @click="selectPublic()" class="radio-containers">
                <input class="form-check-input" type="radio" name="flexRadioDefault" checked="checked" id="public">
                <label class="form-check-label" for="public">공개</label>
              </span>
              <span @click="selectPrivate()" class="radio-containers">
                <input class="form-check-input" type="radio" name="flexRadioDefault" id="private">
                <label class="form-check-label" for="private" >비공개</label>
              </span>
            </span>
          </div>
          <!-- 비밀번호 입력 -->
          <div class="row password-container" v-show="isPrivate">  
						<label>비밀번호</label>
						<input v-model="password" class="form-control password-input shadow-none" type="password">
					</div>

					<div class="text-center">
            <button class="btn create-btn shadow-none" @click="makeRoom()">생성하기</button>
            <button class="btn shadow-none" @click="goToMain()">취소</button>
					</div>        
				</div>
			</div>
		</div>
  </div>
</template>

<script>
import { reactive, ref, toRefs, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'
import axios from 'axios'

export default {
  name: 'CreateRoom',
  
  setup() {
    const router = useRouter()
    const store = useStore()

    let counts = ref([1, 2, 3, 4, 5, 6])
    let selectedCnt = ref(null);
    let isCntVisible = ref(false);

    let searchQuery = ref('');
    let selectedItem = ref(null);  // 선택한 mall
    let isMallListVisible = ref(false);

    let isPrivate = ref(false);
    let password = ref(null);
    
    // 검색된 쇼핑몰 리스트
    let searchedMalls = ref({})  // {id: 2, logo: null, name: '아디다스', url: 'www.xxx.com'}

    // 테스트용 토큰
    let token = ref('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw')

    const state = reactive({
      shoppingMallList: [],  // 
      isMall: true,  // 찾고자 하는 쇼핑몰의 리스트 내 존재여부
      inputMallName: null,
      inputMallUrl: null,
    })

    // select dropdown method
    function selectCnt(cnt) {
      selectedCnt.value = cnt;
      isCntVisible.value = false;
    }

    function selectItem(mall) {
      selectedItem.value = mall;
      isMallListVisible.value = false;
    }
    
    // 쇼핑몰 검색
    function searchMall() {
      // axios.get(`${store.state.url}/v1/shopping-malls?keyword=${searchQuery.value}`)
      axios.get(`${store.state.url}/v1/shopping-malls`, { params: { keyword: searchQuery.value } })
        .then(res => {
          searchedMalls.value = res.data.data
          console.log(searchedMalls.value)
        })
    }

    // 쇼핑몰 입력부분 생성 
    watch(searchedMalls, (searchedMalls) => {
      if (searchedMalls.length === 0) {
        state.isMall = false;
        selectedItem.value = null;
      } else {
        state.isMall = true;
      }
    }) 

    // 바깥영역 클릭으로 dropdown 닫기
    document.addEventListener('click', function(e){
      if (isMallListVisible.value == true) {
        if (e.target.className !== 'dropdown-popover' && e.target.className !== 'selected-item form-select' && e.target.className !== '' && e.target.className !== 'search-mall-input' ) {
          isMallListVisible.value = false
        }
      } 
      if (isCntVisible.value == true) {
        if (e.target.className !== 'dropdown-popover' && e.target.className !== 'selected-item form-select' && e.target.className !== '' && e.target.className !== 'search-mall-input' ) {
          isCntVisible.value = false
        }
      } 
    })
    
    // 기본 method
    const selectPrivate = () => {
      isPrivate.value = true
    }

    const selectPublic = () => {
      isPrivate.value = false
    }

    // 쇼핑룸 생성
    function makeRoom () {
      let mallId = null
      if (selectedItem.value) {
        mallId = selectedItem.value.id
        state.inputMallName = null
        state.inputMallUrl = null
      } 

      const roomData = {  
        customShoppingMall: !state.isMall,
        participantCount: selectedCnt.value,
        password: password.value,  // null
        private: isPrivate.value,  
        shoppingMallId: mallId,  // null 
        shoppingMallName: state.inputMallName, 
        shoppingMallUrl: state.inputMallUrl,  
      }
      console.log(roomData)

      axios({
        method : 'post',
        url: `${store.state.url}/v1/shopping-rooms/`,
        data: roomData,
        headers: { Authorization : `Bearer ${token.value}` }
      })
        .then(res => {
          console.log(res.data.data)
          const data = res.data.data
          router.push({ name: 'ShoppingRoom', params: { roomId: data.shoppingRoomId, token: data.token, mallUrl: data.shoppingRoomUrl }}) 
        })
        .catch(err => console.log(err))
    }

    const goToMain = () => {
      router.push({ name: 'Main' })
    }

    return { 
      selectPrivate, makeRoom, goToMain, selectPublic, token,
      ...toRefs(state), counts, searchQuery, selectedCnt ,selectedItem, isMallListVisible, 
      isCntVisible, selectItem, selectCnt, isPrivate, password,
      searchedMalls, searchMall,
    }
  }
}
</script>

<style scoped>
.create-container {
  margin: 0px 142px 0;
  display: flex;
  justify-content: center;
}

#new-room {
  margin: 0px 187px 100px;
  width: 782px;
  height: 951px;
}

.dropdown-wrapper {
  width: 454px;
  height: 35px;
  position: relative;
  padding: 0;
}

.form-select {
  width: 454px;
  height: 35px;
}

.selected-item {
  height: 35px;
  padding: 5px 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1rem;
}

p {
  margin: 0;  
}

.bi-asterisk {
  font-size: 10px;
  color: red;
  margin-right: 4px;
}

/* 입력X */
.mall-container {
  margin-top: 160px;
  margin-bottom: 197px;
}
/* 입력 필요 */
.mall-margin-change {
  margin-top: 160px;
  margin-bottom: 44px;
}

.dropdown-popover {
  position: absolute;
  border: 1px solid lightgray;
  top: 35px;
  left: 0;
  right: 0;
  background-color: #fff;
  max-width: 100%;
  min-height: 40px;
  padding: 10px;
  box-shadow: 0 0 6px #D3E2E7;
}

.search-mall-input {
  width: 100%;
  height: 30px;
  border: 1px solid lightgray;
  border-radius: 2px;
  font-size: 16px;
  padding-left: 8px;
}

.options {
  width: 100%;
}

ul {
  list-style: none;
  text-align: left;
  padding-left: 8px;
  max-height: 115px;
  overflow-y: scroll;
}
.cnt-ul {
  max-height: 100px;
}

.scroll::-webkit-scrollbar {
  width: 7px;
}
.scroll::-webkit-scrollbar-track {
  background-color: transparent;
}
.scroll::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #D3E2E7;
}

li {
  width: 100%;
  border-bottom: 1px solid lightgray;
  cursor: pointer;
  font-size: 16px;
}

li:hover {
  width: 100%;
  border-bottom: 1px solid lightgray;
  background-color: lightgray;
  cursor: pointer;
  font-size: 16px;
}

input:focus {
  outline:none;
  box-shadow: none;
}

.title {
  font-size: 36px;
  margin-bottom: 60px;
}

#dialog {
  margin: 27px 30px 0;
}

.row {
  display: flex;
  justify-content: space-between;
  height: 35px;
  line-height: 35px;
}

label {
  font-size: 20px;
  width: 170px;
  height: 38px;
  line-height: 38px;
}

/* 비번 없음 */
.private-container {
  margin-top: 75px;
  margin-bottom: 159px;
}

/* 비번 입력시 */
.private-margin-change {   
  margin-top: 75px;
  margin-bottom: 54px;
}

.form-radio {
  display: flex;
  flex-direction: row;
  margin-right: 192px;
  width: 350px;
}

.radio-containers {
  display: flex;
  flex-direction: row;
}

.form-check-label {
  font-size: 20px;
  width: 60px;
  margin: 0;
}

.form-check-input {
  margin: 9px 18px 5px 76px;
}

.mall-input-container {
  width: 530px;
  display: flex;
  flex-direction: column;
}

.mall-input {
  display: flex;
  flex-direction: row;
}

.mall-list-input {
  width: 454px;
}

.text-input {
  width: 324px;
  height: 35px;
}

.password-container {
  margin-bottom: 70px;
}

.password-input {
  width: 454px;
  height: 35px;
}

button {
  width: 330px;
  height: 50px;
  flex-grow: 0;
  border-radius: 12px;
  border: solid 2px #000;
}

.create-btn {
  margin: 0 34px 0 0;
}
</style>