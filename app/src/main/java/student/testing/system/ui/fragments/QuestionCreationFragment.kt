package student.testing.system.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import student.testing.system.R
import student.testing.system.common.TextResultClickListener
import student.testing.system.databinding.QuestionCreationFragmentBinding
import student.testing.system.databinding.TestCreationFragmentBinding
import student.testing.system.models.Answer
import student.testing.system.ui.adapters.AnswersAdapter

class QuestionCreationFragment : Fragment() {

    private lateinit var _binding: QuestionCreationFragmentBinding
    private val binding get() = _binding
    private lateinit var adapter: AnswersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QuestionCreationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnswersAdapter(arrayListOf())
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        binding.btnAdd.setOnClickListener() {
            addAnswer()
        }
    }

    private fun addAnswer() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Добавление ответа")
        val input = EditText(requireContext())
        input.hint = "Введите ответ"
        input.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        builder.setView(input)
        builder.setPositiveButton("Ок") { dialog, which ->
            adapter.addItem(Answer(input.text.toString(), true, null))
        }
        builder.setNegativeButton("Отмена", { dialog, which -> dialog.cancel() })
        builder.show()
    }

}