package student.testing.system.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.databinding.ItemCourseBinding

class CoursesAdapter(val listener: ClickListener) :
    RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    private var dataList = mutableListOf<CourseResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    fun addItem(item: CourseResponse) {
        dataList += item
        notifyItemChanged(itemCount)
    }

    fun setDataList(dataList: MutableList<CourseResponse>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun deleteById(id: Int) {
        var position = getPositionById(id)
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun getPositionById(id: Int): Int {
        return dataList.indexOf(dataList.find { it.id == id })
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        with(holder) {
            val course = dataList[position]
            binding.tvName.text = course.name
            Glide.with(holder.itemView.context)
                .load("http://ezapitest.ml/images/${course.img}")
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.imageView)
            holder.itemView.setOnClickListener() {
                listener.onClick(course)
            }
            holder.itemView.setOnLongClickListener() {
                listener.onLongClick(course.id)
                true
            }
        }
    }

    inner class CourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ClickListener {
        fun onClick(course: CourseResponse)
        fun onLongClick(courseId: Int)
    }

}