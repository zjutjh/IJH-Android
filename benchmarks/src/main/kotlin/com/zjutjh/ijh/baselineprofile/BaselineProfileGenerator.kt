package com.zjutjh.ijh.baselineprofile

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import com.zjutjh.ijh.PACKAGE_NAME
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalBaselineProfilesApi::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineRule = BaselineProfileRule()

    @Test
    fun generate() =
        baselineRule.collectBaselineProfile(PACKAGE_NAME) {
            pressHome()
            startActivityAndWait()
            with(device) {
                wait(Until.findObject(By.scrollable(true)), 1_000)
                findObject(By.scrollable(true)).also {
                    it.setGestureMargin(device.displayWidth / 10)
                    it.scroll(Direction.DOWN, 0.6f)
                    it.scroll(Direction.UP, 0.8f)
                }
                findObject(By.desc("Navigation")).click()
                findObject(By.desc("Collapse")).click()

                findObject(By.desc("Login"))?.click()
                waitForIdle()
                pressBack()
            }
        }
}