// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'


const ws = require('./assets/ws')

Vue.config.productionTip = false
// Vue.require(ac)

/* eslint-disable no-new */
window.vueinstance = new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  methods: {
    testMethod: function(action) {
        this.$emit('propagateAction', action);
    },
    loginRedirect: function() {
        this.$router.push('/login');
    }
  }
})
