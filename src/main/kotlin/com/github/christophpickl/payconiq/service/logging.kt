package com.github.christophpickl.payconiq.service

import mu.KotlinLogging.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.annotation.AnnotationRetention.RUNTIME

@Target(AnnotationTarget.FUNCTION)
@Retention(RUNTIME)
annotation class Logged

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class NoLog

@Aspect
@Component
class LogAspect {

    private val log = logger {}

    @Before("@annotation(com.github.christophpickl.payconiq.service.Logged)")
    fun debugLogMessage(joinPoint: JoinPoint) {
        val targetLog = LoggerFactory.getLogger(joinPoint.target::class.java)

        if (!targetLog.isDebugEnabled) {
            return
        }

        val signature = joinPoint.signature as MethodSignature
        val methodName = signature.name
        val params = formatParameters(joinPoint, signature)

        targetLog.debug("$methodName($params)")
    }

    private fun formatParameters(joinPoint: JoinPoint, signature: MethodSignature): String {
        val parameterNames = signature.parameterNames

        if (parameterNames == null) {
            log.warn { "Parameter names not available. Please turn it on in gradle by adding 'javaParameters = true'." }
            return "???"
        }

        return parameterNames.mapIndexed { i, name ->
            "$name${formatParameter(joinPoint, signature, i)}"
        }.joinToString()
    }

    private fun formatParameter(joinPoint: JoinPoint, signature: MethodSignature, index: Int): String {
        val arg = joinPoint.args[index]
        val hasNoLogAnnotation = signature.method.parameterAnnotations[index].any { it is NoLog }
        if (!hasNoLogAnnotation) return "=$arg"
        if (arg is Collection<*>) return ".size=${arg.size}"
        return ""
    }

}
