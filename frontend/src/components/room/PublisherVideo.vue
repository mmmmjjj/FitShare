<template>
  <div class="video-container" v-if="streamManager">
    <div>
      <div v-if="loading" class="spinner-border text-secondary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
      <ov-publisher-video :stream-manager="streamManager"/>
      <div v-show="!loading"><p>{{ state.clientData }}</p></div>
    </div>
  </div>
</template>

<script>
import { reactive, computed } from 'vue'
import OvPublisherVideo from './OvPublisherVideo.vue';

export default {
    name: 'PublisherVideo',
    components: {
      OvPublisherVideo,
    },
    props: {
      streamManager: Object,
      loading: Boolean,
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
	padding-left: 5px;
	padding-right: 5px;
	color: black;
	font-weight: bold;
	border-bottom-left-radius: 10px;
  position: absolute;
  /* font-size: 16px; */
  font-size: 1.6vh;

  left: 50%;
  bottom: -23px;
  transform: translate(-50%, -50%);  
}

.spinner-border {
  margin-bottom: 20px;
  position: absolute;
  top: 45%;
  left: 50%;
}
</style>