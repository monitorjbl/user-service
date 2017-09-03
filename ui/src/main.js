import 'vue-material/dist/vue-material.css'
import Vue from 'vue'
import VueMaterial from 'vue-material'
import App from './App.vue'
import router from './router'

Vue.use(VueMaterial);
Vue.config.productionTip = false;

new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
});
