package student.testing.system.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.models.CourseResponse
import student.testing.system.api.network.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.confirmAction
import student.testing.system.common.showSnackbar
import student.testing.system.common.subscribeInUI
import student.testing.system.databinding.CoursesFragmentBinding
import student.testing.system.ui.adapters.CoursesAdapter
import student.testing.system.ui.dialogFragments.CourseAddingDialogFragment
import student.testing.system.viewmodels.CoursesViewModel


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

            override fun onLongClick(courseId: Int, courseOwnerId: Int) {
                confirmAction(R.string.delete_request) { _, _ ->
                    deleteCourse(courseId, courseOwnerId)
                }
            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = coursesAdapter
        getCourses()
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
        viewModel.getCourses().subscribeInUI(this, binding.progressBar) {
            coursesAdapter.setDataList(it as MutableList<CourseResponse>)
        }
    }

    private fun deleteCourse(courseId: Int, courseOwnerId: Int) {
        viewModel.deleteCourse(courseId, courseOwnerId).subscribeInUI(this, binding.progressBar) {
            coursesAdapter.deleteById(it)
        }
    }
}