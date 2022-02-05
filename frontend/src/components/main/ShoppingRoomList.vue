<template>
  <div class="room-container">
   <h2>Live</h2>
   <div class="row">
     <!-- emit change-host-room -->
     <div id="room" class="room col-6" :class="index % 2 ? 'room-right' : 'room-left'" 
      :style="{ 'background-image': `url(${require(`../../assets/shopping_${index % 5 + 1}.png`)})` }"
      v-for="(room, index) in shoppingRoomList" :key="index" 
      @click="$emit('change-host-closet', room)"
      >
       <div class="room-info">
        <p class="mall-name">{{ room.shoppingMallName }}</p>
        <p class="host-name">{{ room.hostName }}님의 쇼핑룸</p>
       </div>
     </div>
   </div>
  </div>
</template>

<script>
import { reactive, toRefs, } from 'vue';

export default {
    name: 'ShoppingRoomList',
    
    props: {
      shoppingRoomList: Object,
    },

    emits: ['change-host-closet'],

    setup() {
      const state = reactive({
        // shoppingRoomList : [
        //   { shoppingRoomId: 1, hostName: '김싸피', maxParticipantCount: 2, isPrivate: true, shoppingMallName: 'nike', shoppingMallUrl: '..' },  // 이 외에 추가적으로
        // ],

      })

      return {
        ...toRefs(state),
      }
    }
}
</script>

<style scoped>
.room-container {
  height: 775px;
  width: 543px;
  background-color: #FDFAF3;
  border-radius: 16px;
  padding: 0;
}

h2 {
  font-size: 20px;
  font-weight: bold;
  padding: 30px 43px;
  margin: 0;
}

.row {
  width: 543px;
  height: 691px;
  overflow-y: scroll;
  padding: 0 43px 0 43px;
  margin: 0;
}

/* 스크롤바 */
.row::-webkit-scrollbar {
  width: 7px;
}
.row::-webkit-scrollbar-track {
  background-color: transparent;
}
.row::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #D3E2E7;
}

.room {
  position: relative;
  padding: 0;
  margin-bottom: 45px;
  width: 205px;
  height: 297px;  
  border-radius: 10px;
  border: 3px solid #D3E2E7;
  cursor: pointer;

  background-repeat : no-repeat;
  background-size : contain;
  background-position: 0 20%;
}

.room:hover {
  box-shadow: 1px 1px 15px rgb(207, 206, 206);
}

.room-left {
  margin-right: 20px;
}
.room-right {
  margin-left: 20px;
}

.room-info {
  position: absolute;
  bottom: 0;
}

.room-info p {
  font-size: 16px;
  font-weight: bold;
}
.mall-name {
  margin: 0 23px 11px;
}
.host-name {
  margin: 0 23px 18px;
}
</style>