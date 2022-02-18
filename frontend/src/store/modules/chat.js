export const chat = {
    namespaced: true,
    state: () => ({
        recvMsg: [],
    }),
    getters: {
    },
    mutations: {
        SET_RECIVE_MSG(state, recvMsg){
            state.recvMsg.push(recvMsg);
        }
    },
    actions: {
        pushMsg({commit}, msg) {
            commit('SET_RECIVE_MSG', msg);
        }
    }
}