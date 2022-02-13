<template>
  <div class="host-closet">
    <h2 class="host-name fw-bold">
      {{ selectedShoppingRoom.hostName }}님의 옷장 
    </h2>
    <host-closet-item :host-clothes="hostClothes" class="host-closet-item"></host-closet-item>
  </div>
</template>

<script>
import { computed, ref, watch } from 'vue'  
import { useStore } from 'vuex'
import axios from '@/api/axios'
import HostClosetItem from './HostClosetItem.vue'

export default {
  name: 'HostCloset',
  components: { HostClosetItem },

  setup() {
    const store = useStore();
    let hostClothes = ref([]) 
    
    const hostCloset = () => {
      console.log(selectedShoppingRoom.value.hostId)
      axios.get(`clothes/${selectedShoppingRoom.value.shoppingRoomId}/${selectedShoppingRoom.value.hostId}`)
        .then(res => {
          hostClothes.value = res.data.data
        })
        .catch(err => console.log(err))
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
  font-weight: bold;
  padding: 30px 43px;
  margin: 0;
}

.host-closet-item {
  padding: 0 20px 14px;
  height: 505px;
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