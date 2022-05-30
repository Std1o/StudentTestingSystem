package student.testing.system.ui.adapters

import agency.tango.android.avatarview.IImageLoader
import agency.tango.android.avatarview.loader.PicassoLoader
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import student.testing.system.R
import student.testing.system.models.Participant
import student.testing.system.common.AccountSession
import student.testing.system.common.showIf
import student.testing.system.databinding.ItemParticipantBinding


class ParticipantsAdapter(private val dataList: List<Participant>,
                          private val moderators: List<Participant>,
                          private val courseOwnerId: Int) :
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
            binding.ivStar.showIf(participant.id == courseOwnerId || participant in moderators)
            if (participant in moderators) {
                binding.ivStar.setColorFilter(R.color.gray)
            }
        }
    }

    inner class CourseViewHolder(val binding: ItemParticipantBinding) :
        RecyclerView.ViewHolder(binding.root)
}