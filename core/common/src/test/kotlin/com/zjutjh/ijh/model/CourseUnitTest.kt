package com.zjutjh.ijh.model

import org.junit.Assert
import org.junit.Test

class CourseUnitTest {

    @Test
    fun testParseUseCase01() {
        val week = CourseWeek.parseFromWeekString("1-16周")
        Assert.assertTrue(week.weeks.isEmpty())
        Assert.assertEquals(week.weekSections.size, 1)
        val section = week.weekSections[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, null)
    }

    @Test
    fun testParseUseCase02() {
        val week = CourseWeek.parseFromWeekString("1周,2周,1-3周,4-8周")
        Assert.assertEquals(week.weeks.size, 2)
        Assert.assertEquals(week.weeks[0], 1)
        Assert.assertEquals(week.weeks[1], 2)
        Assert.assertEquals(week.weekSections.size, 2)
        var section = week.weekSections[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 3)
        Assert.assertEquals(section.oddOrEvenWeek, null)
        section = week.weekSections[1]
        Assert.assertEquals(section.start, 4)
        Assert.assertEquals(section.end, 8)
        Assert.assertEquals(section.oddOrEvenWeek, null)
    }

    @Test
    fun testParseUseCase03() {
        val week = CourseWeek.parseFromWeekString("1-16单周")
        Assert.assertTrue(week.weeks.isEmpty())
        Assert.assertEquals(week.weekSections.size, 1)
        val section = week.weekSections[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, false)
    }

    @Test
    fun testParseUseCase04() {
        val week = CourseWeek.parseFromWeekString("1-16双周")
        Assert.assertTrue(week.weeks.isEmpty())
        Assert.assertEquals(week.weekSections.size, 1)
        val section = week.weekSections[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, true)
    }

    @Test
    fun testParseUseCase05() {
        val week = CourseWeek.parseFromWeekString("1-8单周,9-13双周")
        Assert.assertTrue(week.weeks.isEmpty())
        Assert.assertEquals(week.weekSections.size, 2)
        val section1 = week.weekSections[0]
        Assert.assertEquals(section1.start, 1)
        Assert.assertEquals(section1.end, 8)
        Assert.assertEquals(section1.oddOrEvenWeek, false)
        val section2 = week.weekSections[1]
        Assert.assertEquals(section2.start, 9)
        Assert.assertEquals(section2.end, 13)
        Assert.assertEquals(section2.oddOrEvenWeek, true)
    }

    @Test
    fun testParseUseCase06() {
        val week = CourseWeek.parseFromWeekString("1-8单,9-13双周")
        Assert.assertTrue(week.weeks.isEmpty())
        Assert.assertEquals(week.weekSections.size, 2)
        val section1 = week.weekSections[0]
        Assert.assertEquals(section1.start, 1)
        Assert.assertEquals(section1.end, 8)
        Assert.assertEquals(section1.oddOrEvenWeek, false)
        val section2 = week.weekSections[1]
        Assert.assertEquals(section2.start, 9)
        Assert.assertEquals(section2.end, 13)
        Assert.assertEquals(section2.oddOrEvenWeek, true)
    }

    @Test
    fun testParseUseCase07() {
        val week = CourseWeek.parseFromWeekString("1,2周,1-3,4-8周,9-13单,14-16双周")
        Assert.assertEquals(week.weeks.size, 2)
        Assert.assertEquals(week.weeks[0], 1)
        Assert.assertEquals(week.weeks[1], 2)
        Assert.assertEquals(week.weekSections.size, 4)
        var section = week.weekSections[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 3)
        Assert.assertEquals(section.oddOrEvenWeek, null)
        section = week.weekSections[1]
        Assert.assertEquals(section.start, 4)
        Assert.assertEquals(section.end, 8)
        Assert.assertEquals(section.oddOrEvenWeek, null)
        section = week.weekSections[2]
        Assert.assertEquals(section.start, 9)
        Assert.assertEquals(section.end, 13)
        Assert.assertEquals(section.oddOrEvenWeek, false)
        section = week.weekSections[3]
        Assert.assertEquals(section.start, 14)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, true)
    }
}