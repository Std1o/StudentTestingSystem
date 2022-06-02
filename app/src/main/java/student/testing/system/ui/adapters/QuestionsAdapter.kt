package student.testing.system.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import student.testing.system.databinding.ItemQuestionBinding
import student.testing.system.models.Question
import student.testing.system.models.Test


class QuestionsAdapter(var dataList: ArrayList<Question>, val listener: (Int) -> Unit) :
    RecyclerView.Adapter<QuestionsAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemQuestionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    fun submitData(dataList: ArrayList<Question>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        with(holder) {
            val question = dataList[position]
            binding.tvTitle.text = question.question
            binding.cv.setOnLongClickListener {
                listener.invoke(position)
                true
            }
        }
    }

    inner class CourseViewHolder(val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)
}