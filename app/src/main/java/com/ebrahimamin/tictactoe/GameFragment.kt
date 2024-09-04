package com.ebrahimamin.tictactoe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

class GameFragment : Fragment() {

    private lateinit var radioButtonX: RadioButton
    private lateinit var radioButtonO: RadioButton
    private lateinit var iconX: ImageView
    private lateinit var iconO: ImageView
    private lateinit var continueButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        radioButtonX = view.findViewById(R.id.radioButtonX)
        radioButtonO = view.findViewById(R.id.radioButtonO)
        iconX = view.findViewById(R.id.iconX)
        iconO = view.findViewById(R.id.iconO)
        continueButton = view.findViewById(R.id.continueButton)

        radioButtonX.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                radioButtonO.isChecked = false
                radioButtonX.buttonTintList = ContextCompat.getColorStateList(requireContext(), R.color.button_color)
                radioButtonO.buttonTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                iconX.alpha = 1.0f
                iconO.alpha = 0.3f
            }
        }

        radioButtonO.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                radioButtonX.isChecked = false
                radioButtonO.buttonTintList = ContextCompat.getColorStateList(requireContext(), R.color.button_color)
                radioButtonX.buttonTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                iconO.alpha = 1.0f
                iconX.alpha = 0.5f
            }
        }

        continueButton.setOnClickListener {
            onContinueClicked()
        }

        return view
    }

    private fun onContinueClicked() {
        when {
            radioButtonX.isChecked -> {
                val action = GameFragmentDirections.actionGameFragmentToGamePlayFragment(1)
                findNavController().navigate(action)
            }
            radioButtonO.isChecked -> {
                val action = GameFragmentDirections.actionGameFragmentToGamePlayFragment(2)
                findNavController().navigate(action)
            }
            else -> {
                Toast.makeText(context, getString(R.string.please_select_a_side), Toast.LENGTH_SHORT).show()
            }
        }
    }
}