package org.aalbertini.ham.core.data.storage

import kotlinx.browser.localStorage

/**
 * Web (JS/Wasm) implementation of StorageProvider using localStorage
 * Note: localStorage is single-threaded in JavaScript, so no additional locking needed
 */
actual class StorageProvider {
    actual fun saveData(key: String, data: String) {
        try {
            localStorage.setItem(key, data)
        } catch (e: Exception) {
            println("Error saving data: ${e.message}")
        }
    }
    
    actual fun loadData(key: String): String? {
        return try {
            localStorage.getItem(key)
        } catch (e: Exception) {
            println("Error loading data: ${e.message}")
            null
        }
    }
}
