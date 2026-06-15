package com.project.realtime_chat_platform.messaging

import com.fasterxml.jackson.databind.JsonNode

/**
 * Redis Pub/Sub 채널로 오가는 브로드캐스트 봉투(envelope).
 *
 * 어느 방(`roomId`)으로 보낼지와 실제 페이로드를 함께 싣는다. 페이로드는 채팅 메시지/읽음 이벤트 등
 * 종류가 다양하므로 [JsonNode]로 담아 타입에 무관하게 그대로 relay한다. (수신 측은 역직렬화 없이
 * 그대로 STOMP `/topic/{roomId}`로 전달)
 */
data class ChatBroadcastMessage(
    val roomId: Long,
    val payload: JsonNode,
)
