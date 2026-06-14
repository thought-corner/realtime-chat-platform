import { createRouter, createWebHistory } from 'vue-router'
import SignUp from '../views/SignUp.vue'
import SignIn from '../views/SignIn.vue'
import Members from '../views/Members.vue'
import Chat from '../views/Chat.vue'
import ChatRoom from '../views/ChatRoom.vue'
import { getToken } from '../session'

const routes = [
  { path: '/', redirect: () => (getToken() ? '/members' : '/sign-in') },
  { path: '/sign-up', component: SignUp },
  { path: '/sign-in', component: SignIn },
  { path: '/members', component: Members, meta: { requiresAuth: true } },
  { path: '/chat', component: Chat, meta: { requiresAuth: true } },
  { path: '/chat/:roomId', component: ChatRoom, meta: { requiresAuth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !getToken()) {
    return '/sign-in'
  }
})

export default router
