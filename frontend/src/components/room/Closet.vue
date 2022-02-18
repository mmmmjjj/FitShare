<template>
  <div id="closet">
    <div class="accordion" id="accordionExample">
      <div class="accordion-item">
        <h2 class="accordion-header" :id="'heading' + myId">
          <button
            class="accordion-button"
            type="button"
            data-bs-toggle="collapse"
            :data-bs-target="'#collapse' + myId"
            aria-expanded="true"
            :aria-controls="'collapse' + myId"
            @click="getClothes(myId)"
          >
            내 옷장 <img id="hanger" src="@/assets/tshirt.png" alt="" />
          </button>
        </h2>
        <div
          :id="'collapse' + myId"
          class="accordion-collapse collapse show"
          :aria-labelledby="'heading' + myId"
          data-bs-parent="#accordionExample"
        >
          <div class="accordion-body">
            <div class="d-flex align-items-end">
              <input type="text" placeholder="이미지 URL 입력" class="img-url" v-model="ImgUrl" />
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="31"
                height="31"
                fill="currentColor"
                class="bi bi-plus-square ms-3"
                @click="AddUrl"
                viewBox="0 0 16 16"
              >
                <path
                  d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"
                ></path>
                <path
                  d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"
                ></path>
              </svg>
            </div>
            <div class="img-box mt-2">
              <div v-for="(cloth, index) in state.clothes" :key="cloth.clothId">
                <button class="delete-btn" @click="deleteCloth(state.clothes, index)">
                  <img src="@/assets/close-icon.png" alt="" />
                </button>
                <button class="btn btn-secondary" @click="$emit('fitting', cloth.imageUrl)">입어보기</button>
                <img :src="cloth.imageUrl" alt="img" class="img-style" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="accordion-item" v-for="(subscribe, index) in subscribers" :key="index">
        <h2 class="accordion-header" :id="'heading' + index">
          <button
            class="accordion-button collapsed"
            type="button"
            data-bs-toggle="collapse"
            :data-bs-target="'#collapse' + index"
            aria-expanded="false"
            :aria-controls="'collapse' + index"
            @click="getFriendsClothes(subscribe)"
          >
            {{ splitName(subscribe).split(" ")[0] }} 님의 옷장
          </button>
        </h2>
        <div
          :id="'collapse' + index"
          class="accordion-collapse collapse"
          :aria-labelledby="'heading' + index"
          data-bs-parent="#accordionExample"
        >
          <div class="accordion-body">
            <div class="img-box">
              <div v-for="cloth in state.friendsClothes" :key="cloth.id">
                <button class="btn btn-secondary" @click="$emit('fitting', cloth.imageUrl)">입어보기</button>
                <img :src="cloth.imageUrl" alt="img" class="img-style" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="warn_alert" class="alert alert-warning warn-alert" role="alert">
      <i class="bi bi-exclamation-triangle-fill"></i>이미지 주소를 확인해 주세요
    </div>

    <div v-if="success_alert" class="alert alert-primary success-alert" role="alert">
      <i class="bi bi-basket"></i>옷장에 추가되었습니다
    </div>
  </div>
</template>

<script scoped>
import { ref, reactive, computed, watch } from "vue";
import { useStore } from "vuex";
import axios from "../../api/axios";

export default {
  name: "Closet",
  components: {},
  props: {
    subscribers: Object,
    mySessionId: String,
    publisher: Object,
  },
  emits: ["fitting"],
  setup(props) {
    const ImgUrl = ref("");

    const Urls = ref("");

    let warn_alert = ref(false);

    let success_alert = ref(false);

    const state = reactive({
      clothes: [],
      subscribers: [],
      clientData: computed(() => {
        const { clientData } = getConnectionData();
        return Number(clientData.split(" ")[1]);
      }),
      friendsClothes: [],
      errorStatus: false,
    });

    // publisher data 객체만 추출
    const getConnectionData = () => {
      const { connection } = props.publisher.stream;
      return JSON.parse(connection.data);
    };

    // subscriber data 추출
    const splitName = (subscribe) => {
      const { connection } = subscribe.stream;
      const parse = JSON.parse(connection.data);
      const { clientData } = parse;
      return clientData;
    };

    const store = useStore();

    // 내 id store/login 에서 가져오기
    const myId = computed(() => {
      return Number(store.state.user.user_id);
    });

    // 옷 이미지 주소 입력 후 등록
    const AddUrl = () => {
      if (ImgUrl.value.length < 1) {
        alert("이미지 주소를 입력하세요");
      } else {
        // alert('옷장에 추가되었습니다.')
        axios({
          method: "POST",
          url: `clothes`,
          data: { imageUrl: ImgUrl.value, shoppingRoomId: props.mySessionId },
        })
          .then((res) => {
            console.log(res);
            state.clothes.push(res.data.data);
            success_alert.value = true;
          })
          .catch((err) => {
            console.log(err);
            if (err.response.status == 400) {
              warn_alert.value = true;
            }
          });
        ImgUrl.value = "";
      }
    };

    watch(success_alert, () => {
      if (success_alert.value) setTimeout(() => (success_alert.value = false), 3000);
    });

    watch(warn_alert, () => {
      if (warn_alert.value) setTimeout(() => (warn_alert.value = false), 3000);
    });

    // mySessionId번 방의 내 memberId(myId) 의 옷 리스트 GET 요청
    const getClothes = (myId) => {
      axios({
        method: "GET",
        url: `clothes/${props.mySessionId}/${myId}`,
      }).then((res) => {
        console.log(res);
        state.clothes = res.data.data;
        console.log(state.clothes);
      });
    };

    const getFriendsClothes = (subscribe) => {
      state.friendsClothes = [];
      axios({
        method: "GET",
        url: `clothes/${props.mySessionId}/${splitName(subscribe).split(" ")[1]}`,
      }).then((res) => {
        console.log(res);
        state.friendsClothes = res.data.data;
      });
    };

    // 내 옷 삭제
    const deleteCloth = (clothes, index) => {
      axios({
        method: "DELETE",
        url: `clothes/${clothes[index].clothId}`,
      }).then((res) => {
        console.log(res);
        clothes.splice(index, 1);
      });
    };

    // created
    const createdGetClothes = () => {
      const userId = store.state.user.user_id;
      axios({
        method: "GET",
        url: `clothes/${props.mySessionId}/${userId}`,
      }).then((res) => {
        if (res.data.data) {
          state.clothes = res.data.data
        }
      });
    }

    createdGetClothes()

    return {
      ImgUrl,
      AddUrl,
      Urls,
      getClothes,
      state,
      myId,
      getConnectionData,
      splitName,
      getFriendsClothes,
      deleteCloth,
      warn_alert,
      success_alert,
      createdGetClothes
    };
  },
};
</script>

<style scoped>
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
  width: 200px;
  box-shadow: 0 0 0 0;
}

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
  background-color: #fdfaf3;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}

.clth-style {
  width: 300px;
  height: 300px;
}

.warn-alert {
  position: absolute;
  left: 80%;
  top: 6%;
}

.success-alert {
  position: absolute;
  left: 80%;
  top: 6%;
}

.accordion-body {
  height: 634px;
}

.accordion-button {
  font-weight: bold;
  font-size: 0.65em;
  height: 60px;
}
.accordion-button.collapsed {
  background-color: #703b00;
  color: #eac59e;
}

.accordion-button {
  color: #703b00;
  background-color: #eac59e;
}

.accordion-button.collapsed::after {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%23fff'%3e%3cpath fill-rule='evenodd' d='M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z'/%3e%3c/svg%3e");
}

.accordion-button:after {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%23fff'%3e%3cpath fill-rule='evenodd' d='M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z'/%3e%3c/svg%3e");
}

.accordion-button:not(.collapsed) {
  color: none;
}

.accordion-button:focus {
  z-index: 3;
  border-color: none;
  outline: 0;
  box-shadow: none;
}

#hanger {
  margin-left: 10px;
  width: 24px;
  height: 24px;
}
</style>
