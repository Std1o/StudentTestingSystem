package student.testing.system.presentation.ui.dialogFragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.showSnackbar
import student.testing.system.databinding.DialogResultsFilterBinding
import student.testing.system.models.enums.OrderingType
import student.testing.system.models.enums.OrderingType.Companion.getOrderingTypes
import student.testing.system.presentation.viewmodels.ResultsSharedViewModel

@AndroidEntryPoint
class ResultsFilterDialogFragment : BottomSheetDialogFragment() {

    private var _binding: DialogResultsFilterBinding? = null
    private val binding get() = _binding!!
    val viewModel: ResultsSharedViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogResultsFilterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)

        with(binding) {
            initCheckBoxes()
            initScoreInputLayout()
            initRangeSlider()
            initOrderingTypeSelector()
            btnSave.setOnClickListener {
                viewModel.getResults(
                    requireArguments().getInt(ARG_TEST_ID),
                    requireArguments().getInt(ARG_COURSE_ID)
                )
                dismiss()
            }
        }
    }

    private fun initCheckBoxes() {
        with(binding) {
            cbOnlyMaxResults.isChecked = viewModel.showOnlyMaxResults
            cbOnlyMaxResults.setOnCheckedChangeListener { _, isChecked ->
                viewModel.showOnlyMaxResults = isChecked
            }

            cbScoreEquals.isChecked = viewModel.scoreEquals
            cbScoreEquals.setOnCheckedChangeListener { _, isChecked ->
                viewModel.scoreEquals = isChecked
                scoreInputLayout.isEnabled = isChecked
                scoreInputLayout.editText?.isEnabled = isChecked
            }
        }
    }

    private fun initRangeSlider() {
        with(binding) {
            rangeSlider.valueFrom = 0f
            rangeSlider.valueTo = viewModel.maxScore.toFloat()
            rangeSlider.values = listOf(viewModel.lowerBound, viewModel.upperBound)
            rangeSlider.addOnChangeListener { rangeSlider, _, _ ->
                viewModel.lowerBound = rangeSlider.values[0]
                viewModel.upperBound = rangeSlider.values[1]
            }
        }
    }

    private fun initScoreInputLayout() {
        with(binding) {
            if (viewModel.scoreEqualsValue != null) {
                scoreInputLayout.editText?.setText(viewModel.scoreEqualsValue.toString())
            }
            scoreInputLayout.isEnabled = viewModel.scoreEquals
            scoreInputLayout.editText?.isEnabled = viewModel.scoreEquals

            scoreInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) return@doOnTextChanged
                viewModel.scoreEqualsValue = text.toString().toFloat()
            }
        }
    }

    private fun initOrderingTypeSelector() {
        val items = getOrderingTypes()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.orderingType.editText as? AutoCompleteTextView)?.onItemClickListener =
            OnItemClickListener { _, _, position, _ -> viewModel.orderingType = items[position] }
        (binding.orderingType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    companion object {
        private const val ARG_TEST_ID = "testId"
        private const val ARG_COURSE_ID = "courseId"

        fun newInstance(testId: Int, courseId: Int,): ResultsFilterDialogFragment {
            return ResultsFilterDialogFragment().apply {
                arguments = bundleOf(ARG_TEST_ID to testId, ARG_COURSE_ID to courseId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}