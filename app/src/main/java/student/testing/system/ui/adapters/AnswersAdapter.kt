package student.testing.system.ui.adapters

import agency.tango.android.avatarview.IImageLoader
import agency.tango.android.avatarview.loader.PicassoLoader
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import student.testing.system.models.Participant
import student.testing.system.common.AccountSession
import student.testing.system.databinding.ItemMultiAnswerBinding
import student.testing.system.databinding.ItemParticipantBinding
import student.testing.system.models.Answer
import student.testing.system.models.CourseResponse


class AnswersAdapter(val dataList: ArrayList<Answer>) :
    RecyclerView.Adapter<AnswersAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemMultiAnswerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    fun addItem(item: Answer) {
        dataList += item
        notifyItemChanged(itemCount)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        with(holder) {
            val answer = dataList[position]
            binding.checkBox.text = answer.answer
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                answer.isRight = isChecked
            }
        }
    }

    inner class CourseViewHolder(val binding: ItemMultiAnswerBinding) :
        RecyclerView.ViewHolder(binding.root)
}