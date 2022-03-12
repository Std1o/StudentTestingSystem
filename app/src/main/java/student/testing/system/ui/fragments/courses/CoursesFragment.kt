package student.testing.system.ui.fragments.courses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.databinding.CoursesFragmentBinding
import student.testing.system.ui.fragments.CourseReviewFragment
import student.testing.system.ui.adapters.CoursesAdapter
import student.testing.system.ui.dialogFragments.courseAdding.CourseAddingDialogFragment


@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private val viewModel by viewModels<CoursesViewModel>()
    private lateinit var _binding: CoursesFragmentBinding
    private val binding get() = _binding

    companion object {
        const val KEY_COURSE_ADDING = "courseAdding"
        const val ARG_COURSE = "course"
    }

    lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoursesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coursesAdapter = CoursesAdapter(object : CoursesAdapter.ClickListener {
            override fun onClick(course: CourseResponse) {
                val bundle = Bundle()
                bundle.putSerializable(ARG_COURSE, course)
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_coursesFragment_to_courseReviewFragment, bundle)
            }

            override fun onLongClick(courseId: Int) {
                confirmDeletion(courseId)
            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = coursesAdapter
        getCourses()
        getUser()
        binding.btnAdd.setOnClickListener() {
            CourseAddingDialogFragment
                .newInstance()
                .show(requireActivity().supportFragmentManager, KEY_COURSE_ADDING);
        }
        setFragmentResultListener()
    }

    private fun setFragmentResultListener() {
        requireActivity()
            .supportFragmentManager
            .setFragmentResultListener(KEY_COURSE_ADDING, this) { requestKey, bundle ->
                val result = bundle.getSerializable(ARG_COURSE)
                coursesAdapter.addItem(result as CourseResponse)
            }
    }

    private fun getCourses() {
        lifecycleScope.launch {
            viewModel.getCourses().collect {
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
                        coursesAdapter.setDataList(it.data as MutableList<CourseResponse>)
                    }
                }
            }
        }
    }

    private fun confirmDeletion(courseId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Удалить?")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            deleteCourse(courseId)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun deleteCourse(courseId: Int) {
        lifecycleScope.launch {
            viewModel.deleteCourse(courseId).collect {
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
                        coursesAdapter.deleteById(it.data)
                    }
                }
            }
        }
    }

    private fun getUser() {
        lifecycleScope.launch {
            viewModel.getUser().collect {
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
                        AccountSession.instance.userId = it.data.id
                    }
                }
            }
        }
    }
}