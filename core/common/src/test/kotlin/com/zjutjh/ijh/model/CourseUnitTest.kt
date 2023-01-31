package com.zjutjh.ijh.model

import org.junit.Assert
import org.junit.Test

class CourseUnitTest {

    @Test
    fun testParseUseCase01() {
        val week = CourseWeek.parseFromZfWeekString("1-16周")
        Assert.assertTrue(week.singles.isEmpty())
        Assert.assertEquals(week.ranges.size, 1)
        val section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, null)
    }

    @Test
    fun testParseUseCase02() {
        val week = CourseWeek.parseFromZfWeekString("1周,2周,1-3周,4-8周")
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
        val week = CourseWeek.parseFromZfWeekString("1-16单周")
        Assert.assertTrue(week.singles.isEmpty())
        Assert.assertEquals(week.ranges.size, 1)
        val section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, false)
    }

    @Test
    fun testParseUseCase04() {
        val week = CourseWeek.parseFromZfWeekString("1-16双周")
        Assert.assertTrue(week.singles.isEmpty())
        Assert.assertEquals(week.ranges.size, 1)
        val section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, true)
    }

    @Test
    fun testParseUseCase05() {
        val week = CourseWeek.parseFromZfWeekString("1-8单周,9-13双周")
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

    @Test
    fun testParseUseCase06() {
        val week = CourseWeek.parseFromZfWeekString("1-8单,9-13双周")
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

    @Test
    fun testParseUseCase07() {
        val week = CourseWeek.parseFromZfWeekString("1,2周,1-3,4-8周,9-13单,14-16双周")
        Assert.assertEquals(week.singles.size, 2)
        Assert.assertEquals(week.singles[0], 1)
        Assert.assertEquals(week.singles[1], 2)
        Assert.assertEquals(week.ranges.size, 4)
        var section = week.ranges[0]
        Assert.assertEquals(section.start, 1)
        Assert.assertEquals(section.end, 3)
        Assert.assertEquals(section.oddOrEvenWeek, null)
        section = week.ranges[1]
        Assert.assertEquals(section.start, 4)
        Assert.assertEquals(section.end, 8)
        Assert.assertEquals(section.oddOrEvenWeek, null)
        section = week.ranges[2]
        Assert.assertEquals(section.start, 9)
        Assert.assertEquals(section.end, 13)
        Assert.assertEquals(section.oddOrEvenWeek, false)
        section = week.ranges[3]
        Assert.assertEquals(section.start, 14)
        Assert.assertEquals(section.end, 16)
        Assert.assertEquals(section.oddOrEvenWeek, true)
    }

    @Test
    fun testSerialize() {
        val case = CourseWeek(
            listOf(7, 8, 9, 10),
            listOf(
                CourseWeek.WeekRange(1, 16, null),
                CourseWeek.WeekRange(1, 16, true),
                CourseWeek.WeekRange(1, 16, false)
            ),
        )
        val text = case.serialize()
        Assert.assertEquals(text, "S7S8S9S10R1-16E1-16O1-16")
    }

    @Test
    fun testDeserialize() {
        val case = CourseWeek.Serializer.deserialize("S7S8S9S10R1-16E1-16O1-16")
        Assert.assertEquals(case.singles.size, 4)
        Assert.assertEquals(case.ranges.size, 3)
        var range = case.ranges[0]
        Assert.assertEquals(range.start, 1)
        Assert.assertEquals(range.end, 16)
        Assert.assertEquals(range.oddOrEvenWeek, null)
        range = case.ranges[1]
        Assert.assertEquals(range.start, 1)
        Assert.assertEquals(range.end, 16)
        Assert.assertEquals(range.oddOrEvenWeek, true)
        range = case.ranges[2]
        Assert.assertEquals(range.start, 1)
        Assert.assertEquals(range.end, 16)
        Assert.assertEquals(range.oddOrEvenWeek, false)
    }
}