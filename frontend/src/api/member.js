import client from './client'

export function fetchMembers() {
  return client.get('/member/list')
}
