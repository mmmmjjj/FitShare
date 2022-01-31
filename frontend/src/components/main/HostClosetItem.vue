<template>
  <div>
    <div class="row">
      <div v-for="friend in friends" :key="friend.id" class="col-4">
        <img :src="friend.src" alt="cloth-img">
      </div>
    </div>
  </div>

</template>

<script>
import { ref } from 'vue'

export default {
  name: 'HostClosetItem',
  setup() {
    const friends= ref([
      {id: 1, src: require('@/assets/shirt.jpg'), name:'김'},
      {id: 2, src: require('@/assets/shirt.jpg'), name:'배'},
      {id: 3, src: require('@/assets/shirt.jpg'), name:'이'},
      {id: 4, src: require('@/assets/shirt.jpg'), name:'최'},
      {id: 5, src: require('@/assets/shirt.jpg'), name:'박'},
      {id: 6, src: require('@/assets/shirt.jpg'), name:'안'},
      {id: 7, src: require('@/assets/shirt.jpg'), name:'강'},
      {id: 8, src: require('@/assets/shirt.jpg'), name:'허'},
      {id: 9, src: require('@/assets/shirt.jpg'), name:'양'},
      {id: 10, src: require('@/assets/shirt.jpg'), name:'장'},
    ])

    return {
      friends,
    }
  }
}
</script>

<style scoped>
img {
  width: 125px;
  height: 150px;
  padding: 0px;
}

</style>