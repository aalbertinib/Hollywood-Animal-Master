package org.aalbertini.ham.core.data.storage

import java.io.File
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * JVM (Desktop) implementation of StorageProvider using file storage
 * Thread-safe implementation using read-write locks
 */
actual class StorageProvider : KeyValueStore {
    private val storageDir: File by lazy {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".hollywood_animal_master")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        appDir
    }
    
    private val lock = ReentrantReadWriteLock()
    
    actual override fun saveData(key: String, data: String) {
        lock.write {
            try {
                // Write a sibling file, then swap it in: a crash mid-write never leaves half a JSON.
                val file = File(storageDir, "$key.json")
                val temp = File(storageDir, "$key.json.tmp")
                temp.writeText(data)
                try {
                    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
                } catch (e: AtomicMoveNotSupportedException) {
                    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    actual override fun loadData(key: String): String? {
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
