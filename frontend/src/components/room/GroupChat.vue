<template>
  <div class="container">
    <div>
      <!-- <span id="chatLogo"><b>Chat</b></span> -->
      <span class="material-icons-outlined" style="float:right"> cancel </span>
      <span></span>
    </div>
    <div>
      <textarea
        name=""
        id="textarea"
        class="border-3 border-light rounded"
        cols="30"
        rows="10"
        :value="state.textarea"
        disabled
      >
      </textarea>
    </div>
    <div>
      <input
        type="text"
        class="border-2 border-dark rounded"
        id="message"
        v-model="state.message"
        placeholder="Message를 입력해주세요"
        @keyup.enter="sendMessage()"
        autofocus
      />
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
    });

    const state = reactive({
      roomId: 0,
      userName: "",
      message: "",
      textarea: "",
      connected: false,
    });

    state.roomId = route.params.roomId; // 쇼핑룸 생성 시 쇼핑룸 id 가져오기
    state.userName = computed(() => store.state.user.user_name); // vuex에서 가져오기

    let accessToken = cookies.get("accessToken");

    let headers = {
      Authorization: "Bearer " + accessToken,
    };

    const sockJs = new SockJS("https://i6a405.p.ssafy.io/api/v1/chat");
    const stomp = Stomp.over(sockJs);

    const dateFormatChange = (date) => {
      let changedDate = new Date(date);
      // console.log(changedDate.getHours() + changedDate.getMinutes());
      if (changedDate.getHours() > 12) {
        return (
          "오후 " +
          pad(changedDate.getHours() - 12) +
          ":" +
          pad(changedDate.getMinutes())
        );
      } else {
        return (
          "오전 " +
          pad(changedDate.getHours()) +
          ":" +
          pad(changedDate.getMinutes())
        );
      }
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
              dateFormatChange(receiveMsgBody.createdTime) +
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

    // 메세지를 보냈을 때
    const sendMessage = async () => {
      // message의 값이 있고 연결된 상태라면
      if (state.message !== "" && state.connected) {
        console.log("szfdz");
        console.log(koreaTime());
        let sendMsg = {
          senderName: state.userName,
          message: state.message,
          createdTime: koreaTime(),
        };
        await stomp.send(
          `/app/rooms/${state.roomId}`,
          headers,
          JSON.stringify(sendMsg)
        );
        state.message = "";
      }
    };

    return {
      state,
      accessToken,
      headers,
      connect,
      sendMessage,
      dateFormatChange,
      koreaTime,
      pad,
    };
  },
};
</script>

<style scoped>
#container {
}

#chatLogo {
  font-size: 24px;
}

#textarea {
  /* change */
  margin-top: 40px;
  height: 430px;
  width: 100%;
  resize: none;
  background-color: #fdfaf3;
  color: gray;
}
#message {
  width: 100%;
  background-color: #d3d3d380;
  height: 30px;
  margin-top: 4px;
}
</style>
