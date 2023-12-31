package com.example.demo.common.security

import org.springframework.security.core.GrantedAuthority

class CustomAuthority(
    private val userId: String,
    private val issuer: String) : GrantedAuthority {
    override fun getAuthority(): String {
        return "ROLE_USER_${userId}_${issuer}"
    }
}
