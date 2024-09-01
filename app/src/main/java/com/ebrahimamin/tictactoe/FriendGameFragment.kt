package com.ebrahimamin.tictactoe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import androidx.navigation.fragment.findNavController

class FriendGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player1EditText: EditText = view.findViewById(R.id.player1Name)
        val player2EditText: EditText = view.findViewById(R.id.player2Name)
        val continueButton: MaterialButton = view.findViewById(R.id.continueButton)

        continueButton.setOnClickListener {
            val player1Name = if (player1EditText.text.toString().isEmpty()) "Player 1" else player1EditText.text.toString()
            val player2Name = if (player2EditText.text.toString().isEmpty()) "Player 2" else player2EditText.text.toString()

            val action = FriendGameFragmentDirections.actionFriendGameFragmentToFriendGamePlayFragment(player1Name, player2Name)
            findNavController().navigate(action)
        }
    }
}