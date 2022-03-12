package student.testing.system.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import student.testing.system.R
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.databinding.FragmentCourseReviewBinding
import student.testing.system.ui.fragments.courses.CoursesFragment

class CourseReviewFragment : Fragment() {

    private var _binding: FragmentCourseReviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) return
        val course = arguments?.getSerializable(CoursesFragment.ARG_COURSE) as CourseResponse


        val navView: BottomNavigationView = binding.navView
        val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_course_review)
        val arg1 = NavArgument.Builder().setDefaultValue(course).build()
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.course_review_navigation)
        navGraph.addArgument(CoursesFragment.ARG_COURSE, arg1)
        navController.graph = navGraph
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("destination", destination.id.toString())
            Log.d("required destination", R.id.navigation_users.toString())
            when(destination.id) {
                R.id.navigation_users -> {
                    destination.addArgument(CoursesFragment.ARG_COURSE, arg1)
                }
            }
        }
        navView.setupWithNavController(navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}