<template>
  <div>
    <div v-if="hostClothes.length > 0" class="row">
      <div v-for="cloth in hostClothes" :key="cloth.clothId" class="col-4">
        <img :src="cloth.imageUrl" alt="cloth-img">
      </div>
    </div>
    <div v-else class="d-flex flex-column">
      <i class="fas fa-tshirt"></i>
      <p>친구의 옷장이 비어 있습니다.</p>
    </div>
  </div>

</template>

<script>
export default {
  name: 'HostClosetItem',
  props: {
    hostClothes: Object,
  },
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

i {
  margin: 120px 0 30px;
  font-size: 80px;
  color: #1B4D50;
}

p {
  font-size: 20px;
  font-weight: bold;
}
</style>