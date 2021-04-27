package com.deledzis.messenger.ui

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginSuccessLogoutTest::class,
    LoginFailThenSuccessTest::class,
    RegisterFailLoginLogoutTest::class,
    RegisterSuccessDeleteAccountTest::class
)
class EndToEndTestsSuite