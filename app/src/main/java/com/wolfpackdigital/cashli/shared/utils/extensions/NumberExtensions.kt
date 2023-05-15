package com.wolfpackdigital.cashli.shared.utils.extensions

private const val HUNDRED = 100f

fun Int.percentOf(number: Int): Float = this * (number / HUNDRED)
fun Int.percentOf(number: Float): Float = this * (number / HUNDRED)
fun Float.percentOf(number: Float): Float = this * (number / HUNDRED)
fun Float.percentOf(number: Int): Float = this * (number / HUNDRED)
