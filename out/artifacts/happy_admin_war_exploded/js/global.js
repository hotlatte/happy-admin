// 路由
const Index = Vue.extend({
  template: '#index',
  data() {
    return {

    }
  }
})

const Vote = Vue.extend({
  template: '#vote',
  data() {
    return {
      
    }
  },
  mounted() {
    this.listRender()
  },
  methods: {
    listRender() {
      const $grid = $('.grid').imagesLoaded(function() {
        $grid.masonry()
      })
    }
  }
})

const Upload = Vue.extend({
  template: '#upload',
  data() {
    return {

    }
  }
})

const router = new VueRouter({
  routes: [
    { path: '/', component: Index },
    { path: '/vote', component: Vote },
    { path: '/upload', component: Upload },
  ]
})

// vuex
// const store = new Vuex.Store({
//   // store 的变量
//   state: {
    
//   },
//   // store 的方法
//   mutations: {
    
//   }
// })


new Vue({
  el: '#app',
  router
})