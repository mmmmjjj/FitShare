<template> 
  <div id="session">
    <div id="session" class="" v-if="session">
      <!-- 화상화면 -->
			<div id="video-container" class="d-flex flex-row">
        <room-video :stream-manager="publisher" @click="updateMainVideoStreamManager(publisher)"/>
        <room-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" @click="updateMainVideoStreamManager(sub)"/>
      </div>
      <!-- <div id="main-video" class="col-md-6">  
				<user-video :stream-manager="mainStreamManager"/>
			</div> -->

      <!-- 나머지 컴포넌트 -->
      <div class="components-container d-flex flex-row">
        <group-chat class="group-chat"></group-chat>
        <div>
          <shopping-site :shopping-mall-url="shoppingMallUrl" class="shopping-site"></shopping-site>
          <!-- 화상회의 버튼 -->
          <div class="buttons">
            <button v-if="isAudio" class="btn shadow-none" @click="offAudio()"><i class="bi bi-mic-mute-fill"></i></button>
            <button v-if="!isAudio" class="btn shadow-none" @click="onAudio()"><i class="bi bi-mic-fill"></i></button>
            <button v-if="isVideo" class="btn shadow-none" @click="offVideo()"><i class="bi bi-camera-video-off-fill"></i></button>
            <button v-if="!isVideo" class="btn shadow-none" @click="onVideo()"><i class="bi bi-camera-video-fill"></i></button>
            <input class="btn shadow-none" type="button" id="buttonLeaveSession" @click="leaveSession" value="나가기">
            
            <!-- overlay 테스트 -->
            <button @click="filter()">filter</button>
            <button @click="removeFilter()">remove</button>
          </div>
        </div>
        <closet :subscribers="subscribers" class="closet"></closet>
      </div>
		</div>
  </div>
</template>

<script>
import { reactive, toRefs, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { OpenVidu } from 'openvidu-browser';
import RoomVideo from '@/components/room/RoomVideo.vue';
import ShoppingSite from '@/components/room/ShoppingSite.vue';
import Closet from '@/components/room/Closet.vue';
import GroupChat from '@/components/room/GroupChat.vue';
import axios from 'axios'

export default {
    name: 'ShoppingRoom',

    components: {
      RoomVideo, ShoppingSite, Closet, GroupChat
    },

    setup () {
        const router = useRouter()
        const route = useRoute()
        
        let shoppingMallUrl = ref('')

        const state = reactive({
          OV: undefined,
          session: undefined,
          mainStreamManager: undefined,
          publisher: undefined,  
          subscribers: [], 

          mySessionId: '',
          myUserName: '김싸피 12',  // 임시 => store에서 사용자 정보 불러오기 

          isAudio: false,
          isVideo: false,
        })

        // created 
        state.mySessionId = route.params.roomId  
        shoppingMallUrl.value = route.params.mallUrl 
        
        // methods        
        const goToMain = () => {
          router.push({ name: 'Main' })
        }
        
        const offAudio = () => {
          state.publisher.publishAudio(state.isAudio)
          state.isAudio = false
        }
        const onAudio = () => {
          state.publisher.publishAudio(state.isAudio)
          state.isAudio = true
        }

        const offVideo = () => {
          state.publisher.publishVideo(state.isVideo)
          state.isVideo = false
        }
        const onVideo = () => {
          state.publisher.publishVideo(state.isVideo)
          state.isVideo = true
        }

        // openvidu method
        function filter() {
          this.publisher.stream.applyFilter("GStreamerFilter", { command: "gdkpixbufoverlay location=https://cdn.pixabay.com/photo/2019/08/09/15/10/flowers-4395240_960_720.jpg offset-x=10 offset-y=10 overlay-height=200 overlay-width=200" })
            .then(() => {
                console.log("Video rotated!");
            })
            .catch(error => {
                console.error(error);
            });
        }

        function removeFilter() {
          this.publisher.stream.removeFilter()
            .then(() => {
                console.log("Filter removed");
            })
            .catch(error => {
                console.error(error);
            });
        }

        const joinSession = () => {
          state.OV = new OpenVidu();

          state.session = state.OV.initSession();

          state.session.on('streamCreated', ({ stream }) => {
            const subscriber = state.session.subscribe(stream);
            state.subscribers.push(subscriber);
          });

          state.session.on('streamDestroyed', ({ stream }) => {
            const index = state.subscribers.indexOf(stream.streamManager, 0);
            if (index >= 0) {
              state.subscribers.splice(index, 1);
            }
          });

          state.session.on('exception', ({ exception }) => {
            console.warn(exception);
          });
          
          // ------------- token 관련 method -----------------
          state.session.connect(route.params.token, { clientData: state.myUserName })
            .then(() => {
              console.log('토큰 전달 후')
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
              // --- Publish your stream ---
              state.session.publish(publisher);
            })
            .catch(error => {
                console.log('There was an error connecting to the session:', error.code, error.message);
            });

          window.addEventListener('beforeunload', leaveSession)
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
            method : 'get',
            url: `http://i6a405.p.ssafy.io:8081/api/v1/shopping-rooms/${state.mySessionId}`,
            headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw` }
          })
            .then(() => {
              console.log('나가기 성공')
              goToMain()
            })
            .catch(err => console.log(err))
        }

        const updateMainVideoStreamManager = (stream) => {  // 화상화면 클릭시 해당 화면이 메인으로 이동 
          if (state.mainStreamManager === stream) return;
          state.mainStreamManager = stream;
        }

        // created
        joinSession() 

        return { 
          goToMain, offAudio, offVideo, onAudio, onVideo,
          joinSession, 
          leaveSession, updateMainVideoStreamManager,
          ...toRefs(state), shoppingMallUrl, filter, removeFilter,
        }
    }

}
</script>

<style scoped>
#session {
  background-color: #D3E2E7;
}

#video-container {
  justify-content: center;
  background-color: #D3E2E7;
  height: 185px;
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

.shopping-site {
  height: 780px
}

.buttons {
  height: 70px;
  line-height: 70px;
  text-align: center;
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
  font-size: 26px;
}
</style>