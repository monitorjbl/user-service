import Vue from 'vue'
import Router from 'vue-router'
import UserList from '../components/UserList.vue'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: UserList
    }
  ]
});
