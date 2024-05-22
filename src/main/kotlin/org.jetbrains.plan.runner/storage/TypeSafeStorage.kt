package org.jetbrains.plan.runner.storage


interface TypeSafeStorage {
    public operator fun<E> get(key: Key<E>): E?
    public interface Key<E>
}

interface MutableTypeSafeStorage : TypeSafeStorage {
    public operator fun<E> set(key: TypeSafeStorage.Key<E>, value: E)
}

public fun mutableTypeSafeStorage(): MutableTypeSafeStorage = TypeSafeStorageImpl()