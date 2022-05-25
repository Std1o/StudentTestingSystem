package student.testing.system.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import student.testing.system.databinding.ItemQuestionBinding
import student.testing.system.models.Question


class QuestionsAdapter(val dataList: ArrayList<Question>) :
    RecyclerView.Adapter<QuestionsAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemQuestionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    fun addItem(item: Question) {
        dataList += item
        notifyItemChanged(itemCount)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        with(holder) {
            val question = dataList[position]
            binding.tvTitle.text = question.question
        }
    }

    inner class CourseViewHolder(val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)
}