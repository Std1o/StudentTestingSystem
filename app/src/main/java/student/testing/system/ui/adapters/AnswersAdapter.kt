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


class AnswersAdapter(private val dataList: List<Answer>) :
    RecyclerView.Adapter<AnswersAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemMultiAnswerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        with(holder) {
            binding.checkBox.text = dataList[position].answer
        }
    }

    inner class CourseViewHolder(val binding: ItemMultiAnswerBinding) :
        RecyclerView.ViewHolder(binding.root)
}