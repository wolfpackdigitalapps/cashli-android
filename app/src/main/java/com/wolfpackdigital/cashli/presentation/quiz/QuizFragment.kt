package com.wolfpackdigital.cashli.presentation.quiz

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.compose.material.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.QuizFragmentBinding
import com.wolfpackdigital.cashli.databinding.TipRadioButtonBinding
import com.wolfpackdigital.cashli.domain.entities.requests.QuizAnswer
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setImageTint
import com.wolfpackdigital.cashli.shared.utils.extensions.percentOf
import com.wolfpackdigital.cashli.shared.utils.views.StyledSlider
import com.wolfpackdigital.cashli.shared.utils.views.StyledSliderUIState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class QuizFragment : BaseFragment<QuizFragmentBinding, QuizViewModel>(R.layout.fr_quiz) {

    private val args by navArgs<QuizFragmentArgs>()
    override val viewModel by viewModel<QuizViewModel> {
        parametersOf(
            args.cashAmount,
            args.deliveryMethod
        )
    }

    override fun setupViews() {
        setupQuestions()
        setupTipAmountSection()
        setupSliderView()
    }

    private fun setupTipAmountSection() {
        viewModel.quizAnswers.observe(viewLifecycleOwner) {}
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
        handleQuizFirstQuestionRadioGroup()
        handleQuizSecondQuestionRadioGroup()
        handleQuizThirdQuestionRadioGroup()
    }

    private fun handleQuizThirdQuestionRadioGroup() {
        binding?.quizSecondQuestionAlt?.questionRg?.apply {
            setOnCheckedChangeListener { group, checkedId ->
                if (checkedId > 0) {
                    val radio: RadioButton = group.findViewById(checkedId)
                    if (radio.isChecked) {
                        when (checkedId) {
                            R.id.rb_yes -> {
                                viewModel.setQuizAnswer(
                                    createQuizAnswer(QUIZ_THIRD_QUESTION_ID, true)
                                )
                            }

                            R.id.rb_no -> {
                                viewModel.setQuizAnswer(
                                    createQuizAnswer(QUIZ_THIRD_QUESTION_ID, false)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleQuizSecondQuestionRadioGroup() {
        binding?.quizSecondQuestion?.questionRg?.apply {
            setOnCheckedChangeListener { group, checkedId ->
                if (checkedId > 0) {
                    val radio: RadioButton = group.findViewById(checkedId)
                    if (radio.isChecked) {
                        when (checkedId) {
                            R.id.rb_yes -> {
                                viewModel.setQuizAnswer(
                                    createQuizAnswer(QUIZ_SECOND_QUESTION_ID, true)
                                )
                            }

                            R.id.rb_no -> {
                                viewModel.setQuizAnswer(
                                    createQuizAnswer(QUIZ_SECOND_QUESTION_ID, false)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleQuizFirstQuestionRadioGroup() {
        binding?.quizFirstQuestion?.questionRg?.apply {
            handleFirstQuestionPreselectedAnswer()
            setOnCheckedChangeListener { _, checkedId ->
                binding?.quizSecondQuestion?.questionRg?.clearCheck()
                binding?.quizSecondQuestionAlt?.questionRg?.clearCheck()
                when (checkedId) {
                    R.id.rb_yes -> {
                        viewModel.setSecondAltQuestionVisible(false)
                        viewModel.setQuizAnswer(
                            createQuizAnswer(QUIZ_FIRST_QUESTION_ID, true)
                        )
                    }

                    R.id.rb_no -> {
                        viewModel.setSecondAltQuestionVisible(true)
                        viewModel.setQuizAnswer(
                            createQuizAnswer(QUIZ_FIRST_QUESTION_ID, false)
                        )
                    }
                }
            }
        }
    }

    private fun RadioGroup.handleFirstQuestionPreselectedAnswer() {
        check(R.id.rb_yes)
        viewModel.setQuizAnswer(
            createQuizAnswer(QUIZ_FIRST_QUESTION_ID, true)
        )
    }

    private fun createQuizAnswer(answerId: Int, answer: Boolean): QuizAnswer {
        val stringAnswer = when (answer) {
            true -> QUIZ_ANSWERS_YES
            false -> QUIZ_ANSWERS_NO
        }
        return QuizAnswer(answerId, stringAnswer)
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
                    if (tipAmount.isChecked)
                        viewModel.onTipAmountSelected(index, tipAmount)
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
                        tipAmount = viewModel.tipAmount,
                        isInvertedColorScheme = true
                    ),
                    onAmountChanged = viewModel::setSliderValue
                )
            }
        }
    }

    companion object {
        const val QUIZ_ANSWERS_NO = "no"
        const val QUIZ_ANSWERS_YES = "yes"
        const val QUIZ_SECOND_QUESTION_ID = 2
        const val QUIZ_THIRD_QUESTION_ID = 3
        const val QUIZ_FIRST_QUESTION_ID = 1
    }
}
