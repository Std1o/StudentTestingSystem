package student.testing.system.ui.adapters

import agency.tango.android.avatarview.IImageLoader
import agency.tango.android.avatarview.loader.PicassoLoader
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import student.testing.system.api.models.courses.Participant
import student.testing.system.common.AccountSession
import student.testing.system.databinding.ItemParticipantBinding


class ParticipantsAdapter(private val dataList: List<Participant>, private val courseOwnerId: Int) :
    RecyclerView.Adapter<ParticipantsAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemParticipantBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        with(holder) {
            val participant: Participant = dataList[position]
            val imageLoader: IImageLoader = PicassoLoader()
            imageLoader.loadImage(binding.avatarView, "nothing", participant.username)
            binding.tvName.text = participant.username
            if (courseOwnerId == AccountSession.instance.userId) {
                binding.tvMail.text = participant.email
            }
        }
    }

    inner class CourseViewHolder(val binding: ItemParticipantBinding) :
        RecyclerView.ViewHolder(binding.root)
}