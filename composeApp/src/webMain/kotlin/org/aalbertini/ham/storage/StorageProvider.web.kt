package org.aalbertini.ham.storage

import kotlinx.browser.localStorage

/**
 * Web (JS/Wasm) implementation of StorageProvider using localStorage
 */
actual class StorageProvider {
    actual fun saveData(key: String, data: String) {
        try {
            localStorage.setItem(key, data)
        } catch (e: Exception) {
            console.error("Error saving data: ${e.message}")
        }
    }
    
    actual fun loadData(key: String): String? {
        return try {
            localStorage.getItem(key)
        } catch (e: Exception) {
            console.error("Error loading data: ${e.message}")
            null
        }
    }
}
