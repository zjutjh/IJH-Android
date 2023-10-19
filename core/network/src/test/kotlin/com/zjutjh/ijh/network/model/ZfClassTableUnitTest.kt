package com.zjutjh.ijh.network.model

import org.junit.Assert
import org.junit.Test

class ZfClassTableUnitTest {
    private fun table(week: String) =  ZfClassTable.LessonsTable(
        "紫金港",
        "计算机科学与技术（计算机科学与技术）",
        "2019211300",
        "3.0",
        "2019211300",
        "64",
        "计算机科学与技术（计算机科学与技术）",
        "紫金港教1-101",
        "紫金港教1-101",
        "1-16周",
        "李晓",
        "必修",
        week,
        "1"
    )

    @Test
    fun testParseUseCase01() {
        val week = table("1-16周").parseWeekString()
        Assert.assertTrue(week.singles.isEmpty())
        Assert.assertEquals(week.ranges.size, 1)
        val section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, null)
    }

    @Test
    fun testParseUseCase02() {
        val week = table("1周,2周,1-3周,4-8周").parseWeekString()
        Assert.assertEquals(week.singles.size, 2)
        Assert.assertEquals(week.singles[0], 1)
        Assert.assertEquals(week.singles[1], 2)
        Assert.assertEquals(week.ranges.size, 2)
        var section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 3)
        Assert.assertEquals(section.oddOrEvenWeek, null)
        section = week.ranges[1]
        Assert.assertEquals(section.start, 4)
        Assert.assertEquals(section.end, 8)
        Assert.assertEquals(section.oddOrEvenWeek, null)
    }

    @Test
    fun testParseUseCase03() {
        val week = table("1-16周(单)").parseWeekString()
        Assert.assertTrue(week.singles.isEmpty())
        Assert.assertEquals(week.ranges.size, 1)
        val section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, false)
    }

    @Test
    fun testParseUseCase04() {
        val week = table("1-16周(双)").parseWeekString()
        Assert.assertTrue(week.singles.isEmpty())
        Assert.assertEquals(week.ranges.size, 1)
        val section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, true)
    }

    @Test
    fun testParseUseCase05() {
        val week = table("1-8周(单),9-13周(双)").parseWeekString()
        Assert.assertTrue(week.singles.isEmpty())
        Assert.assertEquals(week.ranges.size, 2)
        val section1 = week.ranges[0]
        Assert.assertEquals(section1.start, 1)
        Assert.assertEquals(section1.end, 8)
        Assert.assertEquals(section1.oddOrEvenWeek, false)
        val section2 = week.ranges[1]
        Assert.assertEquals(section2.start, 9)
        Assert.assertEquals(section2.end, 13)
        Assert.assertEquals(section2.oddOrEvenWeek, true)
    }

}