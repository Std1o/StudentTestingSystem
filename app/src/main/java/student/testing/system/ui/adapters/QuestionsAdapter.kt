package student.testing.system.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import student.testing.system.databinding.ItemQuestionBinding
import student.testing.system.models.Question
import student.testing.system.models.Test


class QuestionsAdapter(var dataList: ArrayList<Question>) :
    RecyclerView.Adapter<QuestionsAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemQuestionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    @JvmName("setDataList1")
    fun setDataList(dataList: ArrayList<Question>) {
        this.dataList = dataList
        notifyDataSetChanged()
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