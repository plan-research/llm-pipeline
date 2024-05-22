package org.jetbrains.plan.runner.storage

/**
 * A polymorphic map for storing results of the experiment execution
 */
interface TypeSafeStorage {
    public operator fun<E> get(key: Key<E>): E?
    public interface Key<E>
}

interface MutableTypeSafeStorage : TypeSafeStorage {
    public operator fun<E> set(key: TypeSafeStorage.Key<E>, value: E)
}

/**
 * Returns an empty mutable type-safe storage object.
 */
public fun mutableTypeSafeStorage(): MutableTypeSafeStorage = TypeSafeStorageImpl()