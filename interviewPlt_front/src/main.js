// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui'
import '@/assets/element-variables.scss'
import './icons/iconfont.css'
import App from './App'
import router from './router'
import VueCodeMirror from 'vue-codemirror'
import './directives.js'
import axios from 'axios'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueCodeMirror)
Vue.prototype.$BASE_URL = 'http://localhost:8000'
Vue.prototype.$axios = axios

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
