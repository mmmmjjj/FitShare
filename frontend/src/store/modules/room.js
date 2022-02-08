import axios from 'axios';
// import { createStore } from "vuex" ;

const room = {
    namespaced: true,

    state: () => ({
      shoppingMallList: [],
    }),

    mutations: {
      LOAD_SHOPPING_MALL_LIST: function (state, results) {
        console.log(results)
        state.shoppingMallList = results
        
      }
    },

    actions: {
      loadShoppingMallList( {commit} ) {
        console.log('action!')
        axios({
          method: 'get',
          url: 'http://i6a405.p.ssafy.io:8081/api/v1/shopping-rooms/',
          headers: { Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0Iiwicm9sZXMiOiJVU0VSIiwiZXhwIjoxNjQ3OTA5NTI5fQ.l1TfGZtQarYUWrLy6uI-6gFLX5CVQn62t28USVkJe0_kazLFL824YCDLrGbxx1hAhBWe5lxbtK5SArTgOP77uA` }
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