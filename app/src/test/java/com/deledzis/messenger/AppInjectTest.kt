package com.deledzis.messenger

import org.junit.Before
import org.junit.Test
import timber.log.Timber
import javax.inject.Inject

class AppInjectTest {
    @Inject
    lateinit var application: App

    @Before
    fun setup() {
        val app = App()
        val component = DaggerA.factory().create(app)
        component.inject()
    }

    @Test
    fun test() {
        Timber.e("App: $application")
    }

}