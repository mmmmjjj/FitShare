<template>
  <div>
    <!-- 친구 이름, 이메일 검색 input-box -->
    <input
      class="input-box"
      type="text"
      placeholder="이메일 검색"
      @keyup="SearchUserEmail"
      v-model="SearchUser"
    >
    
    <!-- 이름, 이메일로 검색 시 나오는 users들 components -->
    <searched-users :friends="state.friends"></searched-users>


  </div>
</template>

<script>
import SearchedUsers from './SearchedUsers.vue'
import { ref, reactive } from 'vue'
import axios from '../../api/axios'

export default {
  name: 'FriendMakeTab',
  components: {
    SearchedUsers,
  },
  setup() {
    const state = reactive({
      friends: []
    })

    // input에 입력하는 값
    const SearchUser = ref('');

    // input에 email 입력 시 server에 요청
    const SearchUserEmail = () => {
      if (SearchUser.value) {
        axios({
          method: 'GET',
          url: `members/${SearchUser.value}`,
          })
          .then(res => {
            console.log(res)
            state.friends = res.data.data
          })
      }
      else{
        state.friends = []
      }
    }

    return {
      state,
      SearchUser,
      SearchUserEmail,
    }
  }
}
</script>

<style>
.result {
  text-align: center;
  font-size: 12px;
  color: #565656;
}

img {
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
</style>