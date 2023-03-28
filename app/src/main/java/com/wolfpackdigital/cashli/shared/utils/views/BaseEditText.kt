package com.wolfpackdigital.cashli.shared.utils.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.BaseEditTextBinding
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.visibility

class BaseEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val binding =
        BaseEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    var text: String?
        get() = binding.tietContent.text?.toString() ?: EMPTY_STRING
        set(value) {
            binding.tietContent.setText(value)
        }
}

@BindingAdapter("cliText")
fun BaseEditText.setCliText(content: LiveData<String>?) {
    if (text != content?.value) {
        text = content?.value ?: ""
    }
}

@InverseBindingAdapter(attribute = "cliText")
fun BaseEditText.getCliText() = text

@BindingAdapter("cliInputType")
fun BaseEditText.cliInputType(type: Int?) {
    binding.tietContent.inputType = type ?: InputType.TYPE_CLASS_TEXT
    binding.tietContent.text?.length?.let { binding.tietContent.setSelection(it) }
}

@BindingAdapter("cliHint")
fun BaseEditText.cliHint(hint: String?) {
    hint ?: return
    binding.tietContent.hint = hint
}

@BindingAdapter("cliHintRes")
fun BaseEditText.cliHintRes(@StringRes hintRes: Int?) {
    hintRes ?: return
    binding.tietContent.hint = context.getString(hintRes)
}

@BindingAdapter("cliLabel")
fun BaseEditText.cliLabel(label: String?) {
    label ?: return
    binding.tvLabel.text = label
}

@BindingAdapter("cliLabelRes")
fun BaseEditText.cliLabelRes(@StringRes labelRes: Int?) {
    labelRes ?: return
    binding.tvLabel.text = context.getString(labelRes)
}

@BindingAdapter("cliMaxChars")
fun BaseEditText.cliMaxChars(max: Int?) {
    max ?: return
    binding.tietContent.apply {
        filters = filters.toMutableList().also {
            it.add(InputFilter.LengthFilter(max))
        }.toTypedArray()
    }
}

@BindingAdapter("cliImeOptions")
fun BaseEditText.cliImeOptions(options: Int?) {
    binding.tietContent.imeOptions = options ?: EditorInfo.IME_ACTION_DONE
}

@BindingAdapter("cliError")
fun BaseEditText.cliError(riError: Int?) {
    val error = riError?.let(context::getString) ?: ""
    binding.tvError.text = error
    binding.tvError.visibility(error.isNotEmpty())
    binding.tietContent.setError(error.isNotEmpty())
}

@BindingAdapter("cliDrawableEnd")
fun BaseEditText.cliDrawable(end: Drawable?) {
    end?.let {
        binding.ivDrawableEnd.visibility = View.VISIBLE
        binding.ivDrawableEnd.setImageDrawable(it)
    }
}
