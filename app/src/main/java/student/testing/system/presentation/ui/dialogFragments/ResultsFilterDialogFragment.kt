package student.testing.system.presentation.ui.dialogFragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.common.showSnackbar
import student.testing.system.databinding.DialogResultsFilterBinding
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
            nameLayout.editText?.doOnTextChanged { _, _, _, _ ->
                nameLayout.error = null
            }
            phoneLayout.editText?.doOnTextChanged { _, _, _, _ ->
                phoneLayout.error = null
            }
            rangeSlider.valueFrom = 0f
            rangeSlider.valueTo = viewModel.maxScore.toFloat()
            rangeSlider.values = listOf(rangeSlider.valueFrom, rangeSlider.valueTo)
        }
    }

    companion object {
        fun newInstance(): ResultsFilterDialogFragment = ResultsFilterDialogFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}