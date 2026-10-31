package com.jeiu.studyflow.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeiu.studyflow.data.Course
import com.jeiu.studyflow.data.StudyRepository
import com.jeiu.studyflow.databinding.ItemCourseBinding

class CourseAdapter(
    private val onItemClick: (Course) -> Unit
) : ListAdapter<Course, CourseAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(a: Course, b: Course) = a.courseId == b.courseId
        override fun areContentsTheSame(a: Course, b: Course) = a == b
    }

    inner class VH(val b: ItemCourseBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val b = holder.b
        b.textName.text = item.name
        b.textInfo.text = "${item.professor ?: "-"}  •  ${item.semester}"
        b.textPending.text = "${StudyRepository.pendingCount(item.courseId)} pending"
        b.colorDot.background.mutate().setTint(Color.parseColor(item.colorHex))
        b.root.setOnClickListener { onItemClick(item) }
    }
}
