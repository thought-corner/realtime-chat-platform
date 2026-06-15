<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createStompClient } from '../stomp'
import { getEmail } from '../session'
import { fetchHistory, markRead } from '../api/chat'

const route = useRoute()
const router = useRouter()
const roomId = route.params.roomId
const myEmail = getEmail()

const messages = ref([])
const newMessage = ref('')
const connected = ref(false)
const error = ref('')
const chatBox = ref(null)

let client = null

function scrollToBottom() {
  nextTick(() => {
    if (chatBox.value) chatBox.value.scrollTop = chatBox.value.scrollHeight
  })
}

onMounted(async () => {
  // 1) 과거 메시지 이력을 REST로 먼저 채우고 읽음 처리
  try {
    const { data } = await fetchHistory(roomId)
    messages.value = data.messages
    scrollToBottom()
    await markRead(roomId)
  } catch (e) {
    error.value = e.response?.data?.message ?? '메시지 이력을 불러오지 못했습니다.'
  }

  // 2) STOMP로 실시간 구독 시작
  client = createStompClient({
    onConnect: () => {
      connected.value = true
      // /topic/{roomId} 구독: 새 메시지(MESSAGE)와 읽음 갱신(READ)을 함께 수신
      client.subscribe(`/topic/${roomId}`, (frame) => {
        const payload = JSON.parse(frame.body)
        if (payload.type === 'READ') {
          // 읽음 이벤트: 해당 메시지들의 안 읽은 인원 수를 실시간 갱신
          payload.updates.forEach((u) => {
            const msg = messages.value.find((m) => m.id === u.messageId)
            if (msg) msg.unreadCount = u.unreadCount
          })
        } else {
          // 새 메시지
          messages.value.push(payload)
          scrollToBottom()
          // 방을 보고 있는 중 도착한 남의 메시지는 즉시 읽음 처리
          // → 백엔드가 READ 이벤트를 브로드캐스트해 보낸 사람의 안 읽은 수가 실시간으로 줄어든다.
          if (payload.senderEmail !== myEmail) {
            markRead(roomId)
          }
        }
      })
    },
    onError: () => {
      connected.value = false
      error.value = '서버 연결에 실패했습니다.'
    },
  })
  client.activate()
})

onBeforeUnmount(() => {
  if (client) client.deactivate()
})

function send() {
  const text = newMessage.value.trim()
  if (!text || !connected.value) return
  // /publish/{roomId}로 발행 → 백엔드 @MessageMapping("/{roomId}")가 처리
  client.publish({
    destination: `/publish/${roomId}`,
    body: JSON.stringify({ senderEmail: myEmail, message: text }),
  })
  newMessage.value = ''
}
</script>

<template>
  <div class="container">
    <div class="chat-header">
      <button class="btn btn--ghost btn--sm" @click="router.push('/chat')">← 나가기</button>
      <h1 class="page-title chat-title">채팅방 #{{ roomId }}</h1>
      <span class="status" :class="connected ? 'status--on' : 'status--off'">
        {{ connected ? '연결됨' : '연결 중…' }}
      </span>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div ref="chatBox" class="chat-box">
      <p v-if="messages.length === 0" class="empty">아직 메시지가 없습니다.</p>
      <div
        v-for="(msg, index) in messages"
        :key="index"
        class="chat-message"
        :class="msg.senderEmail === myEmail ? 'sent' : 'received'"
      >
        <span
          v-if="msg.senderEmail === myEmail && msg.unreadCount > 0"
          class="unread-count"
        >
          {{ msg.unreadCount }}
        </span>
        <div class="bubble">
          <div class="sender">{{ msg.senderEmail }}</div>
          <div class="text">{{ msg.message }}</div>
        </div>
      </div>
    </div>

    <form class="chat-input" @submit.prevent="send">
      <input v-model="newMessage" placeholder="메시지를 입력하세요" :disabled="!connected" />
      <button class="btn btn--sm" type="submit" :disabled="!connected">전송</button>
    </form>
  </div>
</template>
