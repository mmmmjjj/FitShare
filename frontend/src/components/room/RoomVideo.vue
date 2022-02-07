<template>
  <div class="video-container" v-if="streamManager">
    <ov-video :stream-manager="streamManager"/>
    <div><p>{{ state.clientData }}</p></div>
  </div>
</template>

<script>
import { reactive, computed } from 'vue'
import OvVideo from './OvVideo.vue';

export default {
    name: 'RoomVideo',

    components: {
      OvVideo,
    },

    props: {
      streamManager: Object,
    },

    setup (props) {
      const state = reactive({
        clientData: computed(() => {
          const { clientData } = getConnectionData();
          return clientData.split(' ')[0]
        })
      })

      const getConnectionData = () => {
        const { connection } = props.streamManager.stream;
        return JSON.parse(connection.data);
      }

      return {
        getConnectionData,
        state
      }
    }
}
</script>

<style scoped>
.video-container {
  position: relative;
}

p {
  display: inline-block;
	/* background: #f8f8f8a8; */
	padding-left: 5px;
	padding-right: 5px;
	color: black;
	font-weight: bold;
	border-bottom-left-radius: 10px;
  position: absolute;
  font-size: 16px;
  /* top: 34px; */
  /* left: 51.5px;
  bottom: -0.5px; */

  left: 50%;
  bottom: -25px;
  transform: translate(-50%, -50%);  
}
</style>