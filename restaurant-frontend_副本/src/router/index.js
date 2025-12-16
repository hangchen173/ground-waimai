import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import CustomerView from '../views/CustomerView.vue'
import RestaurantView from '../views/RestaurantView.vue'
import TableView from '../views/TableView.vue'
import ReservationView from '../views/ReservationView.vue'


const routes = [
  { path: '/login', component: Login },
  { 
    path: '/', 
    component: Layout,
    redirect: '/customers',
    children: [
        { path: 'customers', component: CustomerView },
        { path: 'restaurants', component: RestaurantView },
        { path: 'tables', component: TableView },
        // 👇 2. 启用路由
        { path: 'reservations', component: ReservationView }
    ]
  }
]
const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) next('/login')
  else next()
})

export default router