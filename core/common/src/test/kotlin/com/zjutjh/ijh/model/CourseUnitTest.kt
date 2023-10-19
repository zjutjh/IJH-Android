package com.zjutjh.ijh.model

import org.junit.Assert
import org.junit.Test

class CourseUnitTest {

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