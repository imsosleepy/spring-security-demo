package com.example.demo.common.interceptor

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class JwtTokenProvider {
    fun extractJwtTokenFromHeader(request: HttpServletRequest): String? {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        val parts = authorization.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (parts.size != 2) {
            null
        } else parts[1]
    }
}