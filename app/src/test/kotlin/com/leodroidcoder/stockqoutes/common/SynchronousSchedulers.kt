package com.leodroidcoder.stockqoutes.common

import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.rules.ExternalResource

class SynchronousSchedulers : ExternalResource() {

    @Throws(Throwable::class)
    override fun before() {

        AppSchedulers.setInstance(object : AppSchedulers.SchedulerProvider {
            override fun computation(): Scheduler {
                return Schedulers.trampoline()
            }

            override fun mainThread(): Scheduler {
                return Schedulers.trampoline()
            }

            override fun io(): Scheduler {
                return Schedulers.trampoline()
            }
        })
    }

    override fun after() {
        AppSchedulers.setInstance(AppSchedulers.DefaultSchedulerProvider())
    }
}
