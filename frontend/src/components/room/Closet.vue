<template>
  <div id="closet">
    <div class="container">
      <div class="accordion" id="accordionExample">
        <div class="accordion-item" v-for="(friend, index) in friends" :key="friend.id">
          <h2 class="accordion-header" :id="'heading'+friend.id">
            <button class="accordion-button" :class="{ 'collapsed': index !== 0 }" type="button" data-bs-toggle="collapse" :data-bs-target="'#collapse'+friend.id" aria-expanded="true" :aria-controls="'collapse'+friend.id">
              {{ friend.name }}
            </button>
          </h2>
          <div :id="'collapse'+friend.id" class="accordion-collapse collapse" :class="{ 'show': index === 0 }" :aria-labelledby="'heading'+friend.id" data-bs-parent="#accordionExample">
            <div class="accordion-body" v-if="friend.id === 1">
              <div class="d-flex input-style">
                <div class="d-flex flex-column">
                  <!-- <div class="head-style">
                    상품 주소
                  </div>
                  <input type="text" placeholder="사이트 Url 입력" class="site-url mt-1" v-model="SiteUrl"> -->
                  <div class="mt-1 head-style">
                    이미지 주소
                  </div>
                  <input type="text" placeholder="이미지 Url 입력" class="img-url mt-1" v-model="ImgUrl">
                </div>
                <div class="d-flex justify-content-center align-items-center button-box ms-2">
                  <button @click="AddUrl">
                    +
                  </button>
                </div>
              </div>
              <div class="img-box">
                <img src="@/assets/shirt.jpg" alt="img" class="img-style mt-5">
                <img src="@/assets/shirt.jpg" alt="img" class="img-style mt-5">
                <img src="@/assets/shirt.jpg" alt="img" class="img-style mt-5">
                <img src="@/assets/shirt.jpg" alt="img" class="img-style mt-5">
              </div>
            </div>
          </div>
        </div>

      </div>

    </div>
  </div>
</template>

<script>
// import HostCloset from './HostCloset.vue'
import { ref } from 'vue'

export default {
  name: 'Closet',
  components: {
    // HostCloset,
  },
  props: {
    subscribers: Object,
  },
  setup() {
    const friends = ref([
      {id:1, name: '김싸피'},
      {id:2, name: '이싸피'},
      {id:3, name: '박싸피'},
      {id:4, name: '최싸피'},
      {id:5, name: '안싸피'},
      {id:6, name: '배싸피'},
    ])

    // const SiteUrl = ref('')
    const ImgUrl = ref('')
    
    const Urls = ref('')

    // const AddUrl = () => {
    //   if(SiteUrl.value.length < 1 && ImgUrl.value.length > 1) {
    //     alert('상품 주소를 입력하세요')
    //   } 
    //   else if(ImgUrl.value.length < 1 && SiteUrl.value.length > 1) {
    //     alert('이미지 주소를 입력하세요')
    //   } 
    //   else if(SiteUrl.value.length < 1 && ImgUrl.value.length < 1) {
    //     alert('상품 주소와 이미지 주소를 입력하세요')
    //   }
    //   else {
    //     alert('옷장에 추가되었습니다.')

    //     SiteUrl.value = ''
    //     ImgUrl.value = ''
    //   }
    // }

    const AddUrl = () => {
      if(ImgUrl.value.length < 1) {
        alert('이미지 주소를 입력하세요')
      }
      else {
        alert('옷장에 추가되었습니다.')

        ImgUrl.value = ''
      }
    }

    return {
      friends,
      // SiteUrl,
      ImgUrl,
      AddUrl,
      Urls,
    }
  }
}
</script>

<style scoped>
#closet {
  border: solid black;
}

.head-style {
  font-size: 24px;
}

.input-style {
  margin-top: 33px;
}

.site-url {
  width: 170px;
}

.img-url {
  width: 170px; 
}

.button-box {
  width: 90px;
}

.button-box button {
  width: 50px;
  height: 50px;
  border-radius: 25px;
  background-color: white;
  border: none;
}

.head-style {
  font-size: 18px;
}

img {
  width: 200px;
  height: 200px;
}

.img-box {
  height: 463px;
  overflow-y: scroll;
  overflow-x: hidden;
}

.img-box::-webkit-scrollbar {
  width: 10px;
}
.img-box::-webkit-scrollbar-thumb {
  background-color: #2f3542;
  border-radius: 10px;
}
.img-box::-webkit-scrollbar-track {
  background-color: #FDFAF3;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}
</style>