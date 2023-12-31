package com.example.demo.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("jwt")
class JwtController {
    @GetMapping("/security")
    fun securityJwtTest() = ResponseEntity.ok("security ok")

    @GetMapping("/interceptor")
    fun interceptorJwtTest() = ResponseEntity.ok("interceptor ok")

    @GetMapping("/pass")
    fun jwtTest() = ResponseEntity.ok("pass ok")

}