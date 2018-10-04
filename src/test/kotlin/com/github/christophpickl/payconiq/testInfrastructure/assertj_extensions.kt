package com.github.christophpickl.payconiq.testInfrastructure

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <SELF : AbstractAssert<SELF, ResponseEntity<String>>> AbstractAssert<SELF, ResponseEntity<String>>.hasStatusCode(expectedStatusCode: Int) {
    satisfies {
        Assertions.assertThat(it.statusCodeValue)
            .describedAs("Status code mismatch! Response body was: <<<${it.body}>>>")
            .isEqualTo(expectedStatusCode)
    }
}

fun <SELF : AbstractAssert<SELF, ResponseEntity<String>>> AbstractAssert<SELF, ResponseEntity<String>>.hasStatusCode(expectedStatusCode: HttpStatus) {
    hasStatusCode(expectedStatusCode.value())
}
