package org.aalbertini.ham.storage

import android.content.Context
import java.io.File

/**
 * Android implementation of StorageProvider using internal file storage
 */
actual class StorageProvider {
    private val context: Context by lazy {
        // Get the application context through a static holder
        AndroidContextHolder.applicationContext
    }
    
    actual fun saveData(key: String, data: String) {
        try {
            val file = File(context.filesDir, "$key.json")
            file.writeText(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    actual fun loadData(key: String): String? {
        return try {
            val file = File(context.filesDir, "$key.json")
            if (file.exists()) file.readText() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

/**
 * Holder for Android application context
 */
object AndroidContextHolder {
    lateinit var applicationContext: Context
}
