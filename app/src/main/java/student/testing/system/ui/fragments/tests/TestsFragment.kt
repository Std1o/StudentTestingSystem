package student.testing.system.ui.fragments.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import student.testing.system.R
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.databinding.FragmentTestsBinding
import student.testing.system.ui.fragments.CourseSharedViewModel
import student.testing.system.ui.fragments.courses.CoursesFragment

class TestsFragment : Fragment() {

    private var _binding: FragmentTestsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sharedViewModel: CourseSharedViewModel by activityViewModels()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}