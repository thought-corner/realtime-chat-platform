package com.project.realtime_chat_platform.controller.dto.chat

/**
 * `/topic/{roomId}` 채널로 함께 흐르는 이벤트의 종류 판별자.
 *
 * 클라이언트는 이 값으로 새 채팅 메시지([ChatMessageResponse])와 읽음 갱신([MessageReadResponse])을 구분한다.
 */
enum class ChatTopicEventType {
    MESSAGE,
    READ,
}
