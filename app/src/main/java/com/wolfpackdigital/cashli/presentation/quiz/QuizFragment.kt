package com.wolfpackdigital.cashli.presentation.quiz

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.QuizFragmentBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuizFragment : BaseFragment<QuizFragmentBinding, QuizViewModel>(R.layout.fr_quiz) {

    override val viewModel by viewModel<QuizViewModel>()

    override fun setupViews() {
        return
    }
}
