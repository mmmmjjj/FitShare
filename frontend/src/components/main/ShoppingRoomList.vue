<template>
  <div class="room-container">
   <h2>Live</h2>
   <div class="row">
     <!-- emit event room 정보 -->
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
import { useStore } from 'vuex'
import axios from 'axios'

export default {
    name: 'ShoppingRoomList',
    
    emits: ['first-host-closet', 'change-host-closet'],

    setup(props, { emit }) {
      const store = useStore()

      const state = reactive({
        shoppingRoomList: [],  //뒤에 
        
        // shoppingRoomList : [
        //   { shoppingRoomId: 1, hostName: '김싸피', maxParticipantCount: 2, isPrivate: true, shoppingMallName: 'nike', shoppingMallUrl: '..' },  // 이 외에 추가적으로
        // ],

      })

      // methods
      // 쇼핑룸 목록 불러오기 
      function getShoppingRoomList () {
        axios({
          method: 'get',
          url: `${store.state.url}/api/v1/shopping-rooms/`,
          headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw` }
        })
          .then(res => {
            // console.log(res.data.data)
            state.shoppingRoomList = res.data.data
          })
          .then(() => {
            emit('first-host-closet', state.shoppingRoomList[0])
          })
      }
      
      // created
      getShoppingRoomList()

      return {
        ...toRefs(state), getShoppingRoomList, 
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