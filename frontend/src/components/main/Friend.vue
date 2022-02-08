<template>
  <div>
    <div class="tabs">
      <input id="friendlist" type="radio" name="tab_item" checked @click="GetFriendList">
      <label class="tab_item" for="friendlist">친구목록</label>
      <input id="friendmake" type="radio" name="tab_item">
      <label class="tab_item" for="friendmake">친구추가</label>
      <input id="friendcheck" type="radio" name="tab_item" @click="CheckFriendRequest">
      <label class="tab_item" for="friendcheck">요청확인</label>
      
      <!-- 친구목록 component -->
      <friend-list-tab class="tab_content" id="friendlist_content"></friend-list-tab>
      <!-- 친구추가 component -->
      <friend-make-tab class="tab_content" id="friendmake_content"></friend-make-tab>
      <!-- 친구요청확인 component -->
      <friend-check-tab class="tab_content" id="friendcheck_content" :checkRequests="state.checkRequests"></friend-check-tab>

    </div>
  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue'
import FriendListTab from '@/components/main/FriendListTab.vue'
import FriendMakeTab from '@/components/main/FriendMakeTab.vue'
import FriendCheckTab from '@/components/main/FriendCheckTab.vue'
import axios from 'axios'
import { useStore } from 'vuex'


export default {
  name: 'Friend',
  components: {
    FriendListTab,
    FriendMakeTab,
    FriendCheckTab,
  },
  setup() {
    const status = ref(true)

    const state = reactive({
      friends: [],
      friendLists: [],
      checkRequests: [],
    })

    const CloseTab = () => {
      status.value = false
    }

    // store
    const store = useStore()

    // 친구목록 클릭 시 친구 목록 받아오는 Axios
    const GetFriendList = () => {
      axios({
        method: 'GET',
        url: 'http://i6a405.p.ssafy.io:8081/api/v1/friends',
        headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw` }
      })
        .then(res => {
          console.log(res)
          state.friendLists = res.data.data
        })
        .then(res => {
          console.log(res)
          const friend = state.friendLists
          console.log(state.friendLists)
          console.log(friend)
          store.dispatch('getfriendsbyclick', friend)
        })
    }

    const stateFriends = computed(() => {
      return store.state.friends
    })

    // 요청확인 클릭 시 요청 목록 받아오는 Axios
    const CheckFriendRequest = () => {
      axios({
        method: 'GET',
        url: 'http://i6a405.p.ssafy.io:8081/api/v1/friends/requests',
        headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw` }
      })
        .then(res => {
          console.log(res)
          state.checkRequests = res.data.data
        })
    }

    return {
      status,
      CloseTab,
      state,
      GetFriendList,
      CheckFriendRequest,
      stateFriends,
    }
  }
}
</script>

<style scoped>
/* 탭 전체 스타일 */
.tabs {
  background-color: #FDFAF3;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  width: 387px;
  height: 564px;
  margin: 0 auto;
}

/* 탭 스타일 */
.tab_item {
  width: calc(100%/3);
  height: 50px;
  border-bottom: 3px solid #333333;
  background-color: #8ABDBE;
  line-height: 50px;
  font-size: 16px;
  text-align: center;
  color: #333333;
  display: block;
  float: left;
  font-weight: bold;
  transition: all 0.2s ease;
}
.tab_item:hover {
  opacity: 0.75;
}

/* 라디오 버튼 UI삭제*/
input[name="tab_item"] {
  display: none;
}

/* 탭 컨텐츠 스타일 */
.tab_content {
  display: none;
  padding: 15px 40px 0;
  clear: both;
  height: 400px;
  overflow-y: scroll;
}

.tab_content::-webkit-scrollbar {
  width: 10px;
}


/* 선택 된 탭 콘텐츠를 표시 */
#friendlist:checked ~ #friendlist_content,
#friendmake:checked ~ #friendmake_content,
#friendcheck:checked ~ #friendcheck_content {
  display: block;
}

/* 선택된 탭 스타일 */
.tabs input:checked + .tab_item {
  background-color: #333333;
  color: #fff;
}

</style>