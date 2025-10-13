package org.aalbertini.ham.core.data.storage

import android.content.Context
import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Android implementation of StorageProvider using internal file storage
 * Thread-safe implementation using read-write locks
 */
actual class StorageProvider {
    private val context: Context by lazy {
        // Get the application context through a static holder
        AndroidContextHolder.applicationContext
    }
    
    private val lock = ReentrantReadWriteLock()
    
    actual fun saveData(key: String, data: String) {
        lock.write {
            try {
                val file = File(context.filesDir, "$key.json")
                file.writeText(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    actual fun loadData(key: String): String? {
        return lock.read {
            try {
                val file = File(context.filesDir, "$key.json")
                if (file.exists()) file.readText() else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

/**
 * Holder for Android application context
 */
object AndroidContextHolder {
    lateinit var applicationContext: Context
}
