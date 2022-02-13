<template>
  <div>
    <div class="row">
      <!-- cloth.clothId, clothes => hostClothes -->
      <div v-for="cloth in hostClothes" :key="cloth.clothId" class="col-4">
        <img :src="cloth.imageUrl" alt="cloth-img">
      </div>
      <!-- <div v-for="cloth in clothes" :key="cloth.id" class="col-4">
        <img :src="cloth.src" alt="cloth-img">
      </div> -->
    </div>
  </div>

</template>

<script>
import { ref } from 'vue'

export default {
  name: 'HostClosetItem',
  props: {
    hostClothes: Object,
  },

  setup() {
    const clothes= ref([
      {id: 1, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163155.png', name:'김'},
      {id: 2, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163053.png', name:'배'},
      {id: 3, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163228.png', name:'이'},
      {id: 4, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163228.png', name:'최'},
      {id: 5, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163155.png', name:'박'},
      {id: 6, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163053.png', name:'안'},
      {id: 7, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163053.png', name:'강'},
      {id: 8, src: 'https://fitshare-bucket.s3.ap-northeast-2.amazonaws.com/images/107_2_20220212_163228.png', name:'허'},
      {id: 9, src: require('@/assets/shirt.jpg'), name:'양'},
      {id: 10, src: require('@/assets/shirt.jpg'), name:'장'},
    ])

    return {
      clothes,
    }
  }
}
</script>

<style scoped>
.row div {
  margin: 0px;
  text-align: center;
}

img {
  width: 135px;
  height: 160px;
  margin-bottom: 5px;
}

</style>