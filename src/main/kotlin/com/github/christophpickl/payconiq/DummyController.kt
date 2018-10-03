package com.github.christophpickl.payconiq

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(value = ["/dummy"])
class DummyController {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun dummy() = """{ "it": "works" }"""
}
