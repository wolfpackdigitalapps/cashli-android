package com.wolfpackdigital.cashli.presentation.quiz

import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.QuizFragmentBinding
import com.wolfpackdigital.cashli.databinding.TipRadioButtonBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setImageTint
import com.wolfpackdigital.cashli.shared.utils.extensions.percentOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuizFragment : BaseFragment<QuizFragmentBinding, QuizViewModel>(R.layout.fr_quiz) {

    override val viewModel by viewModel<QuizViewModel>()
    private val args by navArgs<QuizFragmentArgs>()

    override fun setupViews() {
        setupTipAmountSection()
        setupSliderLabel()
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

    @Suppress("MagicNumber")
    private fun setupSliderLabel() {
        viewModel.tipAmountPerc.observe(viewLifecycleOwner) { perc ->
            binding?.llTipAmount?.findViewById<SeekBar>(R.id.sb_tip_amount)?.let { sb ->
                binding?.llTipAmount?.findViewById<TextView>(R.id.tv_tip_amount)?.apply {
                    // TODO Change this
                    val percentage = perc / 10f
                    text = getString(
                        R.string.quiz_tip_amount_slider,
                        percentage,
                        percentage.percentOf(args.cashAmount)
                    )
                    x = sb.thumb.bounds.exactCenterX()
                }
            }
        }
    }
}
