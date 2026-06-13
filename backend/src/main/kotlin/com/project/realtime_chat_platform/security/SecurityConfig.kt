package com.project.realtime_chat_platform.security

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain =
        httpSecurity
            .cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/member/sign-up", "/member/sign-in", "/connect/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun jwtAuthFilterRegistration(filter: JwtAuthFilter): FilterRegistrationBean<JwtAuthFilter> {
        val registration = FilterRegistrationBean(filter)
        registration.isEnabled = false
        return registration
    }
}
