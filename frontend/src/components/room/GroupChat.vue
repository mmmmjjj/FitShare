<template>
  <div class="container">
    <label><b>Group chat</b></label>
    <div id="msgArea" class="col"></div>
    <div class="col-6">
      <input type="text" id="inputMsg" />
      <button type="button" id="button-send" @click="send()">전송</button>
    </div>
  </div>
</template>

<script>
import { reactive, computed } from "vue";
import { useRoute } from "vue-router";
import { useStore } from "vuex";
// import { useCookies } from "vue3-cookies";
// import Stomp from "webstomp-client";
// import SockJS from "sockjs-client";
// import { SockJS } from 'socket.io-client'

// import { VueStomp } from "vue-stomp";
export default {
  name: "GroupChat",
  setup() {
    const route = useRoute();
    const store = useStore();
    // const { cookies } = useCookies();
    // const { VueStomp } = VueStomp();

    const state = reactive({
      roomId: 0,
      userName: "",
      received_messages: [],
      send_message: null,
      connected: false,
    });

    state.roomId = route.params.roomId; // 쇼핑룸 생성 시 쇼핑룸 id 가져오기
    state.userName = computed(() => store.state.login.user_name); // vuex에서 가져오기

    let sendMsg = {
      roomId: state.roomId,
      senderName: state.userName,
      message: "",
    };
    // const accessToken = cookies.get("accessToken");
    // const headers = {
    //   Authorization: "Bearer " + accessToken,
    // };

    // const stomp = new Stomp();

    // 연결
    // const connect = () => {
    //   const serverURL = "http://localhost:8081/api/v1/chat";
    //   const sock = new SockJS(serverURL);
    //   stomp.over(sock);

    //   stomp.connect(headers, function (frame) {
    //     console.log("------ connect -------");
    //     console.log(frame);
    //     console.log("---------------------");
    //     console.log("------ subscribe -------");
    //     console.log("/topic/room/" + state.roomId);
    //     console.log("---------------------");
    //     // 구독 (채팅방을 구독한다고 생각하면 된다)
    //     stomp.subscribe(
    //       "/topic/room/" + state.roomId,
    //       (response) => {
    //         // 구독을 설정할 때, 메시지를 받으면 어떻게 처리할건지도 설정
    //         console.log("------ onMessage -------");
    //         console.log(response);
    //         console.log("---------------------");
    //         onMessage(response);
    //       },
    //       headers
    //     );
    //     // 연결이 완료되면 전송 버튼에 클릭이벤트 설정
    //     // document
    //     //     .getElementById("button-send")
    //     //     .addEventListener("click", send);
    //   });
    // };

    // 메시지를 보낸다
    const send = () => {
      // 메시지 객체에 사용자가 입력한 내용을 설정
      let inputMsg = document.getElementById("inputMsg");
      const inputMsgContents = inputMsg.value;
      sendMsg.message = inputMsgContents;
      inputMsg.value = "";
      // STOMP 사용
      // stomp.send("/app/chat/message", headers, JSON.stringify(sendMsg));
    };

    // 메세지를 받았을 때
    const onMessage = (receiveMsg) => {
      let msgArea = document.getElementById("msgArea");
      // STOMP
      const receiveMsgBody = JSON.parse(receiveMsg.body);
      let div = document.createElement("div");
      let contents = document.createTextNode(
        "작성자:" +
          receiveMsgBody.senderName +
          " 메시지:" +
          receiveMsgBody.message
      );
      div.append(contents);
      msgArea.append(div);
    };

    return {
      // connect,
      send,
      onMessage,
    };
  },
};
</script>

<style scoped></style>
