<template>
  <div id="closet">
  
    <div class="accordion" id="accordionExample">
      <!-- 내 옷장 -->
      <div class="accordion-item">
        <h2 class="accordion-header" :id="'heading'+myId">
          <button class="accordion-button" type="button" data-bs-toggle="collapse" :data-bs-target="'#collapse'+myId" aria-expanded="true" :aria-controls="'collapse'+myId" @click="getClothes(myId)">
            내 옷장
          </button>
        </h2>
        <div :id="'collapse'+myId" class="accordion-collapse collapse show" :aria-labelledby="'heading'+myId" data-bs-parent="#accordionExample">
          <div class="accordion-body">
            <div class="d-flex input-style">
              <input type="text" placeholder="이미지 Url 입력" class="img-url mt-1" v-model="ImgUrl">
              <img src="@/assets/plus_icon.png" @click="AddUrl" alt="" class="plus-img ms-3">
            </div>
            <div class="img-box mt-2">
              <div v-for="(cloth, index) in state.clothes" :key="cloth.clothId">
                <button class="delete-btn" @click="deleteCloth(state.clothes, index)"><img src="@/assets/close-icon.png" alt=""></button>
                <button class="btn btn-secondary" @click="$emit('fitting', cloth.imageUrl)">입어보기</button>
                <img :src="cloth.imageUrl" alt="img" class="img-style">
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- subscribers 옷장 -->
      <div class="accordion-item" v-for="(subscribe, index) in subscribers" :key="index">
        <h2 class="accordion-header" :id="'heading'+index">
          <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" :data-bs-target="'#collapse'+index" aria-expanded="false" :aria-controls="'collapse'+index" @click="getFriendsClothes(subscribe)">
            {{ splitName(subscribe).split(' ')[0] }} 's 옷장
          </button>
        </h2>
        <div :id="'collapse'+index" class="accordion-collapse collapse" :aria-labelledby="'heading'+index" data-bs-parent="#accordionExample">
          <div class="accordion-body">
            <div class="img-box">
              <div v-for="cloth in state.friendsClothes" :key="cloth.id">
                <button class="btn btn-secondary" @click="$emit('fitting', cloth.imageUrl)">입어보기</button>
                <img :src="cloth.imageUrl" alt="img" class="img-style">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
  </div>
</template>

<script scoped>
import { ref, reactive, computed } from 'vue'
import { useStore } from 'vuex'
import axios from '../../api/axios'

export default {
  name: 'Closet',
  components: {
    
  },
  props: {
    subscribers: Object,
    mySessionId: String,
    publisher: Object,
  },
  emits: ['fitting'],
  setup(props) {
    const ImgUrl = ref('')
    
    const Urls = ref('')

    const state = reactive({
      clothes: [],
      subscribers: [],
      clientData: computed(() => {
        const { clientData } = getConnectionData();
        return Number(clientData.split(' ')[1])
      }),
      friendsClothes: [],
    })


    // publisher data 객체만 추출
    const getConnectionData = () => {
      const { connection } = props.publisher.stream
      return JSON.parse(connection.data);
    }

    // subscriber data 추출
    const splitName = (subscribe) => {
      const { connection } = subscribe.stream
      const parse = JSON.parse(connection.data)
      const { clientData } =  parse
      return clientData
    }

    const store = useStore()
    
    // 내 id store/login 에서 가져오기
    const myId = computed(() => {
      return Number(store.state.user.user_id)
    })

    // 옷 이미지 주소 입력 후 등록
    const AddUrl = () => {
      if(ImgUrl.value.length < 1) {
        alert('이미지 주소를 입력하세요')
      }
      else {
        alert('옷장에 추가되었습니다.')
        axios({
          method: 'POST',
          url: `clothes`,
          data: {"imageUrl": ImgUrl.value, "shoppingRoomId": props.mySessionId}
          })
          .then(res => {
            console.log(res)
            state.clothes.push(res.data.data)
          })
          .catch(res => {
            console.log(res)
          })
        ImgUrl.value = ''
      }
    }

    // mySessionId번 방의 내 memberId(myId) 의 옷 리스트 GET 요청
    const getClothes = (myId) => {
      axios({
        method: 'GET',
        url: `clothes/${props.mySessionId}/${myId}`,
      })
      .then(res => {
        console.log(res)
        state.clothes = res.data.data
        console.log(state.clothes)
      })
    }

    const getFriendsClothes = (subscribe) => {
      state.friendsClothes = []
      axios({
        method: 'GET',
        url: `clothes/${props.mySessionId}/${splitName(subscribe).split(' ')[1]}`,
      })
      .then(res => {
        console.log(res)
        state.friendsClothes = res.data.data
      })
    }

    // 내 옷 삭제
    const deleteCloth = (clothes, index) => {
      axios({
        method: 'DELETE',
        url: `clothes/${clothes[index].clothId}`
      })
      .then(res => {
        console.log(res)
        clothes.splice(index, 1)
      })
    }

    const fitting = () => {

    }


    return {
      ImgUrl,
      AddUrl,
      Urls,
      fitting,
      getClothes,
      state,
      myId,
      getConnectionData, splitName, getFriendsClothes, deleteCloth
    }
  }
}
</script>

<style scoped>
#closet {
  
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

/* 버튼 사용 시 스타일 */
/* .add-button {
  background-color: white;
  border: none;
} */

/* 버튼모양 이미지로 대체했을 때 스타일 */
.plus-img {
  width: 40px;
  height: 40px;
}

.head-style {
  font-size: 18px;
}

.img-style {
  width: 200px;
  height: 200px;
}

.img-box .btn {
  position: relative;
  left: 80px;
  top: 190px;
}

.delete-btn {
  background-color: white; 
  color: black;
  border: none;
}

.delete-btn img {
  width: 30px;
  height: 30px;
}

.img-box {
  height: 463px;
  overflow-y: scroll;
  overflow-x: hidden;
  position: relative;
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

.clth-style {
  width: 300px;
  height: 300px;;
}
</style>