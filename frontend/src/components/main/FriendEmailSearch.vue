<template>
  <div>
    <input
      class="input-box"
      type="text"
      v-model="SearchFriend"
      placeholder="이름 또는 이메일 검색"
    >
  </div>
</template>

<script>
export default {
  name: 'FriendEmailSearch'
}
</script>

<style>
.input-box {
  width: 301px;
  border-radius: 20px;
}

input::placeholder {
  text-align: center;
}

</style>