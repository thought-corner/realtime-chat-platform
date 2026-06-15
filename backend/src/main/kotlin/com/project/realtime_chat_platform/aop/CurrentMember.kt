package com.project.realtime_chat_platform.aop

/**
 * 인증된 현재 회원의 이메일을 컨트롤러 파라미터로 주입받기 위한 애너테이션.
 *
 * 인증 정보(SecurityContext) 해석을 컨트롤러 경계에서 처리하여, 서비스 계층이
 * 보안 컨텍스트에 직접 의존하지 않고 이메일을 인자로만 받도록 한다.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CurrentMember
