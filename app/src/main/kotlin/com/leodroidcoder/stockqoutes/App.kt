package com.leodroidcoder.stockqoutes

import com.leodroidcoder.stockqoutes.di.DaggerAppComponent
import com.splunk.mint.Mint
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

/**
 * Application class.
 * Initialize here application-lifecycle-level dependencies,
 * such as Dagger application component etc.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class App : DaggerApplication() {

    /**
     * Initialize Dagger application injector.
     *
     * @since 1.0.0
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
                .create(this)
    }

    override fun onCreate() {
        super.onCreate()
        // initialize Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initializeLeakDetection()
        initializeCrashlytics()
    }

    /**
     * initialize LeakCanary
     *
     * @since 1.0.0
     */
    private fun initializeLeakDetection() {
        if (!BuildConfig.DEBUG || !LeakCanary.isInAnalyzerProcess(this))
            return
        LeakCanary.install(this)
    }

    /**
     * Initialize "lightweight" crashlytics.
     *
     * @since 1.0.0
     */
    private fun initializeCrashlytics() {
        Mint.initAndStartSession(this, "bb3d7389")
    }
}
