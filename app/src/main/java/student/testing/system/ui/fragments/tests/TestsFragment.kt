package student.testing.system.ui.fragments.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.network.DataState
import student.testing.system.databinding.FragmentTestsBinding
import student.testing.system.models.Test
import student.testing.system.ui.adapters.CoursesAdapter
import student.testing.system.ui.adapters.TestsAdapter
import student.testing.system.ui.fragments.CourseSharedViewModel
import student.testing.system.ui.fragments.courses.CoursesFragment
import student.testing.system.ui.fragments.courses.CoursesViewModel

@AndroidEntryPoint
class TestsFragment : Fragment() {

    private var _binding: FragmentTestsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: CourseSharedViewModel by activityViewModels()
    private val viewModel by viewModels<TestsViewModel>()
    lateinit var testsAdapter: TestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(TestsViewModel::class.java)

        _binding = FragmentTestsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) return
        val course = arguments?.getSerializable(CoursesFragment.ARG_COURSE) as CourseResponse
        binding.tvCode.text = getString(R.string.course_code, course.courseCode)
        sharedViewModel.setCourse(course)

        testsAdapter = TestsAdapter(object : TestsAdapter.ClickListener {
            override fun onClick(test: Test) {
                // TODO
            }

            override fun onLongClick(courseId: Int) {
                // TODO
            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = testsAdapter
        getTests(course.id)
    }

    private fun getTests(courseId: Int) {
        lifecycleScope.launch {
            viewModel.getTests(courseId).collect {
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
                        testsAdapter.setDataList(it.data as MutableList<Test>)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}