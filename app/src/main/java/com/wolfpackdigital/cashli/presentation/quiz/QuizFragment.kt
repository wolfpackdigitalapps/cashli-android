package com.wolfpackdigital.cashli.presentation.quiz

import android.widget.RadioButton
import androidx.compose.material.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.QuizFragmentBinding
import com.wolfpackdigital.cashli.databinding.TipRadioButtonBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setImageTint
import com.wolfpackdigital.cashli.shared.utils.extensions.percentOf
import com.wolfpackdigital.cashli.shared.utils.views.StyledSlider
import com.wolfpackdigital.cashli.shared.utils.views.StyledSliderUIState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class QuizFragment : BaseFragment<QuizFragmentBinding, QuizViewModel>(R.layout.fr_quiz) {

    private val args by navArgs<QuizFragmentArgs>()
    override val viewModel by viewModel<QuizViewModel> { parametersOf(args.cashAmount) }

    override fun setupViews() {
        setupQuestions()
        setupTipAmountSection()
        setupSliderView()
    }

    private fun setupTipAmountSection() {
        viewModel.tipPercAmounts.observe(viewLifecycleOwner) { tipAmounts ->
            if (binding?.llTipSelection?.childCount == 0) {
                addTipAmountRadioButtons(tipAmounts)
            } else {
                tipAmounts.forEachIndexed { index, tipAmount ->
                    binding?.llTipSelection
                        ?.getChildAt(index)
                        ?.findViewById<RadioButton>(R.id.radio_button)
                        ?.isChecked = tipAmount.isChecked
                }
            }
        }
    }

    private fun setupQuestions() {
        binding?.quizFirstQuestion?.questionRg?.apply {
            check(R.id.rb_yes)
            setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_yes -> {
                        viewModel.setSecondAltQuestionVisible(false)
                    }

                    R.id.rb_no -> {
                        viewModel.setSecondAltQuestionVisible(true)
                    }
                }
            }
        }
    }

    private fun addTipAmountRadioButtons(tipAmounts: List<TipAmount>) {
        tipAmounts.forEachIndexed { index, tipAmount ->
            binding?.llTipSelection?.addView(
                TipRadioButtonBinding.inflate(layoutInflater).apply {
                    tipPerc = tipAmount.value
                    tipValue = tipAmount.value?.let {
                        context?.getString(
                            R.string.dollar_amount_float,
                            it.percentOf(args.cashAmount)
                        )
                    }
                    radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                        val tint = ContextCompat.getColor(
                            buttonView.context,
                            if (isChecked) R.color.colorGray76 else R.color.colorGrayB6
                        )
                        tvTipAmount.setTextColor(tint)
                        tvTipPerc.setTextColor(tint)
                        ivArrow.setImageTint(tint)
                    }
                    radioButton.isChecked = tipAmount.isChecked
                    clRoot.setOnClickListener { viewModel.onTipAmountSelected(index) }
                }.root
            )
        }
    }

    private fun setupSliderView() {
        binding?.cvTipAmount?.setContent {
            MaterialTheme {
                StyledSlider(
                    uiState = StyledSliderUIState.TipAmountSliderUIState(
                        sliderValue = viewModel.sliderValue,
                        tipAmountPerc = viewModel.tipAmountPerc,
                        tipAmount = viewModel.tipAmount,
                        isInvertedColorScheme = true
                    ),
                    onAmountChanged = viewModel::setSliderValue
                )
            }
        }
    }
}
