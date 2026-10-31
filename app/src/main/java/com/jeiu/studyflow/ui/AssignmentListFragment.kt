package com.jeiu.studyflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeiu.studyflow.R
import com.jeiu.studyflow.data.AssignmentFilter
import com.jeiu.studyflow.data.StudyRepository
import com.jeiu.studyflow.databinding.FragmentAssignmentListBinding

class AssignmentListFragment : Fragment() {

    private var _binding: FragmentAssignmentListBinding? = null
    private val binding: FragmentAssignmentListBinding
        get() = _binding!!

    private lateinit var adapter: AssignmentAdapter
    private var filter = AssignmentFilter.PENDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AssignmentAdapter(
            onItemClick = { a ->
                findNavController().navigate(
                    R.id.action_assignmentListFragment_to_assignmentDetailFragment,
                    bundleOf("assignmentId" to a.assignmentId)
                )
            },
            onCheckClick = { a ->
                StudyRepository.toggleCompleted(a.assignmentId)
                refresh()
            }
        )
        binding.recyclerAssignments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAssignments.adapter = adapter

        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            filter = when (checkedIds.firstOrNull()) {
                R.id.chipAll -> AssignmentFilter.ALL
                R.id.chipDone -> AssignmentFilter.DONE
                else -> AssignmentFilter.PENDING
            }
            refresh()
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_assignmentListFragment_to_addEditAssignmentFragment)
        }

        refresh()
    }

    private fun refresh() {
        val list = StudyRepository.getAssignments(filter)
        adapter.submitList(list)
        binding.textEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
