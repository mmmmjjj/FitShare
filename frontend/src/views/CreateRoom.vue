<template>
  <div class="create-container">
    <div id="new-room"> 
			<div id="dialog" class="jumbotron vertical-center">
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

          <!-- 쇼핑몰 사이트 -->
          <div class="row mall-container" :class="isMall ? '' : 'mall-margin-change'">
            <label><i class="bi bi-asterisk"></i>쇼핑몰 사이트</label>
            <div class="dropdown-wrapper">
              <div class="selected-item form-select" @click="isMallVisible = !isMallVisible">
                <p v-if="selectedItem">{{ selectedItem.name }}</p>
                <!-- <p v-else-if="filteredMalls.length === 0">직접 입력해주세요</p> -->
                <p v-else>쇼핑몰을 선택하세요</p>
              </div>
              <div v-if="isMallVisible" class="dropdown-popover">
                <!-- 단점 : 한글입력이 바로 안됨 -->
                <input v-model="searchQuery" class="search-mall-input" type="text" placeholder="검색하세요">
                <span v-if="filteredMalls.length === 0"><p class="text-center">검색결과 없음</p></span>
                <div class="options">
                  <ul class="scroll">
                    <li v-for="mall in filteredMalls" :key="mall.name" :value="mall.name" @click="selectItem(mall)">
                      {{mall.name}}
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
                <input v-model="mallName" class="form-control text-input shadow-none" type="text">
              </div>
              <div class="mall-input">
                <label><i class="bi bi-asterisk"></i>사이트 주소</label>
                <input v-model="mallUrl" class="form-control text-input shadow-none" type="text">
              </div>
            </div>
          </div>

          <!-- 공개범위 -->
          <div class="row private-container" :class="isPrivate ? 'private-margin-change' : ''">
            <label>공개범위</label>
            <span class="form-radio">
              <span @click="clickPublic()" class="radio-containers">
                <input class="form-check-input" type="radio" name="flexRadioDefault" id="public">
                <label class="form-check-label" for="public">공개</label>
              </span>
              <span @click="clickPrivate()" class="radio-containers">
                <input class="form-check-input" type="radio" name="flexRadioDefault" id="private">
                <label class="form-check-label" for="private" @click="clickPrivate()">비공개</label>
              </span>
            </span>
          </div>
          <!-- 비밀번호 입력 -->
          <div class="row password-container" v-show="isPrivate">  
						<label>비밀번호</label>
						<input v-model="password" class="form-control password-input shadow-none" type="password">
					</div>

          <!-- openvidu -->
          ------------ 임시 -------------
					<div>
						<label>Participant</label>
						<input v-model="myUserName" class="form-control" type="text" required>
					</div>
					<div>
						<label>Session</label>
						<input v-model="mySessionId" class="form-control" type="text" required>
					</div>

					<div class="text-center">
            <button class="btn create-btn shadow-none" @click="goToRoom()">생성하기</button>
            <button class="btn shadow-none" @click="goToMain()">취소</button>
					</div>        
				</div>
			</div>
		</div>
  </div>
</template>

<script>
import { reactive, ref, toRefs, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
// import axios from 'axios'

export default {
  name: 'CreateRoom',
  
  setup() {
    const router = useRouter()

    let counts = ref([1, 2, 3, 4, 5, 6])
    let selectedCnt = ref(null);
    let isCntVisible = ref(false);

    let searchQuery = ref('');
    let selectedItem = ref(null);  // 선택한 mall
    let isMallVisible = ref(false);

    let isPrivate = ref(false);
    let password = ref(null);

    const state = reactive({
      mySessionId: 'sessionA',
      myUserName: 'participant12', 

      mallOptions: [  // 서버 적용시 store에서 불러오는 방식으로 진행
        { id: 1, name: 'Nike', url: 'https://www.nike.com'},
        { id: 2, name: '무신사', url: 'https://www.musinsa.com'},
        { id: 3, name: '지그재그', url: 'https://www.zigzag.com'},
        { id: 4, name: 'Zara', url: 'https://www.zara.com'},
        { id: 5, name: 'next', url: 'https://www.next.com'},
        { id: 6, name: 'ABC', url: 'https://www.abc.com'},
        { id: 7, name: '지마켓', url: 'https://www.gmarket.com'},
      ], 
      isMall: true,  // 찾고자 하는 쇼핑몰이 리스트에 있는지 없는지 
      mallName: null,
      mallUrl: null,
    })

    // select dropdown method
    function selectCnt(cnt) {
      selectedCnt.value = cnt;
      isCntVisible.value = false;
    }

    function selectItem(mall) {
      selectedItem.value = mall;
      isMallVisible.value = false;
    }

    // 검색 기능 
    const filteredMalls = computed(() => {
      const query = searchQuery.value.toLowerCase();
      if (searchQuery.value === '') {
        return state.mallOptions
      }
      return state.mallOptions.filter((mall) => {
        return Object.values(mall).some((word => String(word).toLowerCase().includes(query)))
      })
    })
    
    // 바깥영역 클릭으로 dropdown 닫기
    document.addEventListener('click', function(e){
      if (isMallVisible.value == false ) {
        console.log('')
      } else {
        if (e.target.className !== 'dropdown-popover' && e.target.className !== 'selected-item form-select' && e.target.className !== '' && e.target.className !== 'search-mall-input' ) {
          isMallVisible.value = false
        }
      }
      if (isCntVisible.value == false ) {
        return
      } else {
        if (e.target.className !== 'dropdown-popover' && e.target.className !== 'selected-item form-select' && e.target.className !== '' && e.target.className !== 'search-mall-input' ) {
          isCntVisible.value = false
        }
      }
    })
    
    // 쇼핑몰 입력부분 생성 
    watch(filteredMalls, (filteredMalls) => {
      if (filteredMalls.length === 0) {
        state.isMall = false;
        selectedItem.value = null
      } else {
        state.isMall = true;
      }
    }) 

    // 기본 method
    const clickPrivate = () => {
      isPrivate.value = true
    }

    const clickPublic = () => {
      isPrivate.value = false
    }

    // 쇼핑룸 생성
    const goToRoom = async () => {
      // if (selectedItem) {
      //   const mallId = selectedItem.id
      // } else {
      //   const mallId = null
      // }
      // const roomData = {  
      //   userId: ''  // username(or id) 필요 => DB host_id 저장 
      //   mallId: mallId,  // null
      //   mallName: state.mallName, // null
      //   mallUrl: state.mallUrl,  // null
      //   count: selectedCnt,
      //   isPrivate: isPrivate,  // T/F
      //   password: password,  // null
      // }
      // axios({
      //   method : 'post',
      //   url: 'room-api 추가',
      //   data: roomData,
      // })
      //   .then(res => {
      //     console.log(res.data)  // sessionId(=roomId), mall url 
      //     router.push({ name: 'ShoppingRoom', params: { roomId: res.data.sessionId, mallUrl: res.data.mall_url }})
      //   })
      //   .catch(err => console.log(err))


      router.push({ name: 'ShoppingRoom', params: { roomId: state.mySessionId, myUserName: state.myUserName } })
    }

    const goToMain = () => {
      router.push({ name: 'Main' })
    }

    return { 
      clickPrivate, goToRoom, goToMain, clickPublic, ...toRefs(state), counts,
      searchQuery, selectedCnt ,selectedItem, isMallVisible, isCntVisible, filteredMalls,
      selectItem, selectCnt, isPrivate, password,
    }
  }
}
</script>

<style scoped>
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

.create-container {
  margin: 0px 142px 0;
}

#new-room {
  margin: 0px 187px 100px;
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