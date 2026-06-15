<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  fetchMyRooms,
  fetchGroupRooms,
  createGroupRoom,
  joinGroupRoom,
  leaveGroupRoom,
} from '../api/chat'

const router = useRouter()

const myRooms = ref([])
const groupRooms = ref([])
const newRoomName = ref('')
const error = ref('')

function readMessage(e, fallback) {
  return e.response?.data?.message ?? fallback
}

async function load() {
  error.value = ''
  try {
    const [mine, groups] = await Promise.all([fetchMyRooms(), fetchGroupRooms()])
    myRooms.value = mine.data.rooms
    groupRooms.value = groups.data.rooms
  } catch (e) {
    error.value = readMessage(e, '채팅방 목록을 불러오지 못했습니다.')
  }
}

onMounted(load)

function enter(roomId) {
  router.push(`/chat/${roomId}`)
}

async function create() {
  const name = newRoomName.value.trim()
  if (!name) return
  try {
    await createGroupRoom(name)
    newRoomName.value = ''
    await load()
  } catch (e) {
    error.value = readMessage(e, '채팅방 생성에 실패했습니다.')
  }
}

async function join(roomId) {
  try {
    await joinGroupRoom(roomId)
    enter(roomId)
  } catch (e) {
    error.value = readMessage(e, '채팅방 참여에 실패했습니다.')
  }
}

async function leave(roomId) {
  try {
    await leaveGroupRoom(roomId)
    await load()
  } catch (e) {
    error.value = readMessage(e, '채팅방 나가기에 실패했습니다.')
  }
}
</script>

<template>
  <div class="container">
    <h1 class="page-title">채팅</h1>
    <p v-if="error" class="error">{{ error }}</p>

    <section class="chat-section">
      <h2 class="section-title">내 채팅방</h2>
      <p v-if="myRooms.length === 0" class="empty">참여 중인 채팅방이 없습니다.</p>
      <ul class="room-list">
        <li v-for="room in myRooms" :key="room.roomId" class="room">
          <div class="room__info">
            <span class="room__name">{{ room.roomName }}</span>
            <span class="room__tag">{{ room.isGroupChat === 'Y' ? '그룹' : '1:1' }}</span>
            <span v-if="room.unreadCount > 0" class="badge">{{ room.unreadCount }}</span>
          </div>
          <div class="room__actions">
            <button class="btn btn--sm" @click="enter(room.roomId)">입장</button>
            <button
              v-if="room.isGroupChat === 'Y'"
              class="btn btn--ghost btn--sm"
              @click="leave(room.roomId)"
            >
              나가기
            </button>
          </div>
        </li>
      </ul>
    </section>

    <section class="chat-section">
      <h2 class="section-title">그룹 채팅방</h2>
      <form class="create-room" @submit.prevent="create">
        <input v-model="newRoomName" placeholder="새 그룹 채팅방 이름" />
        <button class="btn btn--sm" type="submit">생성</button>
      </form>
      <p v-if="groupRooms.length === 0" class="empty">개설된 그룹 채팅방이 없습니다.</p>
      <ul class="room-list">
        <li v-for="room in groupRooms" :key="room.roomId" class="room">
          <div class="room__info">
            <span class="room__name">{{ room.roomName }}</span>
            <span class="room__tag">#{{ room.roomId }}</span>
          </div>
          <button class="btn btn--sm" @click="join(room.roomId)">참여하기</button>
        </li>
      </ul>
    </section>
  </div>
</template>
