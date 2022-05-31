package student.testing.system.ui.dialogFragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.api.network.DataState
import student.testing.system.common.TextResultClickListener
import student.testing.system.common.showSnackbar
import student.testing.system.databinding.FragmentCourseAddingDialogBinding
import student.testing.system.ui.fragments.CoursesFragment.Companion.ARG_COURSE
import student.testing.system.ui.fragments.CoursesFragment.Companion.KEY_COURSE_ADDING
import student.testing.system.viewmodels.CourseAddingViewModel


@AndroidEntryPoint
class CourseAddingDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<CourseAddingViewModel>()
    private var _binding: FragmentCourseAddingDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCourseAddingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.create.setOnClickListener() {
            val listener = object : TextResultClickListener {
                override fun onClick(text: String) {
                    createCourse(text)
                }
            }
            showAlertDialog("Создать курс", "Введите название", "Создать", listener)
        }
        binding.join.setOnClickListener() {
            val listener = object : TextResultClickListener {
                override fun onClick(text: String) {
                    joinCourse(text)
                }
            }
            showAlertDialog("Присоединиться к курсу", "Код курса", "Присоединиться", listener)
        }
    }

    private fun showAlertDialog(
        title: String,
        hint: String,
        positiveBtnText: String,
        listener: TextResultClickListener
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        val input = EditText(requireContext())
        input.hint = hint
        input.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        builder.setView(input)
        builder.setPositiveButton(positiveBtnText) { dialog, which ->
            listener.onClick(input.text.toString())
        }
        builder.setNegativeButton("Отмена", { dialog, which -> dialog.cancel() })
        builder.show()
    }

    private fun createCourse(name: String) {
        lifecycleScope.launch {
            viewModel.createCourse(name).collect {
                when (it) {
                    is DataState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is DataState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showSnackbar(it.exception)
                    }
                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val result = Bundle()
                        result.putSerializable(ARG_COURSE, it.data)
                        parentFragmentManager.setFragmentResult(KEY_COURSE_ADDING, result)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun joinCourse(courseCode: String) {
        lifecycleScope.launch {
            viewModel.joinCourse(courseCode).collect {
                when (it) {
                    is DataState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is DataState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        val snackbar =
                            Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val result = Bundle()
                        result.putSerializable(ARG_COURSE, it.data)
                        parentFragmentManager.setFragmentResult(KEY_COURSE_ADDING, result)
                        dismiss()
                        Log.d("joined to course", it.data.toString())
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): CourseAddingDialogFragment {
            return CourseAddingDialogFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}