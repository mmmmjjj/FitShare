<template>
  <div>
    <h2 v-if="selectedShoppingRoom" class="host-name fw-bold">
      <img src="@/assets/closed_closet_2.png" alt="closet">{{ selectedShoppingRoom.hostName }}님의 옷장
    </h2>
    <h2 v-else class="host-name fw-bold"><img src="@/assets/closed_closet_2.png" class="" alt="closet">옷장</h2>
    <host-closet-item :host-clothes="hostClothes" class="host-closet-item"></host-closet-item>
  </div>
</template>

<script>
import { computed, ref, watch } from 'vue';
import { useStore } from 'vuex';
import axios from '@/api/axios';
import HostClosetItem from './HostClosetItem.vue';

export default {
  name: 'HostCloset',
  components: { HostClosetItem },

  setup() {
    const store = useStore();
    let hostClothes = ref([]);
    
    const hostCloset = () => {
      if (selectedShoppingRoom.value) {
        axios.get(`clothes/${selectedShoppingRoom.value.shoppingRoomId}/${selectedShoppingRoom.value.hostId}`)
          .then(res => {
            hostClothes.value = res.data.data
          })
          .catch(err => console.log(err))
      }
    }

    const selectedShoppingRoom = computed(() => {
      return store.state.room.selectedShoppingRoom
    });

    watch(selectedShoppingRoom, () => {
      hostCloset()
    });

    return {
     selectedShoppingRoom, hostCloset, hostClothes
    }
  }
  
}
</script>

<style scoped>
.host-name {
  font-size: 38px;
  font-weight: bold;
  padding: 30px 43px;
  margin: 0;
}

.fa-times {
  font-size: 70px;
  font-weight: normal;
  position: absolute;
  left: 50%;
  top: 40%;
  transform: translate(-50%, -50%);
}

image {
 width: 40px;
 height: 40px;
 margin-right: 10px;
}

.message {
  font-size: 20px;
  width: 300px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.host-closet-item {
  padding: 0 20px 14px;
  height: 505px;
  overflow-y: scroll;
  overflow-x: hidden;
}

.host-closet-item::-webkit-scrollbar {
  width: 7px;
}
.host-closet-item::-webkit-scrollbar-track {
  background-color: transparent;
}
.host-closet-item::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #2f3542;
}
</style>