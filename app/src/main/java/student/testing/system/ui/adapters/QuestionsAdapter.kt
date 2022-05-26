package student.testing.system.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
            binding.cv.setOnLongClickListener {
                confirmDeletion(position, binding.root.context)
                true
            }
        }
    }

    private fun confirmDeletion(position: Int, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Удалить?")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    inner class CourseViewHolder(val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)
}