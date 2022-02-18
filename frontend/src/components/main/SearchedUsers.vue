<template>
  <div>
    <div v-if="friend.length" class="text-center result-box mt-3">검색 결과</div>
    <div class="d-flex mt-3" v-if="friend.length">
      <img :src="friend[0].profileImg" alt="profile-img" class="profile-img" />
      <div class="ms-3 profile-box name-box d-flex-column">
        {{ friend[0].name }}
        {{ friend[0].email }}
      </div>
      <div class="d-flex align-self-center btn-box justify-content-center">
        <button class="plus-btn" @click="RequestFriend(friend, index)">+</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "../../api/axios";

export default {
  name: "SearchedUsers",
  props: ["friend"],
  setup() {
    // 친구 요청 post
    const RequestFriend = (friend, index) => {
      axios({
        method: "POST",
        url: "friends/requests",
        data: { friendId: friend[0].id },
      }).then((res) => {
        console.log(res);
        friend.splice(index, 1);
      });
    };

    return {
      RequestFriend,
    };
  },
};
</script>

<style scoped>
.profile-img {
  width: 60px;
  height: 60px;
  min-width: 60px;
}

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
  color: "#565656";
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
  border-radius: 30px;
}

.plus-btn {
  width: 40px;
  height: 40px;
  border-radius: 25px;
  background-color: #fdfaf3;
}
.btn-box {
  width: 50px;
}
</style>
