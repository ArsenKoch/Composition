package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

interface OnOptionClickListener {

    fun onClick(count: Int)
}

@BindingAdapter("requiredAnswers")

fun bindRequiredViews(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_number_of_correct_answers_s),
        count
    )
}

@BindingAdapter("score")

fun bindScore(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.your_score_s),
        count
    )
}

@BindingAdapter("requiredPercentageAnswers")

fun bindPercentageAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage_of_correct_answers_s),
        count
    )
}

@BindingAdapter("scorePercentage")

fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.percentage_of_correct_answers_s),
        getPercentageOfRightAnswers(gameResult)
    )
}

private fun getPercentageOfRightAnswers(gameResult: GameResult) {
    if (gameResult.countOfQuestions == 0) {
        0
    } else {
        ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("resultImageId")

fun bindResultImageId(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getSmileResId(winner))
}

private fun getSmileResId(winner: Boolean): Int {
    return if (winner) {
        R.drawable.smile
    } else {
        R.drawable.sad
    }
}

@BindingAdapter("enoughPercentAnswers")

fun bindEnoughPercentAnswers(progressBar: ProgressBar, enough: Boolean) {
    val color = getColorByState(enough, progressBar.context)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

private fun getColorByState(goodState: Boolean, context: Context): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("enoughAnswers")

fun bindEnoughAnswers(textView: TextView, enough: Boolean) {
    textView.setTextColor(getColorByState(enough, textView.context))
}

@BindingAdapter("numberAsText")

fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")

fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onClick(textView.text.toString().toInt())
    }
}
