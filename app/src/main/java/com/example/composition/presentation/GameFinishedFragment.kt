package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

@Suppress("DEPRECATION")
class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()
        bindViews()
    }

    private fun setUpClickListener() {
        binding.buttonAgain.setOnClickListener {
            retryGame()
        }
    }

    private fun bindViews() {
        binding.ivEmojiResult.setImageResource(getSmileResId())
        binding.tvRequiredNumberAnswers.text = String.format(
            getString(R.string.required_number_of_correct_answers_s),
            gameResult.gameSettings.minCountOfRightAnswers
        )
        binding.tvScore.text = String.format(
            getString(R.string.your_score_s),
            gameResult.countOfRightAnswers
        )
        binding.tvPercentageAnswers.text = String.format(
            getString(R.string.percentage_of_correct_answers_s),
            getPercentageOfRightAnswers()
        )
        binding.tvRequiredPercentageAnswers.text = String.format(
            getString(R.string.required_percentage_of_correct_answers_s),
            gameResult.gameSettings.minPercentOfRightAnswers
        )
    }

    private fun getSmileResId(): Int {
        return if (gameResult.winner) {
            R.drawable.smile
        } else {
            R.drawable.sad
        }
    }

    private fun getPercentageOfRightAnswers() {
        if (gameResult.countOfQuestions == 0) {
            0
        } else {
            ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    companion object {

        const val KEY_GAME_RESULT = "game result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}