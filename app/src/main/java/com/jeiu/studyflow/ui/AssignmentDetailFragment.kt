package com.jeiu.studyflow.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeiu.studyflow.R
import com.jeiu.studyflow.data.DateUtils
import com.jeiu.studyflow.data.StudyRepository
import com.jeiu.studyflow.databinding.FragmentAssignmentDetailBinding

class AssignmentDetailFragment : Fragment() {

    private var _binding: FragmentAssignmentDetailBinding? = null
    private val binding: FragmentAssignmentDetailBinding
        get() = _binding!!

    private var assignmentId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignmentId = arguments?.getInt("assignmentId", -1) ?: -1

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        val a = StudyRepository.getAssignment(assignmentId)
        if (a == null) {
            findNavController().navigateUp()
            return
        }
        val course = StudyRepository.getCourse(a.courseId)

        binding.textTitle.text = a.title
        binding.textCourse.text = course?.name ?: "-"
        binding.textDueDate.text = "${a.dueDate}  (${DateUtils.dDayLabel(a.dueDate)})"
        binding.textPriority.text = when (a.priority) {
            3 -> "High"
            2 -> "Medium"
            else -> "Low"
        }
        binding.textStatus.text =
            if (a.isCompleted) "Completed on ${a.completedDate}" else "Not completed"
        binding.textDescription.text = a.description ?: "No description"
        binding.colorBar.setBackgroundColor(Color.parseColor(course?.colorHex ?: "#9AA0A6"))
        binding.btnComplete.text = if (a.isCompleted) "Mark as Not Completed" else "Mark as Complete"

        binding.btnComplete.setOnClickListener {
            StudyRepository.toggleCompleted(assignmentId)
            findNavController().navigateUp()
        }
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_assignmentDetailFragment_to_addEditAssignmentFragment,
                bundleOf("assignmentId" to assignmentId)
            )
        }
        binding.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete assignment?")
                .setMessage("This cannot be undone.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete") { _, _ ->
                    StudyRepository.deleteAssignment(assignmentId)
                    findNavController().navigateUp()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
