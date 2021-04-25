package com.deledzis.messenger.ui

/*@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginFailRegisterTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true)

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val component = DaggerTestAppComponent.builder()
            .testCacheModule(TestCacheModule())
            .testNetworkModule(TestNetworkModule())
            .testRepositoriesModule(TestRepositoriesModule())
            .utilsModule(UtilsModule())
            .testAppModule(TestAppModule(context))
            .build()
        component.into(this)
    }

    @Test
    fun loginFailThenRegister() {
        // GIVEN
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        UiThreadStatement.runOnUiThread {
            navController.setGraph(R.navigation.nav_graph)
        }

        // VERIFY
        onView(withId(R.id.login_button))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_username))
            .perform(typeText("test15"), closeSoftKeyboard())
        Thread.sleep(50)
        onView(withId(R.id.tie_password))
            .perform(typeText("testtest"), closeSoftKeyboard())
        onView(withId(R.id.login_button))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.to_registration_button))
            .perform(click())

        // VERIFY
        Thread.sleep(300)
        onView(withId(R.id.fragment_register_root))
            .check(matches(isDisplayed()))
        Timber.e("Current destination: ${navController.currentDestination?.displayName}")

        // GIVEN
        onView(withId(R.id.tie_username))
            .perform(typeText("test15"), closeSoftKeyboard())
        Thread.sleep(50)
        onView(withId(R.id.tie_password))
            .perform(typeText("testtest"), closeSoftKeyboard())
        onView(withId(R.id.register_button))
            .perform(click())
        Thread.sleep(2000)

        // VERIFY
        onView(withId(R.id.fragment_chats_root))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.ic_settings))
            .perform(click())

        // VERIFY
        Thread.sleep(300)
        onView(withId(R.id.fragment_settings_root))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.btn_delete_account))
            .perform(click())
        Thread.sleep(100)
        onView(ViewMatchers.withText("Удалить"))
            .perform(click())
        Thread.sleep(500)

        //VERIFY
        onView(withId(R.id.fragment_login_root))
            .check(matches(isDisplayed()))

    }

}*/
