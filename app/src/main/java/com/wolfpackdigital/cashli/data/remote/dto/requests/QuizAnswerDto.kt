package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizAnswerDto(
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("answer") val answer: String
) : Parcelable
