package com.jeiu.studyflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeiu.studyflow.data.Course
import com.jeiu.studyflow.data.StudyRepository
import com.jeiu.studyflow.databinding.DialogCourseBinding
import com.jeiu.studyflow.databinding.FragmentCourseListBinding

class CourseListFragment : Fragment() {

    private var _binding: FragmentCourseListBinding? = null
    private val binding: FragmentCourseListBinding
        get() = _binding!!

    private lateinit var adapter: CourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CourseAdapter(onItemClick = { course -> showCourseDialog(course) })
        binding.recyclerCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCourses.adapter = adapter

        binding.fabAddCourse.setOnClickListener { showCourseDialog(null) }

        refresh()
    }

    private fun refresh() {
        val list = StudyRepository.getCourses()
        adapter.submitList(list)
        binding.textEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    /** Add (course == null) or edit a course in a simple dialog. */
    private fun showCourseDialog(course: Course?) {
        val d = DialogCourseBinding.inflate(layoutInflater)
        if (course != null) {
            d.editCourseName.setText(course.name)
            d.editProfessor.setText(course.professor)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (course == null) "Add course" else "Edit course")
            .setView(d.root)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Save") { _, _ ->
                val name = d.editCourseName.text.toString().trim()
                val prof = d.editProfessor.text.toString().trim()
                if (name.isNotEmpty()) {
                    if (course == null) StudyRepository.addCourse(name, prof)
                    else StudyRepository.updateCourse(
                        course.copy(name = name, professor = prof.ifEmpty { null })
                    )
                    refresh()
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
