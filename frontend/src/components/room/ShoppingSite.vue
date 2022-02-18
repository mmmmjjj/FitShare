<template>
  <div>
    <iframe id="example" class="site" title="example" :src="shoppingMallUrl"
      frameborder="0" width="859" height="780">
    </iframe>
  </div>
</template>

<script>
export default {
    name: 'ShoppingSite',
    props: {
      shoppingMallUrl: String,
    },

    setup() {
      document.cookie = "SameSite=None; Secure";
      
      return {}
    }

}
</script>

<style scoped>
div {
  position: relative;
  width: 100%;
}

iframe {
  position: absolute;
  background-color: white;
  width: 100%;
  height: 100%;
}

.site::-webkit-scrollbar {
  width: 7px;
}
.site::-webkit-scrollbar-track {
  background-color: transparent;
}
.site::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #D3E2E7;
}
</style>