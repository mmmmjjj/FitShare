<template>
  <div class="container">
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
        autocomplete="off"
        autofocus
      />
    </div>
  </div>
</template>

<script>
import { reactive, computed, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { useStore } from "vuex";
import { useCookies } from "vue3-cookies";
import Stomp from "stompjs";
import SockJS from "sockjs-client";

export default {
  name: "GroupChat",
  setup() {
    const sockJs = new SockJS("https://i6a405.p.ssafy.io/api/v1/chat");
    const stomp = Stomp.over(sockJs);
    const route = useRoute();
    const store = useStore();
    const { cookies } = useCookies();

    const state = reactive({
      roomId: 0,
      userName: "",
      message: "",
      textarea: "",
      receivedMsg: [],
    });
    // state.textarea.scrollTop(state.textarea.scrollHeight);
    state.roomId = route.params.roomId; // 쇼핑룸 생성 시 쇼핑룸 id 가져오기
    state.receivedMsg = computed(() => store.state.chat.recvMsg); // vuex에서 가져오기
    state.userName = computed(() => store.state.user.user_name); // vuex에서 가져오기

    let accessToken = cookies.get("accessToken"); // accessToken cookie에서 가져오기
    let headers = {
      Authorization: "Bearer " + accessToken,
    };

    watch(state.receivedMsg, () => {
      addMsg();
    });

    onMounted(() => {
      recvMsg();
    });

    const dateFormatChange = (date) => {
      let received = date.split("-");
      let hour = received[2].charAt(3) + received[2].charAt(4);
      let minute = received[2].charAt(6) + received[2].charAt(7);

      if (hour >= 12) {
        if(hour > 12){
          return "오후 " + pad(hour - 12) + ":" + minute;
        }
        else{
          return "오후 " + pad(hour) + ":" + minute;  
        }
      } else {
        return "오전 " + pad(hour) + ":" + minute;
      }
    };

    const recvMsg = () => {
      state.receivedMsg.forEach((item) => {
        state.textarea +=
          "[" +
          item.senderName +
          "] " +
          dateFormatChange(item.createdTime) +
          "\n" +
          item.message +
          "\n";
      });
    };

    const addMsg = () => {
      let temp = state.receivedMsg;
      let slice = temp.slice(temp.length - 1, temp.length);
      console.log(slice);
      state.textarea +=
        "[" +
        slice[0].senderName +
        "] " +
        dateFormatChange(slice[0].createdTime) +
        "\n" +
        slice[0].message +
        "\n";
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
      if (state.message !== "") {
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
      recvMsg,
      sendMessage,
      dateFormatChange,
      koreaTime,
      pad,
      addMsg,
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
  margin-top: 10px;
  width: 100%;
  resize: none;
  background-color: #fdfaf3;
  color: gray;
  height: 430px;
  /* overflow-y: auto;
  display: flex;
  flex-direction: column-reverse;
  overflow-x: hidden; */
}
#message {
  width: 100%;
  background-color: #d3d3d380;
  height: 30px;
  margin-top: 10px;
}
#cancelIcon {
  margin-top: 3px;
  float: right;
  cursor: pointer;
}
</style>
