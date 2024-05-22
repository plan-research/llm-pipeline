package org.jetbrains.plan.runner.storage

data class StorageKey<E>(val name: String) : TypeSafeStorage.Key<E>