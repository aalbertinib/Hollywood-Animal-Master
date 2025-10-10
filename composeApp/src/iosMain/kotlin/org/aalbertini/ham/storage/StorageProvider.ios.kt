@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package org.aalbertini.ham.storage

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

/**
 * iOS implementation of StorageProvider using file storage
 */
actual class StorageProvider {
    private val fileManager = NSFileManager.defaultManager
    
    private fun getDocumentsDirectory(): String {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )
        return paths.first() as String
    }
    
    private fun getFilePath(key: String): String {
        return "${getDocumentsDirectory()}/$key.json"
    }
    
    actual fun saveData(key: String, data: String) {
        try {
            val path = getFilePath(key)
            val nsString = NSString.create(string = data)
            nsString.writeToFile(
                path = path,
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = null
            )
        } catch (e: Exception) {
            println("Error saving data: ${e.message}")
        }
    }
    
    actual fun loadData(key: String): String? {
        return try {
            val path = getFilePath(key)
            if (fileManager.fileExistsAtPath(path)) {
                NSString.stringWithContentsOfFile(
                    path = path,
                    encoding = NSUTF8StringEncoding,
                    error = null
                )
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error loading data: ${e.message}")
            null
        }
    }
}
