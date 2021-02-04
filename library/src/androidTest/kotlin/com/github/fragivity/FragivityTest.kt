package com.github.fragivity

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.fragment.NavHostFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FragivityTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.github.fragivity.test", appContext.packageName)

        val scenario = launchFragmentInContainer<NavHostFragment>()

        scenario.onFragment {
            it.loadRoot(HomeFragment::class)
            assertEquals(it.navController.currentDestination?.id, DestFragment::class.hashCode())
        }
    }
}
