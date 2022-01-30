<template>
  <div>
    <!-- 친구 이름 검색 input-box -->
    <input
      class="input-box"
      type="text"
      v-model="SearchFriend"
      placeholder="이름 검색"
    >

    <!-- Filtering 된 친구만 보여주기 FriendFILTER component로 props -->
    <filtered-friend-list :FilteredFriends="FilteredFriends"></filtered-friend-list>

  </div>
</template>

<script>
import { ref, computed } from 'vue'
import FilteredFriendList from './FilteredFriendList.vue'

export default {
  name: 'FriendListTab',
  components: {
    FilteredFriendList,
  },
  setup() {
    const friends = ref([
      {id: 1, name: '김싸피', src: require('@/assets/friend_icon.png')},
      {id: 2, name: '최싸피', src: require('@/assets/friend_icon.png')},
      {id: 3, name: '허싸피', src: require('@/assets/friend_icon.png')},
      {id: 4, name: '안싸피', src: require('@/assets/friend_icon.png')},
      {id: 5, name: '박싸피', src: require('@/assets/friend_icon.png')},
      {id: 6, name: '강싸피', src: require('@/assets/friend_icon.png')}
    ])

    const SearchFriend = ref('');
    const FilteredFriends = computed(() => {
      if (SearchFriend.value) {
        return friends.value.filter(friend => {
          return friend.name.includes(SearchFriend.value)
        })
      }
      return friends.value
    });

    return {
      friends,
      SearchFriend,
      FilteredFriends
    }
  }
}
</script>

<style scoped>
.profile-img {
  width: 60px;
  height: 60px;
}

.input-box {
  width: 301px;
  border-radius: 20px;
  text-align: center;
}

input::placeholder {
  text-align: center;
}
</style>