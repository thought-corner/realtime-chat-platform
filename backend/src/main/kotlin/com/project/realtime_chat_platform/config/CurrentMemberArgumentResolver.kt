package com.project.realtime_chat_platform.config

import com.project.realtime_chat_platform.aop.CurrentMember
import org.springframework.core.MethodParameter
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * [com.project.realtime_chat_platform.aop.CurrentMember]이 붙은 String 파라미터에 인증된 회원의 이메일(인증 principal name)을 주입한다.
 */
@Component
class CurrentMemberArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentMember::class.java) &&
            parameter.parameterType == String::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val authentication =
            SecurityContextHolder.getContext().authentication
                ?: throw AuthenticationCredentialsNotFoundException("인증 정보가 없습니다.")
        return authentication.name
    }
}
