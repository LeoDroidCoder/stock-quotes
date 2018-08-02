package com.leodroidcoder.stockqoutes.domain.common

/**
 * Object mapper.
 * Provides mapping objects of type [B] into objects of type [A] and vice versa
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface ObjectMapperRound<A, B> : ObjectMapper<A, B> {

    /**
     * Map object type of [B] to object type of [A].
     *
     * @return mapped object type of [A].
     *
     * @since 1.0.0
     */
    fun mapFrom(input: B): A

    /**
     * Map collection of objects type of [B] to immutable
     * list of object type of [A].
     * Default implementation may be override for increasing performance in an exact use case.
     *
     * @return immutable list of mapped object type of [A].
     *
     * @since 1.0.0
     */
    fun mapFrom(collection: Collection<B>): List<A> {
        return collection.map { mapFrom(it) }.toList()
    }
}