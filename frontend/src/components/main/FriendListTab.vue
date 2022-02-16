<template>
  <div>
    <!-- 친구 이름 검색 input-box -->
    <input class="input-box" type="text" v-model="SearchFriend" placeholder="이름 검색" @keyup="SearchFriendEmail" />

    <!-- 친구 목록 -->
    <div v-for="(friend, index) in stateFriends" :key="friend.id" class="d-flex mt-3">
      <img :src="friend.profileImg" alt="profile-img" class="profile-img" />
      <div class="ms-3 d-flex flex-column">
        <div class="name-box">
          {{ friend.name }}
        </div>
        <div class="d-flex mt-2 button-box">
          <button class="" @click="DeleteFriend(stateFriends, index)">친구삭제</button>
          <button type="button" class="ms-2" @click="openChatting(friend.id, friend.name)">채팅하기</button>
        </div>
      </div>
    </div>

    <friend-chatting v-if="state.chattingStatus" class="chattingRoom" :friendId="state.friendId" :friendName="state.friendName"></friend-chatting>
    <button v-if="state.chattingStatus" @click="openChatting" class="btn-close btn-secondary closeChatting"></button>

  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue';
import axios from '../../api/axios';
import { useStore } from "vuex";
import FriendChatting from './FriendChatting.vue';

export default {
  name: 'FriendListTab',
  components: {
    FriendChatting,
  },
  setup() {
    const state = reactive({
      friends: [],
      friendLists: [],
      friendEmail: [],
      chattingStatus: false,
      friendId: '',
      friendName: '',
    });

    const SearchFriend = ref('');
    const store = useStore();

    // store의 state에서 받아와서 저장해두기
    const stateFriends = computed(() => {
      return store.state.friend.friends
    })

    // 친구 삭제
    const DeleteFriend = (stateFriends, index) => {
      axios({
        method: 'DELETE',
        url: `friends/${stateFriends[index].id}`,
        data: { friendId: stateFriends[index].id }
        })
        .then(() => {
          stateFriends.splice(index, 1)
        })
    };

    // created , store에서 받아오기
    const GetFriendList = () => {
      axios({
        method: 'GET',
        url: 'friends',
      })
        .then(res => {
          state.friendLists = res.data.data
        })
        .then(() => {
          const friend = state.friendLists
          store.dispatch('friend/getfriends', friend)
        })
    };

    GetFriendList()

    // 이름으로 server에 요청
    const SearchFriendEmail = () => {
      axios({
        method: 'GET',
        url: `friends/${SearchFriend.value}`,
      })
        .then((res) => {
          state.friendEmail = res.data.data;
          console.log(state.friendEmail);
        })
        .then(() => {
          const friendbyname = state.friendEmail;
          store.dispatch('friend/getfriendsbyname', friendbyname);
        })
    }

    const openChatting = (friendId, friendName) => {
      state.chattingStatus = !state.chattingStatus;
      state.friendId = friendId;
      state.friendName = friendName;
    }

    return {
      state,
      SearchFriend,
      GetFriendList,
      DeleteFriend,
      stateFriends,
      SearchFriendEmail, openChatting
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
  min-width: 60px;
  border-radius: 30px;
}

.chattingRoom {
  position: fixed;
  right: 410px;
  top: 300px;
  z-index: 3;
  box-shadow: 3px 3px 15px rgb(121 121 121);
}

@media (max-width: 820px) {
  .chattingRoom {
    position: fixed;
    left: 20px;
    top: 300px;
    z-index: 3;
  }
  /* .closeChatting {
    position: fixed;
    left: 20px;
    top: 272px;
    z-index: 3;
  } */
}

.closeChatting {
  position: absolute;
  right: 755px;
  top: -24px;
  z-index: 3;
}
</style>