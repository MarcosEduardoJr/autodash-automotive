package com.autodash.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Dispatchers injetáveis → o teste troca por TestDispatcher (roda na JVM, sem device). */
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DefaultDispatchers : DispatcherProvider {
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
}
