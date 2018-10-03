package com.github.christophpickl.payconiq

import mu.KotlinLogging

fun log(func: () -> Unit) = KotlinLogging.logger(func)
