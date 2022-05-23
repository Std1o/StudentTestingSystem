package student.testing.system.ui.fragments.tests

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import student.testing.system.common.showIf
import student.testing.system.databinding.FragmentTestsBinding
import student.testing.system.models.Test
import student.testing.system.ui.adapters.TestsAdapter
import student.testing.system.ui.fragments.CourseSharedViewModel
import student.testing.system.ui.fragments.courses.CoursesFragment

@AndroidEntryPoint
class TestsFragment : Fragment() {

    private var _binding: FragmentTestsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: CourseSharedViewModel by activityViewModels()
    private val viewModel by viewModels<TestsViewModel>()
    lateinit var testsAdapter: TestsAdapter

    companion object {
        const val COURSE_CODE = "course_code"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) return
        val course = arguments?.getSerializable(CoursesFragment.ARG_COURSE) as CourseResponse
        binding.tvCode.text = getString(R.string.course_code, course.courseCode)
        sharedViewModel.setCourse(course)

        testsAdapter = TestsAdapter(object : TestsAdapter.ClickListener {
            override fun onClick(test: Test) {
                getResult(test.id, test.courseId)
            }

            override fun onLongClick(testId: Int) {
                // TODO
            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = testsAdapter
        getTests(course.id)

        binding.btnAdd.showIf(course.ownerId == AccountSession.instance.userId)
        binding.btnAdd.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CoursesFragment.ARG_COURSE, course)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_navigation_tests_to_testCreationFragment, bundle)
        }
        binding.tvCode.setOnLongClickListener {
            val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(COURSE_CODE, course.courseCode);
            clipboard.setPrimaryClip(clip)
            val snackbar =
                Snackbar.make(binding.root, R.string.course_code_copied, Snackbar.LENGTH_SHORT)
            snackbar.show()
            true
        }
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

    private fun getResult(testId: Int, courseId: Int) {
        lifecycleScope.launch {
            viewModel.getResult(testId, courseId).collect {
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