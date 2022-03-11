package student.testing.system.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.databinding.ItemCourseBinding

class CoursesAdapter() : RecyclerView.Adapter<CoursesAdapter.HoursViewHolder>() {

    private var dataList: List<CourseResponse> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val binding = ItemCourseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HoursViewHolder(binding)
    }

    fun addItem(item: CourseResponse) {
        dataList.plus(item)
        notifyItemChanged(itemCount + 1)
    }

    fun setDataList(dataList: List<CourseResponse>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        with(holder){
            with(dataList[position]) {
                val course = dataList[position]
                binding.tvName.text = course.name
                Glide.with(holder.itemView.context)
                    .load("http://ezapitest.ml/images/${course.img}")
                    .transform( CenterCrop(), RoundedCorners(16))
                    .into(binding.imageView);
            }
        }
    }

    inner class HoursViewHolder(val binding: ItemCourseBinding)
        :RecyclerView.ViewHolder(binding.root)

}