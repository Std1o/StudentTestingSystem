package student.testing.system.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import student.testing.system.R
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.databinding.ActivityCourseReviewBinding
import student.testing.system.ui.fragments.courses.CoursesFragment

class CourseReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val course = intent.getSerializableExtra(CoursesFragment.ARG_COURSE) as CourseResponse
        
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_course_review)
        val arg1 = NavArgument.Builder().setDefaultValue(course).build()
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.course_review_navigation)
        navGraph.addArgument(CoursesFragment.ARG_COURSE, arg1)
        navController.graph = navGraph
        navView.setupWithNavController(navController)
    }
}