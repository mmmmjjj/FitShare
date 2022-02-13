<template>
  <div class="container">
    <label><b>Group chat</b></label>
    <div>
      <textarea
        name=""
        id="textarea"
        class="col"
        cols="30"
        rows="10"
        :value="state.textarea"
        disabled
      ></textarea>
    </div>
    <div>
      <label for="message">Your Message</label>
      <input
        type="text"
        class="border-2 border-slid rounded border-gray-600 w-full p-2"
        id="message"
        v-model="state.message"
        @keyup.enter="sendMessage()"
        autofocus
      />
      <button
        class="border-2 border-slid rounded border-gray-600 w-full p-2"
        type="button"
        @click="sendMessage()"
      >
        전송
      </button>
    </div>
  </div>
</template>

<script>
import { reactive, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useStore } from "vuex";
import { useCookies } from "vue3-cookies";
import Stomp from "stompjs";
import SockJS from "sockjs-client";

export default {
  name: "GroupChat",
  setup() {
    const route = useRoute();
    const store = useStore();
    const { cookies } = useCookies();

    onMounted(() => {
      connect();
    })

    const state = reactive({
      roomId: 0,
      userName: "",
      message: "",
      textarea: "",
      connected: false,
      currentTime: "",
    });

    state.roomId = route.params.roomId; // 쇼핑룸 생성 시 쇼핑룸 id 가져오기
    state.userName = computed(() => store.state.user.user_name); // vuex에서 가져오기

    let accessToken = cookies.get("accessToken");

    let headers = {
      Authorization: "Bearer " + accessToken,
    };

    const sockJs = new SockJS("http://i6a405.p.ssafy.io:8081/api/v1/chat");
    const stomp = Stomp.over(sockJs);

    const now = () => {
      let date = new Date();
      if (date.getHours() > 12) {
        state.currentTime =
          "오후 " + (date.getHours() - 12) + ":" + date.getMinutes();
      } else {
        state.currentTime = "오전 " + date.getHours() + ":" + date.getMinutes();
      }
      return state.currentTime;
    };

    // 연결
    const connect = () => {
      // if (!state.connected) {
      stomp.connect(headers, (frame) => {
        state.connected = true;
        console.log("Connect Status : " + frame);
        stomp.subscribe(
          `/topic/rooms/${state.roomId}`,
          (response) => {
            // 구독을 설정할 때, 메시지를 받으면 어떻게 처리할건지도 설정
            console.log(response);
            let receiveMsgBody = JSON.parse(response.body);
            state.textarea +=
              "[" +
              receiveMsgBody.senderName +
              "] " +
              receiveMsgBody.createdTime +
              "\n" +
              receiveMsgBody.message +
              "\n";
          },
          headers
        );
        (error) => {
          console.log("Connect Status : ", error);
          state.connected = false;
        };
      });
    };
    // };

    // 메세지를 보냈을 때
    const sendMessage = () => {
      // message의 값이 있고 연결된 상태라면
      if (state.message !== "" && state.connected) {
        let sendMsg = {
          senderName: state.userName,
          message: state.message,
          createdTime: now(),
        };
        stomp.send(`/app/rooms/${state.roomId}`, headers, JSON.stringify(sendMsg));
        state.message = "";
      }
    };

    return {
      state,
      accessToken,
      headers,
      connect,
      sendMessage,
      now,
    };
  },
};
</script>

<style scoped>
#container {
  display: none;
}
#textarea {
  height: 700px;
  resize: none;
}
</style>
