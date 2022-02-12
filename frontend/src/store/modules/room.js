import axios from '../../api/axios';

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
      loadShoppingRoomList({ commit }) {
        axios.get(
          'shopping-rooms/',
        )
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