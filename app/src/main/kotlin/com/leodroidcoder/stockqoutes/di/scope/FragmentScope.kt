package com.leodroidcoder.stockqoutes.di.scope

import javax.inject.Scope


/**
 * A scope qualifier,
 * intended to be used with Fragment-lifecycle-level components
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope