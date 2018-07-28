package com.leodroidcoder.stockqoutes.domain.common

/**
 * Object mapper.
 * Provides mapping objects of type [A] into objects of type [B].
 * For converting objects in both directions refer to [ObjectMapperRound]
 *
 * @see ObjectMapperRound
 */
interface ObjectMapper<in A, out B> {

    /**
     * Map object type of [A] to object type of [B].
     *
     * @since 0.1.0
     *
     * @return mapped object type of [B].
     */
    fun mapTo(input: A): B

    /**
     * Map collection of objects type of [A] to immutable
     * list of object type of [B].
     * Default implementation may be override for increasing performance in an exact use case.
     *
     * @since 0.1.0
     *
     * @return immutable list of mapped object type of [B].
     */
    fun mapTo(collection: Collection<A>): List<B> {
        return collection.map { mapTo(it) }.toList()
    }
}