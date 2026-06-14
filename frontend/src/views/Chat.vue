<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const roomId = ref('')

function enter() {
  const id = String(roomId.value).trim()
  if (!id) return
  router.push(`/chat/${id}`)
}
</script>

<template>
  <div class="auth">
    <div class="card">
      <h1>채팅방 입장</h1>
      <p class="subtitle">같은 방 번호로 입장한 사용자끼리 대화합니다</p>
      <form @submit.prevent="enter">
        <div class="field">
          <label>방 번호</label>
          <input v-model="roomId" type="number" min="1" placeholder="예: 1" />
        </div>
        <button class="btn" type="submit">입장하기</button>
      </form>
      <p class="switch">
        방 목록·생성 API는 아직 백엔드에 없어, 번호로 직접 입장합니다.
      </p>
    </div>
  </div>
</template>
