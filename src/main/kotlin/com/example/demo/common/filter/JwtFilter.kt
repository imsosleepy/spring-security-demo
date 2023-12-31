package com.example.demo.common.filter

import com.example.demo.common.security.CustomAuthority
import io.jsonwebtoken.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter : OncePerRequestFilter() {
    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token = extractJwtTokenFromHeader(request)

        if (token != null && validateToken(token)) {
            val authentication = getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(token: String): Authentication {
        val claims: Claims = getClaims(token)

        val userId = claims["userId"].toString()
        // 커스텀 인증 생성(임의의 값을 생성함)
        val customAuthority = CustomAuthority(userId, claims.issuer)
        val authorities: Collection<GrantedAuthority> = listOf(customAuthority)

        return UsernamePasswordAuthenticationToken(userId, "", authorities)
    }

    fun extractJwtTokenFromHeader(request: HttpServletRequest): String? {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        val parts = authorization.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (parts.size != 2) {
            null
        } else parts[1]
    }

    private fun validateToken(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> {}  // Invalid JWT Token
                is MalformedJwtException -> {}  // Invalid JWT Token
                is ExpiredJwtException -> {}    // Expired JWT Token
                is UnsupportedJwtException -> {}    // Unsupported JWT Token
                is IllegalArgumentException -> {}   // JWT claims string is empty
                else -> {}  // else
            }
            println(e.message)
        }
        return false
    }

    fun getClaims(token: String): Claims =
        Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
}