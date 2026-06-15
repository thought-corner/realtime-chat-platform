package com.project.realtime_chat_platform.config

import com.project.realtime_chat_platform.config.CurrentMemberArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Spring MVC 설정. 커스텀 컨트롤러 인자 리졸버를 등록한다.
 */
@Configuration
class WebConfig(
    private val currentMemberArgumentResolver: CurrentMemberArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(currentMemberArgumentResolver)
    }
}
