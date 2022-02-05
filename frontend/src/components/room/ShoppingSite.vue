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
      // document.cookie = "safeCookie1=foo; SameSite=Lax"
      // document.cookie = "safeCookie2=foo"
      // document.cookie = "crossCookie=bar; SameSite=None; Secure";
      
      document.cookie = "SameSite=None; Secure"
      
      // document.cookie = 'cookie1=value1; SameSite=Lax';
      // document.cookie = 'cookie2=value2; SameSite=None; Secure';

      return {
      }
    }

}
</script>

<style>

iframe {
  background-color: white;
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