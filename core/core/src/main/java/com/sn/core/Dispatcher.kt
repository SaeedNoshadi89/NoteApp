package com.sn.core

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val dispatcher: Dispatchers)

enum class Dispatchers {
    IO,
    Main,
    Default
}
