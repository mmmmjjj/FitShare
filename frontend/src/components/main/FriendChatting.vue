<template>
  <div>
    <div class="container">
      <div class="tab">
        <div class="last-chat" @click="getMessages()">
          <img src="@/assets/expand_less_1x_icon.png" alt="" class="expand_icon" />
        </div>
      </div>
      <div class="msg-box">
        <div v-for="msg in state.textarea" :key="msg.createdTime">
          <div v-if="msg.receiverId !== friendId">
            <div class="mt-2 friend-msg-box">[{{ friendName }}] {{ dateFormat(msg.createdTime) }} <br /></div>
            <div class="friend-message">
              <p class="friend-msg mt-1">
                {{ msg.message }}
              </p>
            </div>
          </div>
          <div v-else>
            <div class="mt-2 my-msg-box">{{ dateFormat(msg.createdTime) }} <br /></div>
            <div class="my-message">
              <p class="my-msg mt-1">
                {{ msg.message }}
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="inputDiv">
        <label for="message"></label>
        <input
          type="text"
          class="border-2 border-slid rounded border-gray-600 w-full inputMsg"
          id="message"
          v-model="state.message"
          @keyup.enter="sendMessage()"
          placeholder="Message를 입력해주세요."
          autofocus
        />
        <i class="bi bi-send" @click="sendMessage()"></i>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, onMounted } from "vue";
import { useStore } from "vuex";
import { useCookies } from "vue3-cookies";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import axios from "../../api/axios";

export default {
  name: "FriendChatting",
  props: ["friendId", "friendName"],
  setup(props) {
    const store = useStore();
    const { cookies } = useCookies();

    onMounted(() => {
      connect();
    });

    const state = reactive({
      userName: "",
      message: "",
      textarea: [],
      connected: false,
      currentTime: "",
      myMessage: [],

      dayCnt: 0,
      lastMessages: true,
      today: "",
    });

    let accessToken = cookies.get("accessToken");

    let headers = {
      Authorization: "Bearer " + accessToken,
    };

    const sockJs = new SockJS("https://i6a405.p.ssafy.io/api/v1/chat");
    const stomp = Stomp.over(sockJs);

    const dateFormat = (date) => {
      const messageDate = createdDate(date);
      const hour = date.slice(11, 13);
      const minute = date.slice(14, 16);

      if (state.today == messageDate) {
        if (hour > 12) {
          return " 오후 " + String(Number(hour) - 12) + ":" + minute;
        } else {
          return " 오전 " + hour + ":" + minute;
        }
      } else {
        const month = date.slice(5, 7);
        const day = date.slice(8, 10);
        const changedDate = month + "/" + day;
        if (hour > 12) {
          return changedDate + " 오후 " + String(Number(hour) - 12) + ":" + minute;
        } else {
          return changedDate + " 오전 " + hour + ":" + minute;
        }
      }
    };

    const todayDate = () => {
      const today = new Date();
      const month = ("0" + (today.getMonth() + 1)).slice(-2);
      const day = ("0" + today.getDate()).slice(-2);
      state.today = month + day;
    };

    const createdDate = (date) => {
      const month = date.slice(5, 7);
      const day = date.slice(8, 10);
      return month + day;
    };

    const connect = () => {
      // if (!state.connected) {
      stomp.connect(headers, (frame) => {
        state.connected = true;
        console.log("Connect Status : " + frame);
        stomp.subscribe(
          `/user/queue/messages`,
          (response) => {
            // 구독을 설정할 때, 메시지를 받으면 어떻게 처리할건지도 설정
            console.log(response);
            let receiveMsgBody = JSON.parse(response.body);
            const obj = {
              username: receiveMsgBody.senderName,
              createdTime: receiveMsgBody.createdTime,
              message: receiveMsgBody.message,
            };
            state.textarea.push(obj);
            document.querySelector(".msg-box").scrollTop = document.querySelector(".msg-box").scrollHeight;
          },
          headers
        );
        (error) => {
          console.log("Connect Status : ", error);
          state.connected = false;
        };
      });
    };

    const koreaTime = () => {
      const now = new Date();
      let temp =
        now.getFullYear() +
        "-" +
        pad(now.getMonth() + 1) +
        "-" +
        pad(now.getDate()) +
        "T" +
        pad(now.getHours()) +
        ":" +
        pad(now.getMinutes()) +
        ":" +
        pad(now.getSeconds()) +
        "." +
        (now.getMilliseconds() / 1000).toFixed(3).slice(2, 5) +
        "Z";
      return temp;
    };

    const pad = (number) => {
      if (number < 10) {
        return "0" + number;
      }
      return number;
    };

    // 지난 메세지 받기
    const getMessages = () => {
      const today = new Date();
      today.setDate(today.getDate() - state.dayCnt);
      const year = today.getFullYear();
      const month = ("0" + (today.getMonth() + 1)).slice(-2);
      const day = ("0" + today.getDate()).slice(-2);
      const date = year + month + day;
      axios({
        method: "get",
        url: `/chat-rest/${props.friendId}/${date}`,
        headers: headers,
      })
        .then((res) => {
          const loadedMessage = res.data.data;
          const array = loadedMessage.concat(state.textarea);

          array.sort(function (a, b) {
            const keyA = new Date(a.createdTime);
            const keyB = new Date(b.createdTime);
            if (keyA < keyB) return -1;
            if (keyA > keyB) return 1;
            return 0;
          });

          const height = document.querySelector(".msg-box").scrollHeight;
          state.textarea = array;

          return height;
        })
        .then((height) => {
          if (!state.lastMessages) {
            document.querySelector(".msg-box").scrollTop = document.querySelector(".msg-box").scrollHeight;
          } else {
            document.querySelector(".msg-box").scrollTop = document.querySelector(".msg-box").scrollHeight - height;
          }
          state.dayCnt += 1;
          state.lastMessages = false;
        })
        .catch((err) => console.log(err.response));
    };

    // created
    getMessages();
    todayDate();

    // 메세지를 보냈을 때
    const sendMessage = async () => {
      // message의 값이 있고 연결된 상태라면
      const user = store.state.user.user_name;
      const userId = store.state.user.user_id;

      if (state.message !== "" && state.connected) {
        let sendMsg = {
          senderName: user,
          senderId: userId,
          message: state.message,
          createdTime: koreaTime(),
          receiverId: props.friendId,
        };
        await stomp.send(`/app/messages`, headers, JSON.stringify(sendMsg));
        state.textarea.push(sendMsg);
        state.message = "";
        document.querySelector(".msg-box").scrollTop = document.querySelector(".msg-box").scrollHeight;
      }
    };

    onMounted(() => {
      let chattingBox = document.querySelector(".msg-box");
      chattingBox.addEventListener("scroll", () => {
        handleScroll();
      });
    });

    const handleScroll = () => {
      const chat = document.querySelector(".msg-box");
      if (chat.scrollTop == 0) {
        state.lastMessages = true;
      } else {
        state.lastMessages = false;
      }
    };

    return {
      state,
      stomp,
      headers,
      accessToken,
      connect,
      sendMessage,
      koreaTime,
      pad,
      scroll,
      getMessages,
      createdDate,
      dateFormat,
    };
  },
};
</script>

<style scoped>
.last-chat {
  position: absolute;
  top: 5px;
  left: 45%;
  cursor: pointer;
}

.container {
  width: 387px;
  height: 490px;
  background-color: #fdfaf3;
  position: fixed;
  right: 470px;
  bottom: 104px;
  box-shadow: 3px 3px 15px rgb(121 121 121);
}

.tab {
  height: 7%;
}

.inputDiv {
  position: relative;
}

i {
  position: absolute;
  left: 90%;
  top: 85%;
  color: rgb(68, 67, 67);
}

.inputMsg {
  width: 360px;
  height: 40px;
  position: absolute;
  top: 55%;
  background-color: #d3d3d380;
  border: 0;
}

.msg-box {
  width: 100%;
  height: 80%;
  overflow-y: scroll;
  overflow-x: hidden;
  background-color: #fdfaf3;
  padding: 0 5px;
}

.msg-box::-webkit-scrollbar {
  width: 7px;
}
.msg-box::-webkit-scrollbar-track {
  background-color: transparent;
}
.msg-box::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #2f3542;
}

.my-name {
  text-align: right;
}

.my-message {
  display: flex;
  flex-direction: row-reverse;
}

.my-msg-box {
  display: flex;
  flex-direction: row-reverse;
}

.my-msg {
  background-color: #ffbdbd;
  right: 5px;
  max-width: 200px;
  margin-left: 110px;
  display: flex;
  flex-direction: row;
  padding: 7px 12px 7px 7px;
  border-radius: 10px;
}

.friend-message {
  display: flex;
  flex-direction: row;
}

.friend-msg {
  background-color: #e5ebf7;
  max-width: 200px;
  display: flex;
  flex-direction: row;
  padding: 7px 12px 7px 7px;
  border-radius: 10px;
}

.expand_icon {
  width: 30px;
  height: 20px;
}
</style>
