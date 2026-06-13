<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { signIn } from '../api/auth'
import { setToken } from '../session'

const email = ref('')
const password = ref('')
const error = ref('')
const router = useRouter()

async function submit() {
  error.value = ''
  try {
    const { data } = await signIn({ email: email.value, password: password.value })
    setToken(data.token)
    router.push('/members')
  } catch (e) {
    error.value = e.response?.data?.message ?? '로그인에 실패했습니다.'
  }
}
</script>

<template>
  <div class="auth">
    <div class="card">
      <h1>로그인</h1>
      <p class="subtitle">계정에 로그인하세요</p>
      <form @submit.prevent="submit">
        <div class="field">
          <label>이메일</label>
          <input v-model="email" type="email" placeholder="you@example.com" />
        </div>
        <div class="field">
          <label>비밀번호</label>
          <input v-model="password" type="password" placeholder="••••••••" />
        </div>
        <button class="btn" type="submit">로그인</button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
      <p class="switch">계정이 없으신가요? <RouterLink to="/sign-up">회원가입</RouterLink></p>
    </div>
  </div>
</template>
