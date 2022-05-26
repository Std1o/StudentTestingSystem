package student.testing.system.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import student.testing.system.databinding.ItemParticipantBinding
import student.testing.system.databinding.ItemUserResultBinding
import student.testing.system.models.Answer
import student.testing.system.models.TestResult


class UserResultAdapter(private val testResult: TestResult) :
    RecyclerView.Adapter<UserResultAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemUserResultBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun getItemCount() = testResult.questions.count()

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        with(holder) {
            binding.tvIndex.text = "${position + 1}"
            binding.tvScore.text = testResult.score.toString()
            binding.tvQuestion.text = testResult.questions[position].question
            binding.rv.layoutManager = LinearLayoutManager(binding.root.context)
            val adapter = AnswersAdapter(testResult.questions[position].answers as ArrayList<Answer>)
            binding.rv.adapter = adapter
        }
    }

    inner class CourseViewHolder(val binding: ItemUserResultBinding) :
        RecyclerView.ViewHolder(binding.root)
}