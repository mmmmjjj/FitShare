<template>
  <div>
    <!-- <router-link to="/main">Main</router-link> |
    <router-link to="/create">CreateRoom</router-link> | -->
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'App',
  components: {

  }
}
</script>

<style>
#app {
  /* text-align: center;
  color: #2c3e50;
  margin-top: 60px; */
  font-family: 'MapoPeacefull';
}

@font-face {
    font-family: 'MapoPeacefull';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoPeacefullA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
</style>
