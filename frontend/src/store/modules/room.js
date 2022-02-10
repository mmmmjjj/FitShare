import axios from 'axios';

export const room = {
    namespaced: true,
    state: () => ({
      shoppingRoomList: [],
      selectedShoppingRoom: {},
    }),

    mutations: {
      LOAD_SHOPPING_ROOM_LIST(state, roomList) {
        state.shoppingRoomList = roomList
        state.selectedShoppingRoom = roomList[0]
      },
      SELECTED_ROOM(state, room) {
        state.selectedShoppingRoom = room
      }
    },
    actions: {
      loadShoppingRoomList({commit}, config) {
        axios({
          method: 'get',
          url: 'http://i6a405.p.ssafy.io:8081/api/v1/shopping-rooms/',
          headers: config,
        })
          .then(res => {
            commit('LOAD_SHOPPING_ROOM_LIST', res.data.data)
          })
          .catch(err => console.log(err));
      },
      selectedRoom({commit}, room) {
        commit('SELECTED_ROOM', room)
      }
    },
    getters: {
    },
}