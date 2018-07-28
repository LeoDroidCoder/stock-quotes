package com.leodroidcoder.stockqoutes.domain.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by leonid on 9/26/17.
 */
object AppSchedulers {

    interface SchedulerProvider {
        fun mainThread(): Scheduler

        fun io(): Scheduler

        fun computation(): Scheduler
    }

    private var sInstance: SchedulerProvider = DefaultSchedulerProvider()

    fun setInstance(instance: SchedulerProvider) {
        AppSchedulers.sInstance = instance
    }

    fun mainThread(): Scheduler {
        return sInstance.mainThread()
    }

    fun io(): Scheduler {
        return sInstance.io()
    }

    fun computation(): Scheduler {
        return sInstance.computation()
    }


    class DefaultSchedulerProvider : SchedulerProvider {

        override fun mainThread(): Scheduler {
            return AndroidSchedulers.mainThread()
        }

        override fun io(): Scheduler {
            return Schedulers.io()
        }

        override fun computation(): Scheduler {
            return Schedulers.computation()
        }
    }
}
