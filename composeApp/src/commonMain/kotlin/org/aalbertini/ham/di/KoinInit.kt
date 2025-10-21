package org.aalbertini.ham.di

import org.koin.core.context.startKoin

object DI {
    @Volatile
    private var started: Boolean = false

    fun init() {
        if (!started) {
            synchronized(this) {
                if (!started) {
                    startKoin {
                        modules(appModules)
                    }
                    started = true
                }
            }
        }
    }
}
