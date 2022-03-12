package student.testing.system.ui.fragments.questionCreation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import student.testing.system.R

class QuestionCreationFragment : Fragment() {

    companion object {
        fun newInstance() = QuestionCreationFragment()
    }

    private lateinit var viewModel: QuestionCreationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.question_creation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QuestionCreationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}