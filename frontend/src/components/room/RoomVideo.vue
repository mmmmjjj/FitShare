<template>
  <div class="video-container" v-if="streamManager">
    <ov-video :stream-manager="streamManager"/>
    <!-- <div><p>{{ state.clientData }}</p></div> -->
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
          return clientData
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
/* .video-container {
  position: relative;
}

p {
  display: inline-block;
	background: #f8f8f8;
	padding-left: 5px;
	padding-right: 5px;
	color: #777777;
	font-weight: bold;
	border-bottom-right-radius: 4px;
  position: absolute;
  font-size: 12px;
  top: 32px;
  left: 50%;
  transform: translate(-50%, -50%);  
} */
</style>