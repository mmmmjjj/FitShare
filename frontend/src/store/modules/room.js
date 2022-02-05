import axios from 'axios';
// import { createStore } from "vuex" ;

const room = {
    namespaced: true,

    state: () => ({
      shoppingMallList: [],
    }),

    mutations: {
      LOAD_SHOPPING_MALL_LIST: function (state, results) {
        state.shoppingMallList.value = results
        console.log(state.shoppingMallList)
      }
    },

    actions: {
      loadShoppingMallList( {commit} ) {
        console.log('action!')
        axios({
          method: 'get',
          url: 'http://i6a405.p.ssafy.io:8081/api/v1/shopping-rooms/',
          headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3NDc3NzYyfQ.tRLXFW9wHHIXCrJotone8gsjsi5Vba6zWvIQGCUtZWFrYZw3F9OaHLDeDQ9ZSOpn9E9y2OrLiDuHazuSTd4yAw` }
        })
          .then(res => {
            console.log(res.data)
            commit('LOAD_SHOPPING_MALL_LIST', res.data.data)
          })
      //     .then(() => {
      //       emit('first-host-closet', state.shoppingRoomList[0])
      //     })
      },

    },
    getters: {
    },
}

export default room;