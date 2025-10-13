package org.aalbertini.ham.core.data.storage

/**
 * Platform-specific storage provider
 */
expect class StorageProvider() {
    /**
     * Saves data to persistent storage
     */
    fun saveData(key: String, data: String)
    
    /**
     * Loads data from persistent storage
     */
    fun loadData(key: String): String?
}
