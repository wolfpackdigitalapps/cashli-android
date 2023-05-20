package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.membership.MembershipAdvanceItem
import com.wolfpackdigital.cashli.presentation.membership.MembershipAdvanceItemType

@BindingAdapter("itemType")
fun TextView.setItemStatus(item: MembershipAdvanceItem?) {
    item?.let {
        typeface = when (it.itemType) {
            MembershipAdvanceItemType.MEMBERSHIP -> {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.size_16sp))
                ResourcesCompat.getFont(context, R.font.lato_semi_bold)
            }

            MembershipAdvanceItemType.CASH_ADVANCE -> {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.size_14sp))
                ResourcesCompat.getFont(context, R.font.lato_bold)
            }
        }
        setTextColor(ContextCompat.getColor(context, it.statusTint))
        setText(item.status)
    }
}

@BindingAdapter("itemType")
fun ImageView.setItemStatus(item: MembershipAdvanceItem?) {
    item?.let {
        drawableRes(it.statusDrawableRes)
        setTintRes(item.statusTint)
    }
}

@BindingAdapter("bottomTextType")
fun TextView.setBottomTextValue(item: MembershipAdvanceItem?) {
    item?.let {
        text = when (it) {
            is MembershipAdvanceItem.MembershipActiveItem,
            is MembershipAdvanceItem.MembershipPausedItem -> {
                context.getString(R.string.membership_amount_value, item.bottomValue)
            }

            else -> item.bottomValue
        }
    }
}
