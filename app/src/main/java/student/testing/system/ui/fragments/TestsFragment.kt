package student.testing.system.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import student.testing.system.R
import student.testing.system.api.network.DataState
import student.testing.system.common.*
import student.testing.system.databinding.FragmentTestsBinding
import student.testing.system.models.CourseResponse
import student.testing.system.models.Test
import student.testing.system.ui.adapters.TestsAdapter
import student.testing.system.viewmodels.CourseSharedViewModel
import student.testing.system.viewmodels.TestsViewModel


@AndroidEntryPoint
class TestsFragment : Fragment(R.layout.fragment_tests) {

    private val binding by viewBinding(FragmentTestsBinding::bind)
    private val sharedViewModel: CourseSharedViewModel by activityViewModels()
    private val viewModel by viewModels<TestsViewModel>()
    lateinit var testsAdapter: TestsAdapter
    lateinit var selectedTest: Test

    companion object {
        const val COURSE_CODE = "course_code"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) return
        val course = arguments?.getSerializable(CoursesFragment.ARG_COURSE) as CourseResponse
        val userId = AccountSession.instance.userId
        val isUserModerator = course.moderators.any { it.id == userId } || course.ownerId == userId
        binding.tvCode.text = getString(R.string.course_code, course.courseCode)
        sharedViewModel.setCourse(course)

        testsAdapter = TestsAdapter(isUserModerator, object : TestsAdapter.ClickListener {
            override fun onClick(test: Test) {
                selectedTest = test
                if (isUserModerator) {
                    val action = TestsFragmentDirections.viewResults(test.id, test.courseId, course.ownerId)
                    findNavController().navigate(action)
                } else {
                    getResult(test.id, test.courseId, course.ownerId, isUserModerator)
                }
            }

            override fun onLongClick(test: Test) {
                selectedTest = test
                showOptionsDialog(course, test.id, isUserModerator)
            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = testsAdapter
        getTests(course.id)

        binding.btnAdd.showIf(isUserModerator)
        binding.btnAdd.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CoursesFragment.ARG_COURSE, course)
            findNavController().navigate(
                R.id.action_navigation_tests_to_testCreationFragment,
                bundle
            )
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Test>(
            TestCreationFragment.ARG_TEST
        )?.observe(viewLifecycleOwner) {
            testsAdapter.addItem(it)
        }
        binding.tvCode.setOnLongClickListener {
            val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(COURSE_CODE, course.courseCode);
            clipboard.setPrimaryClip(clip)
            showSnackbar(R.string.course_code_copied)
            true
        }
    }

    private fun getTests(courseId: Int) {
        viewModel.getTests(courseId).subscribeInUI(this, binding.progressBar) {
            testsAdapter.setDataList(it as MutableList<Test>)
        }
    }

    private fun getResult(testId: Int, courseId: Int, courseOwnerId: Int, isUserModerator: Boolean) {
        viewModel.getResult(testId, courseId).onEach {
            binding.progressBar.showIf(it is DataState.Loading)
            if (it is DataState.Success) {
                val action = TestsFragmentDirections.viewResult(it.data)
                findNavController().navigate(action)
            } else if (it is DataState.Error && it.code == 404) {
                val action = TestsFragmentDirections
                    .navigateToTestPassing(selectedTest, 0, courseOwnerId, isUserModerator)
                findNavController().navigate(action)
            } else if (it is DataState.Error) {
                showSnackbar(it.exception)
            }
        }.launchWhenStartedCollect(lifecycleScope)
    }

    private fun showOptionsDialog(course: CourseResponse, testId: Int, isUserModerator: Boolean) {
        val options = arrayOf("Проверить", "Удалить")
        MaterialAlertDialogBuilder(requireContext())
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val action = TestsFragmentDirections.navigateToTestPassing(
                            selectedTest,
                            0,
                            course.ownerId,
                            isUserModerator
                        )
                        findNavController().navigate(action)
                    }
                    1 -> {
                        confirmAction(R.string.delete_request) { _, _ ->
                            deleteTest(testId, course.id, course.ownerId)
                        }
                    }
                }
            }.show()
    }

    private fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int) {
        viewModel.deleteTest(testId, courseId, courseOwnerId)
            .subscribeInUI(this, binding.progressBar) {
                testsAdapter.deleteById(testId)
            }
    }
}