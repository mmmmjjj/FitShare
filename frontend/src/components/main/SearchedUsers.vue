<template>
  <div>
    <div v-if="friend.profileImg" class="text-center result-box mt-3">
      검색 결과
    </div>
    <div class="d-flex mt-3" v-if="friend.profileImg">
      <img :src="friend.profileImg" alt="profile-img">
      <div class="ms-3 profile-box name-box">
        {{ friend.name }}
      </div>
      <div class="d-flex align-self-center btn-box justify-content-center">
        <button class="plus-btn" @click="RequestFriend(friend)">
          +
        </button>
      </div>
    </div>
  </div>
</template>


<script>
import axios from '../../api/axios'

export default {
  name: 'SearchedUsers',
  props: ['friend'],
  setup() {

    // 친구 요청 post
    const RequestFriend = (friend) => {
      axios({
        method: 'POST',
        url: 'friends/requests',
        data: {"friendId": friend.id}
      })
      .then(res => {
        console.log(res)
      })
      friend.profileImg = null
    }

    return {
      RequestFriend,
    }
  }
}
</script>


<style scoped>
.input-box {
  width: 301px;
  border-radius: 20px;
  text-align: center;
}

input::placeholder {
  text-align: center;
}

.result-box {
  font-size: 12px;
  color: '#565656'
}

.profile-box {
  width: 180px;
}

/* user 이름 font-size /
.name-box {
  font-size: 16px;
}

/ email font-size */
.email-box {
  font-size: 12px;
}

img {
  width: 50px;
  height: 50px;
  border-radius: 25px;
}

.plus-btn {
  width: 40px;
  height: 40px;
  border-radius: 25px;
  background-color: #FDFAF3;
}
.btn-box {
  width: 50px;
}

</style>