<template>
  <div>
    <!-- 친구 이름 검색 input-box -->
    <input
      class="input-box"
      type="text"
      v-model="SearchFriend"
      placeholder="이름 검색"
      @keyup="SearchFriendEmail"
    >

    <!-- 친구 목록 -->
    <div v-for="(friend, index) in stateFriends" :key="friend.id" class="d-flex mt-3">
      <img :src="friend.profileImg" alt="profile-img">
      <div class="ms-3 d-flex flex-column">
        <div class="name-box">
          {{ friend.name }}
        </div>
        <div class="d-flex mt-2 button-box">
          <button class="" @click="DeleteFriend(stateFriends, index)">친구삭제</button>
          <button class="ms-3">채팅하기</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue'
import axios from 'axios'
import { useStore } from "vuex";
import { useCookies } from 'vue3-cookies'

export default {
  name: 'FriendListTab',
  components: {

  },
  setup() {
    const state = reactive({
      friends: [],
      friendLists: [],
      friendEmail: [],
    })

    // accesstoken 받아오기
    const { cookies } = useCookies() 

    const setToken = () => {
      const token = cookies.get('accessToken')
      const config = {
        Authorization: `Bearer ${token}`
      }
      return config
    }

    const SearchFriend = ref('')

    // store test
    const store = useStore()

    // store의 state에서 받아와서 저장해두기
    const stateFriends = computed(() => {
      return store.state.friend.friends
    })

    // 친구 삭제
    const DeleteFriend = (stateFriends, index) => {
      axios({
        method: 'DELETE',
        url: `http://i6a405.p.ssafy.io:8081/api/v1/friends/${stateFriends[index].id}`,
        headers: setToken(),
        data: {"friendId": stateFriends[index].id}
        })
        .then(res => {
          console.log(stateFriends[index].id)
          console.log(res)
          stateFriends.splice(index, 1)
        })
    }

    // created , store에서 받아오기
    const GetFriendList = () => {
      axios({
        method: 'GET',
        url: 'http://i6a405.p.ssafy.io:8081/api/v1/friends',
        headers: setToken()
      })
        .then(res => {
          console.log(res)
          state.friendLists = res.data.data
        })
        .then(res => {
          console.log(res)
          const friend = state.friendLists
          console.log('성공')
          console.log(state.friendLists)
          console.log(friend)
          store.dispatch('friend/getfriends', friend)
        })
    }

    GetFriendList()

    // 이름으로 server에 요청
    const SearchFriendEmail = () => {
      axios({
        method: 'GET',
        url: `http://i6a405.p.ssafy.io:8081/api/v1/friends/${SearchFriend.value}`,
        headers: setToken()
      })
        .then(res => {
          console.log(res)
          state.friendEmail = res.data.data
          console.log(state.friendEmail)
        })
        .then(res => {
          console.log(res)
          const friendbyname = state.friendEmail
          store.dispatch('friend/getfriendsbyname', friendbyname)
        })
    }


    return {
      state,
      SearchFriend,
      GetFriendList,
      DeleteFriend,
      stateFriends,
      SearchFriendEmail,
      setToken,
    }
  }
}
</script>

<style scoped>
.profile-img {
  width: 60px;
  height: 60px;
}

.input-box {
  width: 301px;
  border-radius: 20px;
  text-align: center;
}

input::placeholder {
  text-align: center;
}

.name-box {
  font-size: 16px;
}

.button-box {
  font-size: 12px;
}

.button-box > button {
  background: #FDFAF3;
  font-size: 12px;
  border-radius: 15px;
  width: 90px;
}

img {
  width: 60px;
  height: 60px;
  border-radius: 30px;
}
</style>