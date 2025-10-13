package org.aalbertini.ham.core.data.storage

import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * JVM (Desktop) implementation of StorageProvider using file storage
 * Thread-safe implementation using read-write locks
 */
actual class StorageProvider {
    private val storageDir: File by lazy {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".hollywood_animal_master")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        appDir
    }
    
    private val lock = ReentrantReadWriteLock()
    
    actual fun saveData(key: String, data: String) {
        lock.write {
            try {
                val file = File(storageDir, "$key.json")
                file.writeText(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    actual fun loadData(key: String): String? {
        return lock.read {
            try {
                val file = File(storageDir, "$key.json")
                if (file.exists()) file.readText() else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
