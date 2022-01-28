<template>
  <div>
    <!-- 친구 이름, 이메일 검색 input-box -->
    <input
      class="input-box"
      type="text"
      v-model="SearchUser"
      placeholder="이름 또는 이메일 검색"
    >

    <!-- 이름, 이메일로 검색 시 나오는 users들 components -->
    <searched-users :SearchedUsers="SearchedUsers"></searched-users>


  </div>
</template>

<script>
import SearchedUsers from './SearchedUsers.vue'
import { ref, computed } from 'vue'

export default {
  name: 'FriendMakeTab',
  components: {
    SearchedUsers,
  },
  setup() {
    const friends = ref([
      {id: 1, name: '김싸피', src: require('@/assets/friend_icon.png'), email: 'ssafykim@samsung.com'},
      {id: 2, name: '이싸피', src: require('@/assets/friend_icon.png'), email: 'ssafylee@samsung.com'},
      {id: 3, name: '박싸피', src: require('@/assets/friend_icon.png'), email: 'ssafypark@samsung.com'},
      {id: 4, name: '최싸피', src: require('@/assets/friend_icon.png'), email: 'ssafychoi@samsung.com'},
      {id: 5, name: '양싸피', src: require('@/assets/friend_icon.png'), email: 'ssafyyang@samsung.com'},
      {id: 6, name: '오싸피', src: require('@/assets/friend_icon.png'), email: 'ssafyo@samsung.com'},
      {id: 7, name: '김싸피2', src: require('@/assets/friend_icon.png'), email: 'ssafykim2@samsung.com'},
    ])

    const SearchUser = ref('');
    const SearchedUsers = computed(() => {
      if (SearchUser.value) {
        return friends.value.filter(friend => {
          return friend.name.includes(SearchUser.value)
        })
      }
      return ''
    });

    return {
      friends,
      SearchUser,
      SearchedUsers,
    }
  }
}
</script>

<style>
.result {
  text-align: center;
  font-size: 12px;
  color: #565656;
}

img {
  width: 60px;
  height: 60px;
}

</style>