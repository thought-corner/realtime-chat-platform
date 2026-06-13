import { ref, computed } from 'vue'

const TOKEN_KEY = 'accessToken'
const token = ref(localStorage.getItem(TOKEN_KEY))

export const isAuthenticated = computed(() => !!token.value)

export function getToken() {
  return token.value
}

export function setToken(value) {
  token.value = value
  localStorage.setItem(TOKEN_KEY, value)
}

export function clearToken() {
  token.value = null
  localStorage.removeItem(TOKEN_KEY)
}
