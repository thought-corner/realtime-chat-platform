<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { signUp } from '../api/auth'

const name = ref('')
const email = ref('')
const password = ref('')
const error = ref('')
const router = useRouter()

async function submit() {
  error.value = ''
  try {
    await signUp({ name: name.value, email: email.value, password: password.value })
    router.push('/sign-in')
  } catch (e) {
    error.value = e.response?.data?.message ?? '회원가입에 실패했습니다.'
  }
}
</script>

<template>
  <div class="auth">
    <div class="card">
      <h1>회원가입</h1>
      <p class="subtitle">새 계정을 만드세요</p>
      <form @submit.prevent="submit">
        <div class="field">
          <label>이름</label>
          <input v-model="name" placeholder="홍길동" />
        </div>
        <div class="field">
          <label>이메일</label>
          <input v-model="email" type="email" placeholder="you@example.com" />
        </div>
        <div class="field">
          <label>비밀번호</label>
          <input v-model="password" type="password" placeholder="••••••••" />
        </div>
        <button class="btn" type="submit">가입하기</button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
      <p class="switch">이미 계정이 있으신가요? <RouterLink to="/sign-in">로그인</RouterLink></p>
    </div>
  </div>
</template>
