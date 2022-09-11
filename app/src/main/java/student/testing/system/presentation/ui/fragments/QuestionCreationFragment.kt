package student.testing.system.presentation.ui.fragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.*
import student.testing.system.databinding.FragmentQuestionCreationBinding
import student.testing.system.models.Answer
import student.testing.system.models.Question
import student.testing.system.presentation.ui.adapters.AnswersAdapter
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@AndroidEntryPoint
class QuestionCreationFragment : Fragment(R.layout.fragment_question_creation) {

    private val binding by viewBinding(FragmentQuestionCreationBinding::bind)
    private lateinit var adapter: AnswersAdapter
    private val viewModel: TestCreationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnswersAdapter(arrayListOf(), true) {
            confirmAction(R.string.delete_request) { _, _ ->
                adapter.deleteItem(it)
            }
        }
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        binding.btnAdd.setOnClickListener() {
            addAnswer()
        }
        binding.btnSave.setOnClickListener {
            if (!binding.etQuestion.isNotEmpty()) return@setOnClickListener
            for (ans in adapter.dataList) {
                if (ans.isRight) {
                    viewModel.addQuestion(Question(binding.etQuestion.text.trimString(), adapter.dataList))
                    requireActivity().onBackPressed()
                    return@setOnClickListener
                }
            }
            showSnackbar(R.string.assign_correct_answers)
        }
    }

    private fun addAnswer() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle(R.string.answer_adding)
        val input = EditText(requireContext())
        input.hint = getString(R.string.input_answer)
        input.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        builder.setView(input)
        builder.setPositiveButton(R.string.ok) { _, _ ->
            if (input.text.trimString().isEmpty()) {
                showSnackbar(R.string.error_empty_answer)
            } else {
                adapter.addItem(Answer(input.text.trimString(), false))
            }
        }
        builder.setNegativeButton(R.string.cancel, null)
        builder.show()
    }
}