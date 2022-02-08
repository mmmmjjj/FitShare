<template>
  <div class="video-container" v-if="streamManager">
    <ov-main-video :stream-manager="streamManager"/>
    <!-- <div><p>{{ state.clientData }}</p></div> -->
  </div>
</template>

<script>
import { reactive, computed } from 'vue'
import OvMainVideo from './OvMainVideo.vue';

export default {
    name: 'MainVideo',

    components: {
      OvMainVideo,
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

</style>