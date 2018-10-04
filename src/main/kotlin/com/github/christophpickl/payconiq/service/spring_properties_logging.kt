package com.github.christophpickl.payconiq.service

import mu.KotlinLogging.logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Environment
import org.springframework.core.env.MapPropertySource
import org.springframework.stereotype.Component

@Component
class SpringPropertiesLoggerOnApplicationReady(
    springEnvironment: Environment
) {

    private val logger = SpringPropertiesLogger(springEnvironment)

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReadyEvent() {
        logger.logProperties(
            inclusions = emptyList(),
            exclusions = listOf(".*(password).*".toRegex())
        )
    }

}

class SpringPropertiesLogger(
    private val springEnvironment: Environment
) {

    private val log = logger {}
    private val springPropertySourcePrefixForYamlFiles = "applicationConfig:"

    fun logProperties(inclusions: List<Regex>, exclusions: List<Regex>) {
        log.info { "logProperties(inclusions.size=$inclusions, exclusions.size=$exclusions)" }

        val properties = fetchProperties(inclusions, exclusions)

        log.info { "=============================================================" }
        properties.forEach { (key, value) -> log.info { "  $key => $value" } }
        log.info { "=============================================================" }
    }

    private fun fetchProperties(inclusions: List<Regex>, exclusions: List<Regex>): Map<String, Any> =
        springEnvironment.listAllMapPropertySources()
            .filter { it.name.startsWith(springPropertySourcePrefixForYamlFiles) }
            .flatMap { it.propertyNames.toSet() }
            .toSet()
            .filter { propertyName -> exclusions.all { !it.matches(propertyName) } }
            .filter { propertyName -> if (inclusions.isEmpty()) true else inclusions.any { regex -> propertyName.matches(regex) } }
            .sorted()
            .map { propertyName -> propertyName to (springEnvironment.getProperty(propertyName) ?: "") }
            .toList()
            .toMap()

    /**
     * Unfortunately spring does not support accessing all keys/properties.
     *
     * https://stackoverflow.com/questions/23506471/spring-access-all-environment-properties-as-a-map-or-properties-object
     */
    private fun Environment.listAllMapPropertySources(): List<MapPropertySource> {
        val abstractEnvironment = this as? ConfigurableEnvironment ?: return emptyList()
        return abstractEnvironment.propertySources.filterIsInstance<MapPropertySource>()
    }

}
