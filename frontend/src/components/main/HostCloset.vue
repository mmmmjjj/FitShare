<template>
  <div>
    <div class="container">
      <div class="host-name fw-bold">
        {{ selectedShoppingRoom.hostName }}님의 옷장 
      </div>
      <!-- 옷장에서 옷 가져오기 -->
      <host-closet-item class="host-closet-item"></host-closet-item>
    </div>
  </div>
</template>

<script>
import HostClosetItem from './HostClosetItem.vue'
export default {
  name: 'HostCloset',

  components: { HostClosetItem },

  props: {
    selectedShoppingRoom: Object,
  },
  
}
</script>

<style scoped>
.container {
  width: 457px;
  height: 535px;
  padding: 0px;
  position: relative;
  top: 30px;
  background-color: white;
}

.host-name {
  font-size: 20px;
}

.host-closet-item {
  margin-top: 44px;
  height: 463px;
  /* background-color: beige; */
  overflow-y: scroll;
  overflow-x: hidden;
}
.host-closet-item::-webkit-scrollbar {
  width: 7px;
  /* background-color: beige; */
}
.host-closet-item::-webkit-scrollbar-thumb {
  background-color: #2f3542;
  border-radius: 10px;
}
.host-closet-item::-webkit-scrollbar-track {
  background-color: #FDFAF3;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}
</style>