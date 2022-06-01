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
import student.testing.system.common.*
import student.testing.system.databinding.CoursesFragmentBinding
import student.testing.system.ui.adapters.CoursesAdapter
import student.testing.system.ui.dialogFragments.CourseAddingDialogFragment
import student.testing.system.viewmodels.CoursesViewModel


@AndroidEntryPoint
class CoursesFragment : Fragment(R.layout.courses_fragment) {

    private val viewModel by viewModels<CoursesViewModel>()
    private val binding by viewBinding(CoursesFragmentBinding::bind)
    lateinit var adapter: CoursesAdapter

    companion object {
        const val KEY_COURSE_ADDING = "courseAdding"
        const val ARG_COURSE = "course"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CoursesAdapter(object : CoursesAdapter.ClickListener {
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
        binding.rv.adapter = adapter
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
                adapter.addItem(result as CourseResponse)
            }
    }

    private fun getCourses() {
        viewModel.getCourses().subscribeInUI(this, binding.progressBar) {
            adapter.setDataList(it as MutableList<CourseResponse>)
        }
    }

    private fun deleteCourse(courseId: Int, courseOwnerId: Int) {
        viewModel.deleteCourse(courseId, courseOwnerId).subscribeInUI(this, binding.progressBar) {
            adapter.deleteById(it)
        }
    }
}