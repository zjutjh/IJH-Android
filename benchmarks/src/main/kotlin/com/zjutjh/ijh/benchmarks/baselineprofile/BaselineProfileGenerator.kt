package com.zjutjh.ijh.benchmarks.baselineprofile

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import com.zjutjh.ijh.benchmarks.PACKAGE_NAME
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
                findObject(By.desc("Navigation")).click()
                waitForIdle()
                findObject(By.desc("Collapse")).click()

                waitForIdle()
                pressBack()
            }
        }
}