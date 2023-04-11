@file:Suppress("TooManyFunctions")

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
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.BaseEditTextBinding
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced
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
        text = content?.value ?: EMPTY_STRING
    }
}

@InverseBindingAdapter(attribute = "cliText")
fun BaseEditText.getCliText() = text

@BindingAdapter("cliTextAttrChanged")
fun BaseEditText.setTextListener(attrChange: InverseBindingListener) {
    binding.tietContent.doOnTextChanged { _, _, _, _ ->
        attrChange.onChange()
    }
}

@BindingAdapter("onCliImeDoneAction")
fun BaseEditText.setOnImeDoneActionListener(callback: () -> Unit) {
    binding.tietContent.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}

@BindingAdapter("cliInputType")
fun BaseEditText.cliInputType(type: Int?) {
    binding.tietContent.apply {
        val typeface = binding.tietContent.typeface
        inputType = type ?: InputType.TYPE_CLASS_TEXT
        setTypeface(typeface)
        text?.length?.let { setSelection(it) }
    }
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
    binding.tvLabel.visibility = View.VISIBLE
}

@BindingAdapter("cliLabelRes")
fun BaseEditText.cliLabelRes(@StringRes labelRes: Int?) {
    labelRes ?: return
    binding.tvLabel.text = context.getString(labelRes)
    binding.tvLabel.visibility = View.VISIBLE
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

@BindingAdapter(value = ["cliError", "cliShowTextError"], requireAll = false)
fun BaseEditText.cliError(cliError: Int?, cliShowTextError: Boolean?) {
    val error = cliError?.let(context::getString) ?: EMPTY_STRING
    val showTextError = cliShowTextError ?: true
    if (showTextError)
        binding.tvError.apply {
            text = error
            visibility(error.isNotEmpty())
        }
    binding.tietContent.setError(error.isNotEmpty())
}

@BindingAdapter("cliDrawableEnd")
fun BaseEditText.cliDrawableEnd(end: Drawable?) {
    end?.let {
        binding.ivDrawableEnd.visibility = View.VISIBLE
        binding.ivDrawableEnd.setImageDrawable(it)
    }
}

@BindingAdapter("cliDrawableEndClick")
fun BaseEditText.cliDrawableEndClick(callback: () -> Unit?) {
    binding.ivDrawableEnd.setOnClickDebounced {
        callback()
    }
}
