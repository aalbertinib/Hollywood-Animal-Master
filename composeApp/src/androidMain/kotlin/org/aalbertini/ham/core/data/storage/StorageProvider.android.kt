package org.aalbertini.ham.core.data.storage

import android.content.Context
import android.util.AtomicFile
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Android implementation of StorageProvider using internal file storage
 * Thread-safe implementation using read-write locks
 */
actual class StorageProvider : KeyValueStore {
    private val context: Context by lazy {
        // Get the application context through a static holder
        AndroidContextHolder.applicationContext
    }
    
    private val lock = ReentrantReadWriteLock()
    
    actual override fun saveData(key: String, data: String) {
        lock.write {
            // AtomicFile writes a new file and swaps it in, so a crash mid-write never leaves half a JSON.
            val file = atomicFile(key)
            var stream: FileOutputStream? = null
            try {
                stream = file.startWrite()
                stream.write(data.toByteArray(Charsets.UTF_8))
                file.finishWrite(stream)
            } catch (e: Exception) {
                stream?.let(file::failWrite)
                e.printStackTrace()
            }
        }
    }

    actual override fun loadData(key: String): String? {
        return lock.read {
            try {
                // readFully also recovers the backup an interrupted write left behind.
                atomicFile(key).readFully().toString(Charsets.UTF_8)
            } catch (e: FileNotFoundException) {
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun atomicFile(key: String) = AtomicFile(File(context.filesDir, "$key.json"))
}

/**
 * Holder for Android application context
 */
object AndroidContextHolder {
    lateinit var applicationContext: Context
}
