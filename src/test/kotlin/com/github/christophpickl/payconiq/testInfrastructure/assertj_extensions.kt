package com.github.christophpickl.payconiq.testInfrastructure

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.AbstractObjectAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.ObjectAssert
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.reflect.KProperty1

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

fun <T> ObjectAssert<T>.isEqualToIgnoringGivenProps(expected: T, vararg propsToIgnore: KProperty1<T, Any?>) {
    isEqualToIgnoringGivenFields(expected, *propsToIgnore.map { it.name }.toTypedArray())
}

fun <T> AbstractObjectAssert<*, T>.isEqualToIgnoringGivenProps(expected: T, vararg propsToIgnore: KProperty1<T, Any?>) {
    isEqualToIgnoringGivenFields(expected, *propsToIgnore.map { it.name }.toTypedArray())
}
