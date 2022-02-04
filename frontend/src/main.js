import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from "./store";


createApp(App).use(router).use(store).mount('#app');

// Kakao developers에서 발급받은 JavaScript Key
window.Kakao.init("fa7b3aabcb61a2c3abd3809129a8ea8f");

// Naver Developers에서 발급받은 key
//"skkqxLCLHDQrwOlhytTS"
