import client from './client'

export function signUp(payload) {
  return client.post('/member/sign-up', payload)
}

export function signIn(payload) {
  return client.post('/member/sign-in', payload)
}
