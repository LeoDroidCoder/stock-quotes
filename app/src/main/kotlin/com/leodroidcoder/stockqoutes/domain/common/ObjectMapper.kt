package com.leodroidcoder.stockqoutes.domain.common

/**
 * Object mapper.
 * Provides mapping objects of type [A] into objects of type [B].
 * For converting objects in both directions refer to [ObjectMapperRound]
 *
 * @see ObjectMapperRound
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface ObjectMapper<in A, out B> {

    /**
     * Map object type of [A] to object type of [B].
     *
     * @return mapped object type of [B].
     *
     * @since 1.0.0
     */
    fun mapTo(input: A): B

    /**
     * Map collection of objects type of [A] to immutable
     * list of object type of [B].
     * Default implementation may be override for increasing performance in an exact use case.
     *
     * @return immutable list of mapped object type of [B].
     *
     * @since 1.0.0
     */
    fun mapTo(collection: Collection<A>): List<B> {
        return collection.map { mapTo(it) }.toList()
    }
}