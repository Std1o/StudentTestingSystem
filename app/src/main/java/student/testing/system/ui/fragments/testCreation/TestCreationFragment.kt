package student.testing.system.ui.fragments.testCreation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import student.testing.system.R

class TestCreationFragment : Fragment() {

    companion object {
        fun newInstance() = TestCreationFragment()
    }

    private lateinit var viewModel: TestCreationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.test_creation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TestCreationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}