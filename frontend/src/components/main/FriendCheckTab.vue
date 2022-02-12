<template>
  <div>
    <div v-for="(checkRequest, index) in checkRequests" :key="checkRequest.id" class="d-flex mt-3">
      <img :src="checkRequest.profileImg" alt="profile-img">
      <div class="ms-3 d-flex flex-column">
        <div>
          {{ checkRequest.name }}
        </div>
        <div class="d-flex mt-2 button-box">
          <button class="" @click="AcceptFriend(checkRequests, index)">수락</button>
          <button class="ms-3" @click="DeclineFriend(checkRequests, index)">거절</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue'
import axios from '../../api/axios'

export default {
  name: 'FriendCheckTab',
  props: ['checkRequests'],
  setup() {
    const state = reactive({
      friends: [],
    })

    const AcceptFriend = async (checkRequests, index) => {
      const res = await axios({
        method: 'POST',
        url: 'friends',
        data: {"friendId": checkRequests[index].id}
      })
      console.log(res)
      
      const res1 = await axios({
        method: 'DELETE',
        url: `friends/requests/${checkRequests[index].id}`,
      })
      console.log(res1)

      checkRequests.splice(index, 1)
    
    }
    
    const DeclineFriend = (checkRequests, index) => {
      axios({
        method: 'DELETE',
        url: `friends/requests/${checkRequests[index].id}`,
        })
        .then(res => {
          console.log(checkRequests[index].id)
          console.log(res)
          checkRequests.splice(index, 1)
          console.log(checkRequests)
        })
    }


    return {
      state,
      AcceptFriend,
      DeclineFriend,
    }
  }
}
</script>

<style scoped>
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
