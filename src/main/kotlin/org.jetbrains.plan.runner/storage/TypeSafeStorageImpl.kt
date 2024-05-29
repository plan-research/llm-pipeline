package org.jetbrains.plan.runner.storage

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger { }

internal class TypeSafeStorageImpl : MutableTypeSafeStorage {
    override fun <E> get(key: TypeSafeStorage.Key<E>): E? {
        val modifiedKey = UnderlyingKey(key)
        @Suppress("UNCHECKED_CAST")
        return underlyingMap[modifiedKey] as? E
    }

    override fun <E> set(key: TypeSafeStorage.Key<E>, value: E) {
        val modifiedKey = UnderlyingKey(key)
        logger.debug { "setting $key=$value" }
        underlyingMap[modifiedKey] = value
    }

    private val underlyingMap: MutableMap<UnderlyingKey, Any?> = mutableMapOf()

    // For the sake of type aligned (I do not want to store Map<Any, Any?>)
    private class UnderlyingKey(val key: TypeSafeStorage.Key<*>) {
        override fun hashCode(): Int = key.hashCode()
        override fun equals(other: Any?): Boolean {
            return if (other is UnderlyingKey) {
                key == other.key
            } else key == other
        }
    }
}