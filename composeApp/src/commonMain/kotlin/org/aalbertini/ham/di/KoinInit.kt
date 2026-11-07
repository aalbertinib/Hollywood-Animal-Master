package org.aalbertini.ham.di

import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

object DI {
    /**
     * Starts Koin once. Every entry point (Android, desktop, iOS, web) calls this,
     * so it relies on Koin's own global context instead of JVM-only locking
     * (`@Volatile` / `synchronized` don't exist on Kotlin/JS and Wasm).
     */
    fun init() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(appModules)
            }
        }
    }
}
