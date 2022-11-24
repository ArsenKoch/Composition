package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

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
