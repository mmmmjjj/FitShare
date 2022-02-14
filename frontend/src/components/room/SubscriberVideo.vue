<template>
  <div class="video-container" v-if="streamManager">
    <div v-if="loading.length == (subscriber+1) && loading[subscriber] " class="spinner-border text-secondary" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <ov-subscriber-video :stream-manager="streamManager"/>
    <div v-show="!loading[subscriber]"><p>{{ state.clientData }}</p></div>
  </div>
</template>

<script>
import { reactive, computed } from 'vue';
import OvSubscriberVideo from './OvSubscriberVideo.vue';

export default {
    name: 'SubscriberVideo',
    components: {
      OvSubscriberVideo,
    },

    props: {
      streamManager: Object,
      loading: Array,
      subscriber: Number,
    },

    setup (props) {
      const state = reactive({
        clientData: computed(() => {
          const { clientData } = getConnectionData();
          return clientData.split(' ')[0];
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
	color: white;
  position: absolute;
  background-color: rgba(211, 211, 211, 0.363);
  font-size: 1.4vh;
  left: 50%;
  bottom: -13px;
  transform: translate(-50%, -50%); 
}

.spinner-border {
  margin-bottom: 20px;
  position: absolute;
  top: 35%;
  left: 43%;
}
</style>