import { ref, computed } from 'vue'

const TOKEN_KEY = 'accessToken'
const EMAIL_KEY = 'email'
const token = ref(localStorage.getItem(TOKEN_KEY))
const email = ref(localStorage.getItem(EMAIL_KEY))

export const isAuthenticated = computed(() => !!token.value)

export function getToken() {
  return token.value
}

export function setToken(value) {
  token.value = value
  localStorage.setItem(TOKEN_KEY, value)
}

export function getEmail() {
  return email.value
}

export function setEmail(value) {
  email.value = value
  localStorage.setItem(EMAIL_KEY, value)
}

export function clearToken() {
  token.value = null
  email.value = null
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(EMAIL_KEY)
}
