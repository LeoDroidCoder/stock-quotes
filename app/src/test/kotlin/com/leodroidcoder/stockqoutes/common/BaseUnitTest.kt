package com.leodroidcoder.stockqoutes.common

import android.support.annotation.CallSuper
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

open class BaseUnitTest {

    @Rule @JvmField val schedulers = SynchronousSchedulers()

    @Before
    @CallSuper
    open fun init() {
        MockitoAnnotations.initMocks(this)
    }
}
