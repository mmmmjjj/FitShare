<template> 
  <div id="session">
    <div id="session" class="" v-if="session">
      <!-- 화상화면 -->
			<div id="video-container" class="d-flex flex-row">
        <publisher-video :stream-manager="publisher" :loading="pub"/>
        <subscriber-video v-for="(subscriber, index) in subscribers" :key="subscriber.stream.connection.connectionId"
        :stream-manager="subscriber" @click="updateMainVideoStreamManager(subscriber)" :loading="sub" :subscriber="index"/>
      </div>

      <!-- 나머지 컴포넌트 -->
      <div class="components-container d-flex flex-row">
        <group-chat class="group-chat"></group-chat>
        <div class="center">
          <!-- <div v-if="background" class="background"></div> -->
          <div id="main-video" v-if="showMainVideo">  
            <main-video :stream-manager="mainStreamManager"/>
          </div>
          <shopping-site :shopping-mall-url="shoppingMallUrl" class="shopping-site"></shopping-site>
          
          <!-- 화상회의 버튼 -->
          <div class="buttons">
            <!-- 가상피팅화면 전환 -->
            <button v-if="showMainVideo" class="btn shadow-none stop-fitting-btn" @click="backToSite">
              <i class="fas fa-arrow-left"></i><p>쇼핑으로 돌아가기</p>
            </button>
            <!-- 기본기능 -->
            <button v-if="isAudio" class="btn shadow-none" @click="offAudio()"><i class="bi bi-mic-mute-fill"></i></button>
            <button v-if="!isAudio" class="btn shadow-none" @click="onAudio()"><i class="bi bi-mic-fill"></i></button>
            <button v-if="isVideo" class="btn shadow-none" @click="offVideo()"><i class="bi bi-camera-video-off-fill"></i></button>
            <button v-if="!isVideo" class="btn shadow-none" @click="onVideo()"><i class="bi bi-camera-video-fill"></i></button>
            <input class="btn shadow-none" type="button" id="buttonLeaveSession" @click="leaveSession" value="나가기">
          </div>
        </div>
        <closet :subscribers="subscribers" @fitting="overlayFitting" class="closet"></closet>
      </div>
		</div>
  </div>
</template>

<script>
import { reactive, toRefs, ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useStore } from 'vuex'
import { OpenVidu } from 'openvidu-browser';
import axios from '../api/axios'
import PublisherVideo from '@/components/room/PublisherVideo.vue';
import SubscriberVideo from '@/components/room/SubscriberVideo.vue';
import MainVideo from '@/components/room/MainVideo.vue';
import ShoppingSite from '@/components/room/ShoppingSite.vue';
import Closet from '@/components/room/Closet.vue';
import GroupChat from '@/components/room/GroupChat.vue';

export default {
    name: 'ShoppingRoom',
    components: {
      PublisherVideo, SubscriberVideo, MainVideo, ShoppingSite, Closet, GroupChat
    },

    setup () {
        const router = useRouter();
        const route = useRoute();
        const store = useStore();
        
        let isFitting = ref(false);
        let showMainVideo = ref(false) ; // 중앙 비디오 여부 

        let background = ref(false);
        const loading = reactive({
          pub: true,
          sub: [],
        })
        const state = reactive({
          OV: undefined,
          session: undefined,
          mainStreamManager: undefined,
          publisher: undefined,  
          subscribers: [], 

          mySessionId: '',
          myUserName: '',  
          shoppingMallUrl: '',
          isAudio: false,
          isVideo: false,
        });
        
        const userData = computed(() => {
          return store.getters['login/userData']
        });

        // methods        
        const goToMain = () => {
          router.push({ name: 'Main' });
        };
        
        const offAudio = () => {
          state.publisher.publishAudio(state.isAudio);
          state.isAudio = false;
        };
        const onAudio = () => {
          state.publisher.publishAudio(state.isAudio);
          state.isAudio = true;
        };

        const offVideo = () => {
          state.publisher.publishVideo(state.isVideo);
          state.isVideo = false;
        };
        const onVideo = () => {
          state.publisher.publishVideo(state.isVideo);
          state.isVideo = true;
        };
        
        // 옷장과 연결 
        const overlayFitting = (clothesUrl) => {
          if (isFitting.value) {
            removeFilter()
          }
          background.value = true
          showMainVideo.value = false
          
          state.publisher.stream.applyFilter("FaceOverlayFilter")
            .then(filter => {
                filter.execMethod(
                    "setOverlayedImage",
                    {
                        "uri": clothesUrl,
                        "offsetXPercent":"-1.5F",
                        "offsetYPercent":"0.6F",  // 하의 : 3.0F
                        "widthPercent":"4.0F",
                        "heightPercent":"4.0F"
                    });
            })
            .then(() => {
              isFitting.value = true
              state.mainStreamManager = state.publisher;
              showMainVideo.value = true
              background.value = false
            })
            .catch(err => console.log(err));

          // faceoverlay 모자
          // state.publisher.stream.applyFilter("FaceOverlayFilter")
          //   .then(filter => {
          //       filter.execMethod(
          //           "setOverlayedImage",
          //           {
          //               // aws 이미지 주소 사용 예정
          //               "uri": "http://files.openvidu.io/img/mario-wings.png",
          //               "offsetXPercent":"-0.2F",
          //               "offsetYPercent":"-0.8F",
          //               "widthPercent":"1.3F",
          //               "heightPercent":"1.0F"
          //            });
          //   });
        };

        const removeFilter = () => {
          state.publisher.stream.removeFilter()
            .then(() => console.log("Filter removed"))
            .catch(err => console.error(err));
        };

        // 쇼핑사이트로 전환
        const backToSite = () => {
          if (isFitting.value) removeFilter();
          isFitting.value = false;
          showMainVideo.value = false;
          state.mainStreamManager = state.publisher;
        };

        // openvidu session 생성 method
        const joinSession = () => {
          state.OV = new OpenVidu();
          state.session = state.OV.initSession();

          state.session.on('streamCreated', ({ stream }) => {
            const subscriber = state.session.subscribe(stream);
            state.subscribers.push(subscriber);
            loading.sub.push(true)
            
            subscriber.on('streamPlaying', function () {
              loading.sub[loading.sub.length - 1] = false
            })
          });

          state.session.on('streamDestroyed', ({ stream }) => {
            const index = state.subscribers.indexOf(stream.streamManager, 0);
            if (index >= 0) {
              state.subscribers.splice(index, 1);
              loading.sub.splice(index, 1);
            }
          });

          state.session.on('exception', ({ exception }) => {
            console.warn(exception);
          });
          
          state.session.connect(route.params.token, { clientData: state.myUserName })
            .then(() => {
              let publisher = state.OV.initPublisher(undefined, {
                audioSource: undefined, 
                videoSource: undefined,
                publishAudio: true,  	
                publishVideo: true,  	
                resolution: '640x480',  
                frameRate: 30,			
                insertMode: 'APPEND',	
                mirror: false,
              });
              
              publisher.subscribeToRemote();
              state.mainStreamManager = publisher;
              state.publisher = publisher;
              state.session.publish(publisher);

              publisher.on('streamPlaying', function () {
                loading.pub = false
              })
            })
            .catch(error => {
                console.log('There was an error connecting to the session:', error.code, error.message);
            });

          window.addEventListener('beforeunload', leaveSession);
        }

        const leaveSession = () => {
          if (state.session) state.session.disconnect();
          state.session = undefined;
          state.mainStreamManager = undefined;
          state.publisher = undefined;
          state.subscribers = [];
          state.OV = undefined;

          window.removeEventListener('beforeunload', leaveSession);

          axios({
            method : 'post',
            url: `shopping-rooms/${state.mySessionId}`,
          })
            .then(() => {
              goToMain()
            })
            .catch(err => console.log(err));
        }

        // 화상화면 클릭시 해당 화면이 메인으로 이동
        const updateMainVideoStreamManager = (stream) => {   
          showMainVideo.value = false  // 추가
          state.mainStreamManager = stream;

          if (!showMainVideo.value) showMainVideo.value = true;
          if (isFitting.value) { 
            removeFilter()
            isFitting.value = false  
            console.log('피팅 종료')
          }
        }

        // created 
        state.mySessionId = route.params.roomId  
        state.shoppingMallUrl = route.params.mallUrl 
        state.myUserName = userData
        
        joinSession() 

        return { 
          goToMain, offAudio, offVideo, onAudio, onVideo,
          joinSession, leaveSession, updateMainVideoStreamManager, overlayFitting, backToSite, removeFilter,
          ...toRefs(state), ...toRefs(loading), isFitting, showMainVideo, background
        }
    }

}
</script>

<style scoped>
.background {
  position: absolute;
  top: 0px;
  width: 859px;
  height: 790px;
  background-color: #D3E2E7;
}

/*  */
#session {
  background-color: #D3E2E7;
}

#video-container {
  justify-content: center;
  background-color: #D3E2E7;
  height: 185px;
  /* 임시 */
  /* border-bottom: 1px solid white;   */
}

.video-row {
  flex-direction: row;
}

.components-container {
  display: flex;
  justify-content: space-between;
  /* position: relative; */
}

.group-chat, .closet {
  width: 290px;
  height: 839px;
  background-color: white;
}

.center {
  position: relative;
}

.center #main-video {
  position: absolute;
}

.shopping-site {
  height: 780px
}

.buttons {
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
  /* padding-bottom: 5px; */
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

/* 확인! */
.buttons .btn {  
  margin: 0 5px 0;
  cursor: pointer;
}

.buttons #buttonLeaveSession {
  background-color: red;
  color: white;
  margin-left: 15px;
}

i {
  font-size: 30px;
}
</style>