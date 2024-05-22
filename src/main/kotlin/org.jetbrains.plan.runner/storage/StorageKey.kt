package org.jetbrains.plan.runner.storage

/**
 * StorageKey is a data class that represents a key used for storing and retrieving values in a [TypeSafeStorage].
 *
 * @param name The name of the key. **NB! The name of the key should be unique**
 * @param E The type of the value associated with the key.
 */
data class StorageKey<E>(val name: String) : TypeSafeStorage.Key<E>