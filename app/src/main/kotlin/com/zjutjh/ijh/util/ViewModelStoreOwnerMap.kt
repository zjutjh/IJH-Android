package com.zjutjh.ijh.util

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras

/**
 * A [ViewModelStore] mapping owner.
 * It can be used to create/get/delete a [ViewModelStore] for a specific key.
 *
 * This View Model Store Owner is usable to provide a view model sharing approach and
 * a manageable view model store owner.
 */
class ViewModelStoreMappingOwner(
    private val viewModelProviderFactory: ViewModelProvider.Factory,
    private val viewModelCreationExtras: CreationExtras,
) {
    private val viewModelStores = mutableMapOf<String, ViewModelStore>()

    operator fun get(key: String): ViewModelStore? {
        return viewModelStores[key]
    }

    fun new(key: String): ViewModelStore {
        val newStore = ViewModelStore()
        val oldStore = viewModelStores.put(key, newStore)
        oldStore?.clear()
        return newStore
    }

    fun remove(key: String) {
        val oldStore = viewModelStores.remove(key)
        oldStore?.clear()
    }

    fun provider(key: String): ViewModelProvider {
        val store = get(key) ?: new(key)
        return ViewModelProvider(
            store,
            viewModelProviderFactory,
            viewModelCreationExtras
        )
    }

    fun clear() {
        viewModelStores.values.forEach { it.clear() }
        viewModelStores.clear()
        Log.d("MappingOwner", "$this cleared.")
    }
}