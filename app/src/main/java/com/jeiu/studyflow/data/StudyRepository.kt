package com.jeiu.studyflow.data

import java.time.LocalDate

/**
 * In-memory data source used until Room is added (weeks 13-14).
 * Function names follow the planned DAO methods so the swap is easy.
 */
object StudyRepository {

    private val courses = mutableListOf<Course>()
    private val assignments = mutableListOf<Assignment>()
    private var nextCourseId = 1
    private var nextAssignmentId = 1

    private val palette = listOf("#2166D6", "#D64545", "#2EA05A", "#F09628", "#8E44AD", "#16A2B8")

    init {
        val mobile = addCourse("Mobile Programming", "Prof. Kim")
        val algo = addCourse("Algorithms", "Prof. Lee")
        val db = addCourse("Database Systems", "Prof. Park")
        val eng = addCourse("Academic English", "Prof. Choi")
        val today = LocalDate.now()
        addAssignment(Assignment(0, mobile.courseId, "Android Midterm Design",
            "Write the planning & design PDF and submit it with the GitHub URL.",
            today.plusDays(2).toString(), 3))
        addAssignment(Assignment(0, algo.courseId, "Algorithm Problem Set 5",
            "Solve problems 1-6 (graphs).", today.plusDays(5).toString(), 2))
        addAssignment(Assignment(0, eng.courseId, "English Essay Draft",
            "500-word draft.", today.plusDays(8).toString(), 1))
        addAssignment(Assignment(0, db.courseId, "Database ER Diagram",
            "Draw the ER diagram for the library system.", today.plusDays(11).toString(), 2))
    }

    // ---------- Courses ----------
    fun getCourses(): List<Course> = courses.toList()

    fun getCourse(id: Int): Course? = courses.firstOrNull { it.courseId == id }

    fun addCourse(name: String, professor: String?): Course {
        val course = Course(
            courseId = nextCourseId++,
            name = name,
            professor = professor?.ifBlank { null },
            semester = "2026-Fall",
            colorHex = palette[(nextCourseId - 2) % palette.size]
        )
        courses.add(course)
        return course
    }

    fun updateCourse(course: Course) {
        val i = courses.indexOfFirst { it.courseId == course.courseId }
        if (i >= 0) courses[i] = course
    }

    fun pendingCount(courseId: Int): Int =
        assignments.count { it.courseId == courseId && !it.isCompleted }

    // ---------- Assignments ----------
    fun getAssignments(filter: AssignmentFilter): List<Assignment> {
        val list = when (filter) {
            AssignmentFilter.ALL -> assignments
            AssignmentFilter.PENDING -> assignments.filter { !it.isCompleted }
            AssignmentFilter.DONE -> assignments.filter { it.isCompleted }
        }
        return list.sortedBy { it.dueDate }
    }

    fun getAssignment(id: Int): Assignment? = assignments.firstOrNull { it.assignmentId == id }

    fun addAssignment(a: Assignment) {
        assignments.add(a.copy(assignmentId = nextAssignmentId++))
    }

    fun updateAssignment(a: Assignment) {
        val i = assignments.indexOfFirst { it.assignmentId == a.assignmentId }
        if (i >= 0) assignments[i] = a
    }

    fun toggleCompleted(id: Int) {
        val a = getAssignment(id) ?: return
        updateAssignment(
            if (a.isCompleted) a.copy(isCompleted = false, completedDate = null)
            else a.copy(isCompleted = true, completedDate = DateUtils.today())
        )
    }

    fun deleteAssignment(id: Int) {
        assignments.removeAll { it.assignmentId == id }
    }
}
