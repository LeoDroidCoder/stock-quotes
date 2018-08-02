package com.leodroidcoder.stockqoutes.domain.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Shedulers provider.
 * Use it's implementation when dealing with threads in RxJava.
 * Lets easily replace them for other purposes, such as testing.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
object AppSchedulers {

    interface SchedulerProvider {

        /**
         * Main thread.
         * Use it only for UI-related tasks.
         *
         * @since 1.0.0
         */
        fun mainThread(): Scheduler

        /**
         * IO thread.
         * Use it for I/O operations, such as accessing network.
         *
         * @since 1.0.0
         */
        fun io(): Scheduler

        /**
         * Computation thread.
         * Use it for performing "local" extensive background operations,
         * such as accessing database, in order to keep off UI and IO thread.
         *
         * @since 1.0.0
         */
        fun computation(): Scheduler
    }

    private var instance: SchedulerProvider = DefaultSchedulerProvider()

    fun setInstance(instance: SchedulerProvider) {
        AppSchedulers.instance = instance
    }

    fun mainThread(): Scheduler {
        return instance.mainThread()
    }

    fun io(): Scheduler {
        return instance.io()
    }

    fun computation(): Scheduler {
        return instance.computation()
    }



    class DefaultSchedulerProvider : SchedulerProvider {

        override fun mainThread(): Scheduler {
            return AndroidSchedulers.mainThread()
        }

        /**
         * Default provider implementation
         *
         * @since 1.0.0
         */
        override fun io(): Scheduler {
            return Schedulers.io()
        }

        override fun computation(): Scheduler {
            return Schedulers.computation()
        }
    }
}
