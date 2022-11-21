package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GameRepositoryImpl

    private val context = application

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private var countOfAnswers = 0
    private var countOfQuestion = 0

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentAnswers = MutableLiveData<Int>()
    val percentAnswers: LiveData<Int>
        get() = _percentAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughAnswers = MutableLiveData<Boolean>()
    val enoughAnswers: LiveData<Boolean>
        get() = _enoughAnswers

    private val _enoughPercentAnswers = MutableLiveData<Boolean>()
    val enoughPercentAnswers: LiveData<Boolean>
        get() = _enoughPercentAnswers

    private val _minPercentAnswers = MutableLiveData<Int>()
    val minPercentAnswers: LiveData<Int>
        get() = _minPercentAnswers

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult


    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentAnswers()
        _percentAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.right_answers),
            countOfAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughAnswers.value = countOfAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentAnswers.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentAnswers(): Int {
        return ((countOfAnswers / countOfAnswers.toDouble()) * 100).toInt()

    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfAnswers++
        }
        countOfQuestion++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercentAnswers.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formatTime(p0)

            }

            override fun onFinish() {
                finishTimer()
            }
        }
    }

    private fun finishTimer() {
        _gameResult.value = GameResult(
            enoughAnswers.value == true && enoughPercentAnswers.value == true,
            countOfAnswers,
            countOfQuestion,
            gameSettings
        )
    }

    private fun formatTime(p0: Long): String {
        val seconds = p0 / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftTime = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftTime)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}