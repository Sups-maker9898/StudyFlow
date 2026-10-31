package com.jeiu.studyflow.ui

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeiu.studyflow.data.Assignment
import com.jeiu.studyflow.data.DateUtils
import com.jeiu.studyflow.data.StudyRepository
import com.jeiu.studyflow.databinding.ItemAssignmentBinding

class AssignmentAdapter(
    private val onItemClick: (Assignment) -> Unit,
    private val onCheckClick: (Assignment) -> Unit
) : ListAdapter<Assignment, AssignmentAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Assignment>() {
        override fun areItemsTheSame(a: Assignment, b: Assignment) = a.assignmentId == b.assignmentId
        override fun areContentsTheSame(a: Assignment, b: Assignment) = a == b
    }

    inner class VH(val b: ItemAssignmentBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemAssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val course = StudyRepository.getCourse(item.courseId)
        val b = holder.b

        b.textTitle.text = item.title
        b.textCourse.text = course?.name ?: "No course"
        b.textDue.text = "Due ${item.dueDate}"
        b.cbDone.isChecked = item.isCompleted
        b.colorBar.setBackgroundColor(Color.parseColor(course?.colorHex ?: "#9AA0A6"))

        // strike-through for completed work
        b.textTitle.paintFlags =
            if (item.isCompleted) b.textTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else b.textTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        // D-day badge: red when urgent / overdue
        if (item.isCompleted) {
            b.textDday.text = "Done"
            b.textDday.setTextColor(Color.parseColor("#2EA05A"))
        } else {
            b.textDday.text = DateUtils.dDayLabel(item.dueDate)
            b.textDday.setTextColor(
                Color.parseColor(if (DateUtils.isUrgent(item.dueDate)) "#D64545" else "#2166D6")
            )
        }

        val (pText, pColor) = when (item.priority) {
            3 -> "HIGH" to "#D64545"
            2 -> "MED" to "#F09628"
            else -> "LOW" to "#2EA05A"
        }
        b.textPriority.text = pText
        b.textPriority.setTextColor(Color.parseColor(pColor))

        b.root.setOnClickListener { onItemClick(item) }
        b.cbDone.setOnClickListener { onCheckClick(item) }
    }
}
