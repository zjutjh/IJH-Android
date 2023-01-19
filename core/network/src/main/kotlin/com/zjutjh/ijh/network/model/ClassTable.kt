package com.zjutjh.ijh.network.model

data class ClassTable(
    val info: Info,
    val lessonsTable: List<LessonsTable>?,
    val practiceLessons: List<PracticeLesson>?,
) {
    data class Info(
        val className: String,
        val name: String
    )

    data class LessonsTable(
        val campus: String,
        val className: String,
        val credits: String,
        val id: String,
        val lessonHours: String,
        val lessonName: String,
        val lessonPlace: String,
        val placeID: String,
        val sections: String,
        val teacherName: String,
        val type: String,
        val week: String,
        val weekday: String
    )

    data class PracticeLesson(
        val className: String,
        val credits: String,
        val lessonName: String,
        val teacherName: String
    )
}
