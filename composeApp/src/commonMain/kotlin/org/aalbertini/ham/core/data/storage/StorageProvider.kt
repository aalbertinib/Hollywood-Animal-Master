package org.aalbertini.ham.core.data.storage

/**
 * String key-value persistence. Repositories depend on this interface so tests can use
 * an in-memory store instead of the platform one.
 */
interface KeyValueStore {
    /** Saves data to persistent storage. */
    fun saveData(key: String, data: String)

    /** Loads data from persistent storage, or `null` when nothing is stored under [key]. */
    fun loadData(key: String): String?
}

/**
 * Platform-specific storage provider
 */
expect class StorageProvider() : KeyValueStore {
    override fun saveData(key: String, data: String)

    override fun loadData(key: String): String?
}
