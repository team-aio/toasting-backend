package io.toasting.global.config

import io.toasting.global.security.jwt.JwtFactory
import io.toasting.global.security.jwt.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun corsConfigSource(): CorsConfigurationSource {
        val corsConfig = CorsConfiguration()
        corsConfig.apply {
            allowedHeaders = listOf("*")
            allowedOrigins =
                listOf(
                    "http://localhost:3000",
                    "https://toasting.io",
                    "http:://toasting.io",
                )
            exposedHeaders = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowCredentials = true
            maxAge = 3600
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return source
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtFactory: JwtFactory,
    ): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigSource()) }
            .formLogin { it.disable() } // cors 설정 활성화
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        http
            .authorizeHttpRequests {
                it
                    .requestMatchers(existMemberMatcher())
                    .permitAll()
                it
                    .requestMatchers(getProfileMatcher())
                    .permitAll()
                it
                    .requestMatchers(
                        "/",
                        "/h2-console/**",
                        "/favicon.ico",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/api-test/**",
                        "/v1/member/login/google",
                        "/v1/member/signup",
                        "/v1/member/exist?nickname=**",
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }.addFilterBefore(JwtFilter(jwtFactory), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    private fun existMemberMatcher() =
        RequestMatcher { request ->
            request.requestURI == "/v1/member/exist" &&
                request.getParameter("nickname")?.isNotEmpty() ?: false
        }

    private fun getProfileMatcher() =
        RequestMatcher { request ->
            request.requestURI == "/v1/member/profile" &&
                request.getParameter("memberId")?.isNotEmpty() ?: false
        }
}
