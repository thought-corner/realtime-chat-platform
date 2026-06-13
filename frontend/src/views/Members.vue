<script setup>
import { ref, onMounted } from 'vue'
import { fetchMembers } from '../api/member'

const members = ref([])
const error = ref('')

onMounted(async () => {
  try {
    const { data } = await fetchMembers()
    members.value = data
  } catch (e) {
    error.value = e.response?.data?.message ?? '목록을 불러오지 못했습니다.'
  }
})

function initial(name) {
  return name ? name.charAt(0).toUpperCase() : '?'
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
        <div>
          <div class="member__name">{{ m.name }}</div>
          <div class="member__email">{{ m.email }}</div>
        </div>
      </li>
    </ul>
  </div>
</template>
