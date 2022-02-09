export const friend = {
  namespaced: true,
	state: () => ({
		friends: [],
	}),
	getters: {
	},
	mutations: {
		GET_FRIEND_LIST (state, friend) {
			console.log(friend)
			state.friends = friend
		},
		GET_FRIEND_LIST_BY_CLICK (state, friend) {
			console.log(friend)
			state.friends = friend
		},
		GET_FRIEND_BY_NAME (state, friendbyname) {
			console.log(friendbyname)
			state.friends = friendbyname
		}
	},
	actions: {
		getfriends(context, friend) {
			console.log(context)
      console.log('hi')
			console.log(friend)
			context.commit('GET_FRIEND_LIST', friend)
		},
		getfriendsbyclick(context, friend) {
			console.log(context, friend)
			context.commit('GET_FRIEND_LIST_BY_CLICK', friend)
		},
		getfriendsbyname(context, friendbyname) {
			console.log(context, friendbyname)
			context.commit('GET_FRIEND_BY_NAME', friendbyname)
		}
	},
}