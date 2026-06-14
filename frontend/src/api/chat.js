import client from './client'

// 그룹 채팅방 전체 목록
export function fetchGroupRooms() {
  return client.get('/chat/room/group/list')
}

// 그룹 채팅방 생성 (roomName은 RequestParam)
export function createGroupRoom(roomName) {
  return client.post('/chat/room/group/create', null, { params: { roomName } })
}

// 그룹 채팅방 참여
export function joinGroupRoom(roomId) {
  return client.post(`/chat/room/group/${roomId}/join`)
}

// 그룹 채팅방 나가기
export function leaveGroupRoom(roomId) {
  return client.delete(`/chat/room/group/${roomId}/leave`)
}

// 내가 참여 중인 채팅방 목록 (안 읽은 메시지 수 포함)
export function fetchMyRooms() {
  return client.get('/chat/my/rooms')
}

// 채팅방 메시지 이력
export function fetchHistory(roomId) {
  return client.get(`/chat/history/${roomId}`)
}

// 채팅방 메시지 읽음 처리
export function markRead(roomId) {
  return client.post(`/chat/room/${roomId}/read`)
}

// 1:1 채팅방 조회 또는 생성 (otherMemberId는 RequestParam). 응답 본문은 roomId(Long).
export function getOrCreatePrivateRoom(otherMemberId) {
  return client.post('/chat/room/private/create', null, { params: { otherMemberId } })
}
