import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import Video from 'video.js'
import 'video.js/dist/video-js.css'
import axios from 'axios'

Vue.config.productionTip = false
Vue.prototype.$video = Video
Vue.prototype.$BASE_URL = 'http://localhost:8000'
Vue.prototype.$axios = axios

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
