<script setup>
import { useRouter } from 'vue-router'
import { isAuthenticated, clearToken } from './session'

const router = useRouter()

function logout() {
  clearToken()
  router.push('/sign-in')
}
</script>

<template>
  <header class="app-header">
    <div class="app-header__inner">
      <RouterLink to="/" class="brand">Realtime<span>Chat</span></RouterLink>
      <nav class="nav">
        <template v-if="isAuthenticated">
          <RouterLink to="/members">회원목록</RouterLink>
          <button class="btn btn--ghost btn--sm" @click="logout">로그아웃</button>
        </template>
        <template v-else>
          <RouterLink to="/sign-in">로그인</RouterLink>
          <RouterLink to="/sign-up">회원가입</RouterLink>
        </template>
      </nav>
    </div>
  </header>
  <RouterView />
</template>
