<template>
  <div id="session">
    <div id="session" v-if="session">
      <!-- 컴포넌트 -->
      <div class="components-container">
        <!-- 화상화면 -->
        <div id="video-container">
          <publisher-video
            :stream-manager="publisher"
            :loading="pub"
            @click="updateMainVideoStreamManager(publisher)"
          />
          <subscriber-video
            v-for="(subscriber, index) in subscribers"
            :key="subscriber.stream.connection.connectionId"
            :stream-manager="subscriber"
            :loading="sub"
            :subscriber="index"
            @click="updateMainVideoStreamManager(subscriber)"
          />
        </div>
        <div class="content">
          <div class="viewer">
            <!-- 메인 화면 -->
            <main-video
              id="main-video"
              v-if="showMainVideo"
              :stream-manager="mainStreamManager"
            />
            <!-- 쇼핑사이트 -->
            <shopping-site
              v-show="!showMainVideo"
              :shopping-mall-url="shoppingMallUrl"
              class="shopping-site"
            ></shopping-site>
            <!-- 단체 채팅 -->
            <group-chat
              class="group-chat"
              v-if="clickChatStatus === false"
            ></group-chat>
            <!-- 옷장 접기 -->
            <closet
              :subscribers="subscribers"
              :my-session-id="mySessionId"
              :publisher="publisher"
              @fitting="overlayFilter"
              class="closet"
              v-if="clickStatus === false"
            ></closet>
          </div>
          <!-- 버튼 -->
          <div class="buttons" :class="buttons - center">
            <!-- 가상피팅화면 종료 -->
            <button
              v-if="showMainVideo"
              class="btn shadow-none stop-fitting-btn"
              @click="backToSite"
            >
              <i class="fas fa-arrow-left"></i>
              <p>피팅 종료하기</p>
            </button>
            <!-- 기본 기능 -->
            <button
              v-if="isAudio"
              class="btn shadow-none"
              @click="changeAudio()"
            >
              <i class="bi bi-mic-mute-fill"></i>
            </button>
            <button
              v-if="!isAudio"
              class="btn shadow-none"
              @click="changeAudio()"
            >
              <i class="bi bi-mic-fill"></i>
            </button>
            <button
              v-if="isVideo"
              class="btn shadow-none"
              @click="changeVideo()"
            >
              <i class="bi bi-camera-video-off-fill"></i>
            </button>
            <button
              v-if="!isVideo"
              class="btn shadow-none"
              @click="changeVideo()"
            >
              <i class="bi bi-camera-video-fill"></i>
            </button>
            <input
              class="btn shadow-none"
              type="button"
              id="buttonLeaveSession"
              @click="leaveSession"
              value="나가기"
            />

            <!-- 필터 change -->
            <div v-if="isFitting && showMainVideo" class="radio-box">
              <div @click="changeFilter('top')">
                <input
                  class="form-check-input"
                  type="radio"
                  name="flexRadioDefault"
                  checked="checked"
                  id="top"
                />
                <label class="form-check-label" for="top">상의</label>
              </div>
              <div @click="changeFilter('bottoms')">
                <input
                  class="form-check-input"
                  type="radio"
                  name="flexRadioDefault"
                  id="bottom"
                />
                <label class="form-check-label" for="bottom">하의</label>
              </div>
              <div @click="changeFilter('hat')">
                <input
                  class="form-check-input"
                  type="radio"
                  name="flexRadioDefault"
                  id="hat"
                />
                <label class="form-check-label" for="hat">모자</label>
              </div>
            </div>
            <button
              class="btn shadow-none right"
              v-if="clickStatus"
              @click="changeClickStatus"
            >
              <img src="@/assets/closed_closet.png" alt="" class="closetBtn" />
            </button>
            <button
              class="btn shadow-none right"
              v-if="clickStatus === false"
              @click="changeClickStatus"
            >
              <img src="@/assets/opened_closet.png" alt="" class="closetBtn" />
            </button>
            <button
              class="btn shadow-none right"
              v-if="clickChatStatus"
              @click="changeClickChatStatus"
            >
              <img src="@/assets/chatting_icon.png" alt="" class="chatBtn" />
            </button>
            <button
              class="btn shadow-none right"
              v-if="clickChatStatus === false"
              @click="changeClickChatStatus"
            >
              <img src="@/assets/chatting_icon.png" alt="" class="chatBtn" />
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, toRefs, ref, computed, watch, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
import { OpenVidu } from "openvidu-browser";
import { useCookies } from "vue3-cookies";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import axios from "../api/axios";
import PublisherVideo from "@/components/room/PublisherVideo.vue";
import SubscriberVideo from "@/components/room/SubscriberVideo.vue";
import MainVideo from "@/components/room/MainVideo.vue";
import ShoppingSite from "@/components/room/ShoppingSite.vue";
import Closet from "@/components/room/Closet.vue";
import GroupChat from "@/components/room/GroupChat.vue";

export default {
  name: "ShoppingRoom",
  components: {
    PublisherVideo,
    SubscriberVideo,
    MainVideo,
    ShoppingSite,
    Closet,
    GroupChat,
  },

  setup(props, {emit}) {
    onMounted(() => {
      connect();
    });

    const sockJs = new SockJS("https://i6a405.p.ssafy.io/api/v1/chat");
    const stomp = Stomp.over(sockJs);
    const { cookies } = useCookies();
    const router = useRouter();
    const route = useRoute();
    const store = useStore();

    let clothesUrl = ref("");
    let isFitting = ref(false);
    let showMainVideo = ref(false); // fitting 비디오

    const loading = reactive({
      pub: true,
      sub: [],
    });
    const state = reactive({
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      mySessionId: "",
      myUserName: "",
      shoppingMallUrl: "",
      hostId: null,
      isAudio: false,
      isVideo: false,
      closetClass: "closet",
      chatClass: "group-chat",
      clickStatus: true,
      clickChatStatus: true,
    });

    let accessToken = cookies.get("accessToken");
    let headers = {
      Authorization: "Bearer " + accessToken,
    };

    let roomId = route.params.roomId;
    const connect = () => {
      stomp.connect(headers, (frame) => {
        console.log("Connect Status : " + frame);
        stomp.subscribe(
          `/topic/rooms/${roomId}`,
          (response) => {
            emit('emitMsg', response.body);
            store.dispatch("chat/pushMsg", response.body, { root: true });
          },
          headers
        );
        (error) => {
          console.log("Connect Status : ", error);
        };
      });
    };
    const changeClickStatus = () => {
      state.clickStatus = !state.clickStatus;
      if (state.clickStatus) state.closetClass = "closet blocked";
      else state.closetClass = "closet";
    };

    const changeClickChatStatus = () => {
      state.clickChatStatus = !state.clickChatStatus;
      if (state.clickChatStatus) state.chatClass = "group-chat blocked";
      else state.chatClass = "group-chat";
    };

    const userData = computed(() => {
      return store.getters["user/userData"];
    });

    // methods
    const goToMain = () => {
      router.push({ name: "Main" });
    };

    const changeAudio = () => {
      state.publisher.publishAudio(state.isAudio);
      let audio = state.isAudio ? false : true;
      state.isAudio = audio;
    };

    const changeVideo = () => {
      state.publisher.publishVideo(state.isVideo);
      let video = state.isVideo ? false : true;
      state.isVideo = video;
    };

    // 입어보기 버튼의 filter 적용
    const overlayFilter = (url) => {
      clothesUrl.value = url;
      if (isFitting.value) removeFilter();

      state.publisher.stream
        .applyFilter("FaceOverlayFilter")
        .then((filter) => {
          filter.execMethod("setOverlayedImage", {
            uri: clothesUrl.value,
            offsetXPercent: "-1.5F",
            offsetYPercent: "0.6F",
            widthPercent: "4.0F",
            heightPercent: "4.0F",
          });
        })
        .then(() => {
          state.mainStreamManager = state.publisher;
          isFitting.value = true;
          showMainVideo.value = true;
        })
        .catch((err) => console.log(err));
    };

    const changeFilter = (type) => {
      removeFilter();
      state.publisher.stream
        .applyFilter("FaceOverlayFilter")
        .then((filter) => {
          if (type == "top") {
            filter.execMethod("setOverlayedImage", {
              uri: clothesUrl.value,
              offsetXPercent: "-1.5F",
              offsetYPercent: "0.6F",
              widthPercent: "4.0F",
              heightPercent: "4.2F",
            });
          } else if (type == "bottoms") {
            filter.execMethod("setOverlayedImage", {
              uri: clothesUrl.value,
              offsetXPercent: "-1.6F",
              offsetYPercent: "3.2F",
              widthPercent: "4.3F",
              heightPercent: "4.0F",
            });
          } else {
            filter.execMethod("setOverlayedImage", {
              uri: clothesUrl.value,
              offsetXPercent: "-0.2F",
              offsetYPercent: "-0.7F",
              widthPercent: "1.3F",
              heightPercent: "1.1F",
            });
          }
        })
        .then(() => {
          isFitting.value = true;
          state.mainStreamManager = state.publisher;
        });
    };

    // filter 제거
    const removeFilter = () => {
      state.publisher.stream
        .removeFilter()
        .then(() => {})
        .catch((err) => console.error(err));
    };

    // 쇼핑사이트로 전환
    const backToSite = () => {
      if (isFitting.value) removeFilter();
      isFitting.value = false;
      showMainVideo.value = false;
    };

    // openvidu session 생성 method
    const joinSession = () => {
      state.OV = new OpenVidu();
      state.session = state.OV.initSession();

      state.session.on("streamCreated", ({ stream }) => {
        const subscriber = state.session.subscribe(stream);
        state.subscribers.push(subscriber);
        loading.sub.push(true);

        // video 실행시 로딩 스피너 제거
        subscriber.on("streamPlaying", () => {
          const index = state.subscribers.indexOf(stream.streamManager, 0);
          loading.sub[index] = false;
        });
      });

      // stream 제거시 => subscribers, 로딩 index 제거
      state.session.on("streamDestroyed", ({ stream }) => {
        const index = state.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          state.subscribers.splice(index, 1);
          loading.sub.splice(index, 1);
        }
      });

      state.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // session 연결
      state.session
        .connect(route.params.token, { clientData: state.myUserName })
        .then(() => {
          let publisher = state.OV.initPublisher(undefined, {
            audioSource: undefined,
            videoSource: undefined,
            publishAudio: true,
            publishVideo: true,
            resolution: "640x480",
            frameRate: 30,
            insertMode: "APPEND",
            mirror: false,
          });

          publisher.subscribeToRemote();
          state.mainStreamManager = publisher;
          state.publisher = publisher;
          state.session.publish(publisher);

          publisher.on("streamPlaying", function () {
            loading.pub = false;
          });
        })
        .catch((error) => {
          console.log(
            "There was an error connecting to the session:",
            error.code,
            error.message
          );
        });

      window.addEventListener("beforeunload", leaveSession);
    };

    // 화상화면 클릭시 메인 화면 전환
    const updateMainVideoStreamManager = (stream) => {
      state.mainStreamManager = stream;
      if (!showMainVideo.value) showMainVideo.value = true;
    };

    const leaveSession = () => {
      if (state.session) state.session.disconnect();
      state.session = undefined;
      state.mainStreamManager = undefined;
      state.publisher = undefined;
      state.subscribers = [];
      state.OV = undefined;

      window.removeEventListener("beforeunload", leaveSession);

      axios
        .post(`shopping-rooms/${state.mySessionId}`)
        .then(() => goToMain())
        .catch((err) => console.log(err));
    };

    watch(state.subscribers, () => {
      const userId = store.state.user.user_id;
      if (state.hostId == userId) {
        return;
      } else {
        if (state.subscribers.length == 0) goToMain();
      }
    });

    // created
    state.mySessionId = route.params.roomId;
    state.shoppingMallUrl = route.params.mallUrl;
    state.myUserName = userData;
    state.hostId = route.params.hostId;

    joinSession();

    return {
      goToMain,
      changeAudio,
      changeVideo,
      overlayFilter,
      changeFilter,
      removeFilter,
      backToSite,
      joinSession,
      leaveSession,
      updateMainVideoStreamManager,
      ...toRefs(state),
      ...toRefs(loading),
      isFitting,
      showMainVideo,
      clothesUrl,
      changeClickStatus,
      changeClickChatStatus,
    };
  },
};
</script>

<style scoped>
.background {
  position: absolute;
  top: 0px;
  width: 859px;
  height: 790px;
  background-color: #d3e2e7;
}

#session {
  background-color: #d3e2e7;
}

#video-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #8abdbe;
  width: 220px;
  height: 100vh;
}

.components-container {
  display: flex;
  flex-direction: row;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.viewer {
  display: flex;
  flex-direction: row;
  width: 100%;
}

/* change */
.group-chat {
  position: fixed;
  right: 40px;
  bottom: 110px;
  min-width: 290px;
  width: 180px;
  height: 530px;
  margin: 0;
  background-color: white;
  border-radius: 10px;
  /* background-color: #1B4D50; */
  /* border: 1px solid black; */
  box-shadow: 3px 3px 15px rgb(121 121 121);
}
.closet {
  min-width: 290px;
  width: 29vh;
  height: 91vh;
  margin: 0;
  background-color: white;
}

.blocked {
  display: block;
}

#main-video {
  position: relative;
  width: 100%;
}

.shopping-site {
  height: 91vh;
}

.buttons {
  width: 900px;
  height: 70px;
  line-height: 70px;
  text-align: center;
  position: relative;
}

.buttons-center {
  width: 900px;
  left: -10%;
  height: 70px;
  line-height: 70px;
  text-align: center;
  position: relative;
}

.buttons .stop-fitting-btn {
  position: absolute;
  left: 20px;
  top: 24%;
  font-size: 16px;
  height: 38px;
  line-height: 26px;
  display: flex;
  flex-direction: row;
  background-color: #696b6e;
  color: white;
  padding-top: 2px;
}

.stop-fitting-btn i {
  font-size: 18px;
  font-weight: normal;
  margin: 6px 10px 0 0;
}

p {
  margin-bottom: 0;
  margin-top: 3px;
}

.closetBtn {
  vertical-align: middle;
  width: 44px;
  height: auto;
}

.chatBtn {
  vertical-align: middle;
  width: 44px;
  height: auto;
}

.buttons .btn {
  margin: 0 5px 0;
  cursor: pointer;
}

.buttons #buttonLeaveSession {
  background-color: red;
  color: white;
  margin-left: 15px;
  font-size: 1.6vh;
}

i {
  font-size: 3.3vh;
}

/* 필터 change */
.radio-box {
  display: flex;
  position: absolute;
  top: 16px;
  right: 22px;
  height: 40px;
  line-height: 40px;
  color: #363738;
}

.radio-box input {
  margin: 13px 8px 0 20px;
  cursor: pointer;
}

.radio-box label {
  font-size: 18px;
  cursor: pointer;
}

details > summary {
  list-style: none;
}

details > summary > i {
  position: fixed;
  top: 250px;
  right: 20px;
  z-index: 2;
  width: 20px;
  height: 30px;
}

details > summary > .closeIcon {
  position: fixed;
  right: 275px;
}
</style>
