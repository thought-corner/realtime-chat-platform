<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { fetchMembers } from '../api/member'
import { getOrCreatePrivateRoom } from '../api/chat'
import { getEmail } from '../session'

const router = useRouter()
const members = ref([])
const error = ref('')
const myEmail = getEmail()

onMounted(async () => {
  try {
    const { data } = await fetchMembers()
    members.value = data.members
  } catch (e) {
    error.value = e.response?.data?.message ?? '목록을 불러오지 못했습니다.'
  }
})

function initial(name) {
  return name ? name.charAt(0).toUpperCase() : '?'
}

async function startPrivateChat(memberId) {
  try {
    const { data: roomId } = await getOrCreatePrivateRoom(memberId)
    router.push(`/chat/${roomId}`)
  } catch (e) {
    error.value = e.response?.data?.message ?? '1:1 채팅방을 열 수 없습니다.'
  }
}
</script>

<template>
  <div class="container">
    <h1 class="page-title">회원 목록</h1>
    <p v-if="error" class="error">{{ error }}</p>
    <p v-else-if="members.length === 0" class="empty">등록된 회원이 없습니다.</p>
    <ul class="member-list">
      <li v-for="m in members" :key="m.id" class="member">
        <div class="avatar">{{ initial(m.name) }}</div>
        <div class="member__body">
          <div class="member__name">{{ m.name }}</div>
          <div class="member__email">{{ m.email }}</div>
        </div>
        <button
          v-if="m.email !== myEmail"
          class="btn btn--sm"
          @click="startPrivateChat(m.id)"
        >
          1:1 채팅
        </button>
      </li>
    </ul>
  </div>
</template>
