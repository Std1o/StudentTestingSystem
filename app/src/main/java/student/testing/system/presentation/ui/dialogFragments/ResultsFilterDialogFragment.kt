package student.testing.system.presentation.ui.dialogFragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import student.testing.system.common.formatToString
import student.testing.system.common.showSnackbar
import student.testing.system.databinding.DialogResultsFilterBinding
import student.testing.system.models.enums.OrderingType
import student.testing.system.models.enums.OrderingType.Companion.getOrderingTypes
import student.testing.system.presentation.viewmodels.ResultsSharedViewModel
import java.util.*

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
            initDateInputLayout(dateFromInputLayout)
            initDateInputLayout(dateToInputLayout)
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

            cbScoreEquals.isChecked = viewModel.scoreEqualsEnabled
            cbScoreEquals.setOnCheckedChangeListener { _, isChecked ->
                viewModel.scoreEqualsEnabled = isChecked
                scoreInputLayout.isEnabled = isChecked
                scoreInputLayout.editText?.isEnabled = isChecked

                viewModel.ratingRangeEnabled = !isChecked
                rangeSlider.isEnabled = viewModel.ratingRangeEnabled
            }
        }
    }

    private fun initRangeSlider() {
        with(binding) {
            rangeSlider.isEnabled = viewModel.ratingRangeEnabled
            rangeSlider.valueFrom = 0f
            rangeSlider.valueTo = viewModel.maxScore.toFloat()
            rangeSlider.values = listOf(viewModel.lowerBound, viewModel.upperBound)
            rangeSlider.addOnChangeListener { rangeSlider, _, _ ->
                viewModel.lowerBound = rangeSlider.values[0]
                viewModel.upperBound = rangeSlider.values[1]
            }
        }
    }

    private fun initDateInputLayout(textInputLayout: TextInputLayout) {
        when(textInputLayout.id) {
            R.id.dateFromInputLayout -> {
                if (viewModel.dateFrom != null) {
                    textInputLayout.editText?.setText(viewModel.dateFrom)
                }
            }
            R.id.dateToInputLayout -> {
                if (viewModel.dateTo != null) {
                    textInputLayout.editText?.setText(viewModel.dateTo)
                }
            }
        }
        textInputLayout.editText?.keyListener = null
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .build()
        val onClickListener = OnClickListener() {
            datePicker.show(parentFragmentManager, "datePicker")
        }
        textInputLayout.setOnClickListener(onClickListener)
        textInputLayout.editText?.setOnClickListener(onClickListener)
        datePicker.addOnPositiveButtonClickListener {
            datePicker.selection?.let { date ->
                val dateStr = Date(date).formatToString("yyyy-MM-dd")
                textInputLayout.editText?.setText(dateStr)
                when(textInputLayout.id) {
                    R.id.dateFromInputLayout -> viewModel.dateFrom = dateStr
                    R.id.dateToInputLayout -> viewModel.dateTo = dateStr
                }
            }
        }
    }

    private fun initScoreInputLayout() {
        with(binding) {
            if (viewModel.scoreEqualsValue != null) {
                scoreInputLayout.editText?.setText(viewModel.scoreEqualsValue.toString())
            }
            scoreInputLayout.isEnabled = viewModel.scoreEqualsEnabled
            scoreInputLayout.editText?.isEnabled = viewModel.scoreEqualsEnabled

            scoreInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) return@doOnTextChanged
                viewModel.scoreEqualsValue = text.toString().toFloat()
            }
        }
    }

    private fun initOrderingTypeSelector() {
        val items = getOrderingTypes()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val orderingTypeSelector = (binding.orderingType.editText as? AutoCompleteTextView)
        if (viewModel.orderingType != null) {
            orderingTypeSelector?.setText(viewModel.orderingType.toString())
        }
        orderingTypeSelector?.onItemClickListener =
            OnItemClickListener { _, _, position, _ -> viewModel.orderingType = items[position] }
        orderingTypeSelector?.setAdapter(adapter)
    }

    companion object {
        private const val ARG_TEST_ID = "testId"
        private const val ARG_COURSE_ID = "courseId"

        fun newInstance(testId: Int, courseId: Int): ResultsFilterDialogFragment {
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