package student.testing.system.ui.fragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.confirmAction
import student.testing.system.common.showSnackbar
import student.testing.system.databinding.QuestionCreationFragmentBinding
import student.testing.system.models.Answer
import student.testing.system.models.Question
import student.testing.system.ui.adapters.AnswersAdapter
import student.testing.system.viewmodels.TestCreationViewModel

@AndroidEntryPoint
class QuestionCreationFragment : Fragment() {

    private lateinit var _binding: QuestionCreationFragmentBinding
    private val binding get() = _binding
    private lateinit var adapter: AnswersAdapter
    private val viewModel: TestCreationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QuestionCreationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnswersAdapter(arrayListOf(), true, object : AnswersAdapter.LongClickListener {
            override fun onLongClick(position: Int) {
                confirmAction(R.string.delete_request) { _, _ ->
                    adapter.deleteItem(position)
                }
            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        binding.btnAdd.setOnClickListener() {
            addAnswer()
        }
        binding.btnSave.setOnClickListener {
            for (ans in adapter.dataList) {
                if (ans.isRight) {
                    viewModel.addQuestion(Question(binding.etQuestion.text.toString(), adapter.dataList, null))
                    requireActivity().onBackPressed()
                    return@setOnClickListener
                }
            }
            showSnackbar(R.string.assign_correct_answers)
        }
    }

    private fun addAnswer() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Добавление ответа")
        val input = EditText(requireContext())
        input.hint = "Введите ответ"
        input.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        builder.setView(input)
        builder.setPositiveButton(R.string.ok) { _, _ ->
            adapter.addItem(Answer(input.text.toString(), false, null))
        }
        builder.setNegativeButton(R.string.cancel, null)
        builder.show()
    }

}