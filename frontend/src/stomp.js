import { Client } from '@stomp/stompjs'
import { getToken } from './session'

// 백엔드 STOMP 핸드셰이크 엔드포인트(/connect)는 raw WebSocket이므로
// http(s) → ws(s)로 스킴을 바꿔 brokerURL을 만든다. (SockJS 미사용)
const WS_BASE = import.meta.env.VITE_API_BASE_URL.replace(/^http/, 'ws')

/**
 * 설정된 STOMP 클라이언트를 생성한다. activate()는 호출 측에서 수행한다.
 * @param {{ onConnect?: Function, onError?: Function }} handlers
 */
export function createStompClient({ onConnect, onError } = {}) {
  const token = getToken()
  return new Client({
    brokerURL: `${WS_BASE}/connect`,
    connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
    reconnectDelay: 5000,
    onConnect,
    onStompError: onError,
    onWebSocketError: onError,
  })
}
