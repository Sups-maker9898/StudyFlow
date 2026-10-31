package com.jeiu.studyflow.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeiu.studyflow.R
import com.jeiu.studyflow.data.Assignment
import com.jeiu.studyflow.data.StudyRepository
import com.jeiu.studyflow.databinding.FragmentAddEditAssignmentBinding
import java.time.LocalDate

class AddEditAssignmentFragment : Fragment() {

    private var _binding: FragmentAddEditAssignmentBinding? = null
    private val binding: FragmentAddEditAssignmentBinding
        get() = _binding!!

    private var editingId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditAssignmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editingId = arguments?.getInt("assignmentId", -1) ?: -1

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        // Course spinner
        val courses = StudyRepository.getCourses()
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            courses.map { it.name }
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCourse.adapter = spinnerAdapter

        // Due-date picker
        binding.editDueDate.setOnClickListener { showDatePicker() }

        // Fill the form when editing
        val existing = if (editingId >= 0) StudyRepository.getAssignment(editingId) else null
        if (existing != null) {
            binding.toolbar.title = "Edit Assignment"
            binding.editTitle.setText(existing.title)
            binding.editDueDate.setText(existing.dueDate)
            binding.editDescription.setText(existing.description)
            binding.spinnerCourse.setSelection(
                courses.indexOfFirst { it.courseId == existing.courseId }.coerceAtLeast(0)
            )
            binding.radioGroupPriority.check(
                when (existing.priority) {
                    3 -> R.id.radioHigh
                    1 -> R.id.radioLow
                    else -> R.id.radioMed
                }
            )
        } else {
            binding.radioGroupPriority.check(R.id.radioMed)
        }

        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
        binding.btnSave.setOnClickListener { save(courses.map { it.courseId }, existing) }
    }

    private fun showDatePicker() {
        val current = runCatching { LocalDate.parse(binding.editDueDate.text.toString()) }
            .getOrDefault(LocalDate.now())
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                binding.editDueDate.setText(LocalDate.of(year, month + 1, day).toString())
                binding.layoutDueDate.error = null
            },
            current.year, current.monthValue - 1, current.dayOfMonth
        ).show()
    }

    private fun save(courseIds: List<Int>, existing: Assignment?) {
        val title = binding.editTitle.text.toString().trim()
        val due = binding.editDueDate.text.toString().trim()

        // input validation
        var ok = true
        if (title.isEmpty()) {
            binding.layoutTitle.error = "Title is required"
            ok = false
        } else binding.layoutTitle.error = null
        if (due.isEmpty()) {
            binding.layoutDueDate.error = "Pick a due date"
            ok = false
        } else binding.layoutDueDate.error = null
        if (courseIds.isEmpty()) {
            Toast.makeText(requireContext(), "Add a course first", Toast.LENGTH_SHORT).show()
            ok = false
        }
        if (!ok) return

        val priority = when (binding.radioGroupPriority.checkedRadioButtonId) {
            R.id.radioHigh -> 3
            R.id.radioLow -> 1
            else -> 2
        }
        val courseId = courseIds[binding.spinnerCourse.selectedItemPosition]
        val description = binding.editDescription.text.toString().trim().ifEmpty { null }

        if (existing != null) {
            StudyRepository.updateAssignment(
                existing.copy(
                    courseId = courseId, title = title, description = description,
                    dueDate = due, priority = priority
                )
            )
        } else {
            StudyRepository.addAssignment(
                Assignment(0, courseId, title, description, due, priority)
            )
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
