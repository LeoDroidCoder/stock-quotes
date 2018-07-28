package com.leodroidcoder.stockqoutes

import com.leodroidcoder.stockqoutes.di.DaggerAppComponent
import com.splunk.mint.Mint
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber


/**
 * Created by leonid on 9/26/17.
 */
class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initializeLeakDetection()
        initializeCrashlytics()
    }

    /**
     * initialize LeakCanary
     */
    private fun initializeLeakDetection() {
        if (!BuildConfig.DEBUG || !LeakCanary.isInAnalyzerProcess(this))
            return
        LeakCanary.install(this)
    }

    private fun initializeCrashlytics() {
        Mint.initAndStartSession(this, "bb3d7389")
    }

}
