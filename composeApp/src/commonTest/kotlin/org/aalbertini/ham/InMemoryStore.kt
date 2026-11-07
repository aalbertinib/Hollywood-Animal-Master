package org.aalbertini.ham

import org.aalbertini.ham.core.data.storage.KeyValueStore

/** [KeyValueStore] backed by a map, so tests never touch the real files or localStorage. */
class InMemoryStore(initial: Map<String, String> = emptyMap()) : KeyValueStore {
    val data = initial.toMutableMap()

    override fun saveData(key: String, data: String) {
        this.data[key] = data
    }

    override fun loadData(key: String): String? = data[key]
}
