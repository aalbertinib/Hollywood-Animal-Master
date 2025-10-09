package org.aalbertini.ham.storage

import java.io.File

/**
 * JVM (Desktop) implementation of StorageProvider using file storage
 */
actual class StorageProvider {
    private val storageDir: File by lazy {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".hollywoodanimals")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        appDir
    }
    
    actual fun saveData(key: String, data: String) {
        try {
            val file = File(storageDir, "$key.json")
            file.writeText(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    actual fun loadData(key: String): String? {
        return try {
            val file = File(storageDir, "$key.json")
            if (file.exists()) file.readText() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
