package student.testing.system.ui.dialogFragments.courseAdding

import android.os.Bundle
import android.text.InputType
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.api.network.DataState
import student.testing.system.databinding.FragmentCourseAddingDialogBinding

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
            showCourseCreationDialog()
        }
    }

    fun showCourseCreationDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Создать курс")
        val input = EditText(requireContext())
        input.hint = "Введите название"
        input.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        builder.setView(input)
        builder.setPositiveButton("Создать") { dialog, which ->
            var name = input.text.toString()
            createCourse(name)
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
                        val snackbar = Snackbar.make(binding.root, it.exception, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("created course", it.data.toString())
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): CourseAddingDialogFragment =
            CourseAddingDialogFragment().apply {

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}