<template> 
  <div id="session">
    <div id="session" v-if="session">
      <!-- 컴포넌트 -->
      <div class="components-container">
        <group-chat class="group-chat"></group-chat>
        <div class="center">
          <!-- 화상화면 -->
          <div id="video-container" class="d-flex flex-row">
            <publisher-video :stream-manager="publisher" :loading="pub" @click="updateMainVideoStreamManager(publisher)"/>
            <subscriber-video v-for="(subscriber, index) in subscribers" :key="subscriber.stream.connection.connectionId"
              :stream-manager="subscriber" :loading="sub" :subscriber="index"
              @click="updateMainVideoStreamManager(subscriber)"
            />
          </div>
          <!-- 메인 화면 -->
          <div id="main-video" v-if="showMainVideo">  
            <main-video :stream-manager="mainStreamManager"/>
          </div>
          <!-- 쇼핑사이트 -->
          <shopping-site v-if="!showMainVideo" :shopping-mall-url="shoppingMallUrl" class="shopping-site"></shopping-site>
          <div class="buttons">
            <!-- 가상피팅화면 종료 -->
            <button v-if="showMainVideo" class="btn shadow-none stop-fitting-btn" @click="backToSite">
              <i class="fas fa-arrow-left"></i><p>피팅 종료하기</p>
            </button>
            <!-- 기본 기능 -->
            <button v-if="isAudio" class="btn shadow-none" @click="changeAudio()"><i class="bi bi-mic-mute-fill"></i></button>
            <button v-if="!isAudio" class="btn shadow-none" @click="changeAudio()"><i class="bi bi-mic-fill"></i></button>
            <button v-if="isVideo" class="btn shadow-none" @click="changeVideo()"><i class="bi bi-camera-video-off-fill"></i></button>
            <button v-if="!isVideo" class="btn shadow-none" @click="changeVideo()"><i class="bi bi-camera-video-fill"></i></button>
            <input class="btn shadow-none" type="button" id="buttonLeaveSession" @click="leaveSession" value="나가기">
            <!-- 필터 change -->
            <div v-if="isFitting && showMainVideo" class="radio-box">
              <div @click="changeFilter('top')">
                <input class="form-check-input" type="radio" name="flexRadioDefault" checked="checked" id="top">
                <label class="form-check-label" for="top">상의</label>
              </div>
              <div @click="changeFilter('bottoms')">
                <input class="form-check-input" type="radio" name="flexRadioDefault" id="bottom">
                <label class="form-check-label" for="bottom" >하의</label>
              </div>
              <div @click="changeFilter('hat')">
                <input class="form-check-input" type="radio" name="flexRadioDefault" id="hat">
                <label class="form-check-label" for="hat">모자</label>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 옷장 접기 -->
        <details>
          <summary>
            <i class="bi bi-arrow-bar-left" v-if="clickStatus" @click="changeClickStatus"></i>
            <i class="bi bi-arrow-bar-right closeIcon" v-if="clickStatus === false" @click="changeClickStatus"></i>
          </summary>
          <closet :subscribers="subscribers" :my-session-id="mySessionId" :publisher="publisher" @fitting="overlayFilter" class="closet"></closet>
        </details>

      </div>
		</div>
  </div>
</template>

<script>
import { reactive, toRefs, ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useStore } from 'vuex';
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
        
        let clothesUrl = ref('');
        let isFitting = ref(false);
        let showMainVideo = ref(false) ; // fitting 비디오 
        // let radioSelect = ref(false);

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

          clickStatus: true
        });

        const changeClickStatus = () => {
          state.clickStatus = !state.clickStatus;
        }
        
        const userData = computed(() => {
          return store.getters['user/userData']
        });

        // methods        
        const goToMain = () => {
          router.push({ name: 'Main' });
        };
        
        const changeAudio = () => {
          state.publisher.publishAudio(state.isAudio);
          let audio = (state.isAudio) ? false : true ;
          state.isAudio = audio;
        };

        const changeVideo = () => {
          state.publisher.publishVideo(state.isVideo);
          let video = (state.isVideo) ? false : true ;
          state.isVideo = video;
        };

        // 입어보기 버튼의 filter 적용
        const overlayFilter = (url) => {
          clothesUrl.value = url
          // radioSelect.value = false
          if (isFitting.value) removeFilter();
          
          state.publisher.stream.applyFilter("FaceOverlayFilter")
            .then(filter => {
                filter.execMethod(
                    "setOverlayedImage",
                    {
                        "uri": clothesUrl.value,
                        "offsetXPercent":"-1.5F",
                        "offsetYPercent":"0.6F",
                        "widthPercent":"4.0F",
                        "heightPercent":"4.0F"
                    });
            })
            .then(() => {
              state.mainStreamManager = state.publisher;
              isFitting.value = true
              showMainVideo.value = true
              // radioSelect.value = true
            })
            .catch(err => console.log(err));
        };

        const changeFilter = (type) => {
          removeFilter()
          state.publisher.stream.applyFilter("FaceOverlayFilter")
            .then(filter => {
                if (type == 'top') {
                  filter.execMethod(
                    "setOverlayedImage",
                    {
                        "uri": clothesUrl.value,
                        "offsetXPercent":"-1.5F",
                        "offsetYPercent":"0.6F",
                        "widthPercent":"4.0F",
                        "heightPercent":"4.2F"
                     });
                } else if (type == 'bottoms') {
                  filter.execMethod(
                    "setOverlayedImage",
                    {
                        "uri": clothesUrl.value,
                        "offsetXPercent":"-1.6F",
                        "offsetYPercent":"3.2F",
                        "widthPercent":"4.3F",
                        "heightPercent":"4.0F"
                     });
                } else {
                  filter.execMethod(
                    "setOverlayedImage",
                    {
                        "uri": clothesUrl.value,
                        "offsetXPercent":"-0.2F",
                        "offsetYPercent":"-0.7F",
                        "widthPercent":"1.3F",
                        "heightPercent":"1.1F"
                     });
                } 
            })
            .then(() => {
              isFitting.value = true
              state.mainStreamManager = state.publisher;
            })
        };

        // filter 제거 
        const removeFilter = () => {
          state.publisher.stream.removeFilter()
            .then(() => console.log("Filter removed"))
            .catch(err => console.error(err));
        };

        // 쇼핑사이트로 전환
        const backToSite = () => {
          if (isFitting.value) removeFilter();
          isFitting.value = false;
          // radioSelect.value = false; 
          showMainVideo.value = false;
        };

        // openvidu session 생성 method
        const joinSession = () => {
          state.OV = new OpenVidu();
          state.session = state.OV.initSession();

          state.session.on('streamCreated', ({ stream }) => {
            const subscriber = state.session.subscribe(stream);
            state.subscribers.push(subscriber);
            loading.sub.push(true)
            
            // video 실행시 로딩 스피너 제거 
            subscriber.on('streamPlaying', function () {
              loading.sub[loading.sub.length - 1] = false
            })
          });

          // stream 제거시 => subscribers, 로딩 index 제거  
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
          
          // session 연결
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

        // 화상화면 클릭시 메인 화면 전환
        const updateMainVideoStreamManager = (stream) => {   
          state.mainStreamManager = stream;
          if (!showMainVideo.value) showMainVideo.value = true;
        }

        // created 
        state.mySessionId = route.params.roomId  
        state.shoppingMallUrl = route.params.mallUrl 
        state.myUserName = userData
        
        joinSession() 

        return { 
          goToMain, changeAudio, changeVideo, overlayFilter, changeFilter, removeFilter, backToSite,
          joinSession, leaveSession, updateMainVideoStreamManager,
          ...toRefs(state), ...toRefs(loading), isFitting, showMainVideo, clothesUrl, changeClickStatus,
          // radioSelect
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

#session {
  background-color: #D3E2E7;
}

#video-container {
  justify-content: center;
  background-color: #D3E2E7;
  height: 15vh;
  /* border-bottom: 3px solid #8ABDBE;   */
}

.video-row {
  flex-direction: row;
}

.components-container {
  display: flex;
  justify-content: space-between;
}

.group-chat, .closet {
  min-width: 290px;
  width: 29vh;
  /* ------------------------------ */
  height: 92.3vh;
  margin: 0;
  background-color: white;
}

.center {
  position: relative;
  width: 100%;
  /* ------------------------------- */
  height: 100vh;  
  display: flex;
  flex-direction: column;
  align-items: center;
}

.center #main-video {
  position: relative;
  width: 100%;
}

.shopping-site {
  height: 77.1vh;
  margin-bottom: 6px;
}

.buttons {
  width: 860px;
  height: 7vh;
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
