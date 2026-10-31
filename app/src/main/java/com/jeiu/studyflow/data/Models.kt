package com.jeiu.studyflow.data

// These classes mirror the planned Room tables (courses, assignments).
// In weeks 13-14 they become @Entity classes; for now they live in memory.

data class Course(
    val courseId: Int,
    val name: String,
    val professor: String?,
    val semester: String,
    val colorHex: String
)

data class Assignment(
    val assignmentId: Int,
    val courseId: Int,
    val title: String,
    val description: String?,
    val dueDate: String,          // yyyy-MM-dd
    val priority: Int,            // 1 = Low, 2 = Medium, 3 = High
    val isCompleted: Boolean = false,
    val completedDate: String? = null
)

enum class AssignmentFilter { ALL, PENDING, DONE }
