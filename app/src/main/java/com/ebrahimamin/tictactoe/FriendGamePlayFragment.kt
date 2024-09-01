package com.ebrahimamin.tictactoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class FriendGamePlayFragment : Fragment() {

    private lateinit var player1TextView: TextView
    private lateinit var player2TextView: TextView
    private lateinit var acknowledgmentTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var replayButton: MaterialButton
    private lateinit var currentPlayerTurn: String

    private val boardButtons = Array(3) { arrayOfNulls<ImageButton>(3) }
    private var board = Array(3) { arrayOfNulls<String>(3) }
    private var player1Score = 0
    private var player2Score = 0
    private var isPlayer1Turn = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_game_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        player1TextView = view.findViewById(R.id.player1TextView)
        player2TextView = view.findViewById(R.id.player2TextView)
        acknowledgmentTextView = view.findViewById(R.id.acknowledgmentTextView)
        replayButton = view.findViewById(R.id.replayButton)
        scoreTextView = view.findViewById(R.id.score)

        // Initialize the game board buttons
        boardButtons[0][0] = view.findViewById(R.id.imageButton1)
        boardButtons[0][1] = view.findViewById(R.id.imageButton2)
        boardButtons[0][2] = view.findViewById(R.id.imageButton3)
        boardButtons[1][0] = view.findViewById(R.id.imageButton4)
        boardButtons[1][1] = view.findViewById(R.id.imageButton5)
        boardButtons[1][2] = view.findViewById(R.id.imageButton6)
        boardButtons[2][0] = view.findViewById(R.id.imageButton7)
        boardButtons[2][1] = view.findViewById(R.id.imageButton8)
        boardButtons[2][2] = view.findViewById(R.id.imageButton9)

        // Set up button click listeners
        for (i in 0..2) {
            for (j in 0..2) {
                boardButtons[i][j]?.setOnClickListener { onBoardButtonClick(i, j) }
            }
        }

        // Set up the replay button
        replayButton.setOnClickListener { resetGame() }

        // Set initial player turn
        currentPlayerTurn = "Player 1"
        updateTurnText()
        updateScoreText()  // Initialize score display
    }

    private fun onBoardButtonClick(row: Int, col: Int) {
        if (board[row][col] != null) return // Ignore if already clicked

        // Set the symbol based on the current player's turn
        board[row][col] = if (isPlayer1Turn) "X" else "O"
        boardButtons[row][col]?.setImageResource(if (isPlayer1Turn) R.drawable.ic_x else R.drawable.ic_o)

        // Check if there is a winner
        if (checkForWin()) {
            handleWin()
        } else if (isBoardFull()) {
            handleDraw()
        } else {
            switchTurn()
        }
    }

    private fun checkForWin(): Boolean {
        // Check rows, columns, and diagonals for a win
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != null) return true
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != null) return true
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != null) return true
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != null) return true
        return false
    }

    private fun isBoardFull(): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == null) return false
            }
        }
        return true
    }

    private fun handleWin() {
        acknowledgmentTextView.visibility = View.VISIBLE
        if (isPlayer1Turn) {
            player1Score++
            acknowledgmentTextView.text = "Player 1 Wins!"
            highlightWinningCells("X")
        } else {
            player2Score++
            acknowledgmentTextView.text = "Player 2 Wins!"
            highlightWinningCells("O")
        }
        updateScoreText()
        replayButton.visibility = View.VISIBLE
    }

    private fun highlightWinningCells(symbol: String) {
        val winningCells = mutableListOf<Pair<Int, Int>>()

        // Check rows and columns for a win
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] == symbol) {
                winningCells.add(Pair(i, 0))
                winningCells.add(Pair(i, 1))
                winningCells.add(Pair(i, 2))
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] == symbol) {
                winningCells.add(Pair(0, i))
                winningCells.add(Pair(1, i))
                winningCells.add(Pair(2, i))
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] == symbol) {
            winningCells.add(Pair(0, 0))
            winningCells.add(Pair(1, 1))
            winningCells.add(Pair(2, 2))
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] == symbol) {
            winningCells.add(Pair(0, 2))
            winningCells.add(Pair(1, 1))
            winningCells.add(Pair(2, 0))
        }

        // Highlight the winning cells
        for ((row, col) in winningCells) {
            boardButtons[row][col]?.setBackgroundResource(R.drawable.win)
        }
    }

    private fun handleDraw() {
        acknowledgmentTextView.visibility = View.VISIBLE
        acknowledgmentTextView.text = "It's a Draw!"
        replayButton.visibility = View.VISIBLE
    }

    private fun switchTurn() {
        isPlayer1Turn = !isPlayer1Turn
        currentPlayerTurn = if (isPlayer1Turn) "Player 1" else "Player 2"
        updateTurnText()
    }

    private fun updateTurnText() {
        acknowledgmentTextView.text = "$currentPlayerTurn's Turn"
    }

    private fun updateScoreText() {
        // Update the score text view with the current scores
        scoreTextView.text = "$player1Score - $player2Score"
    }

    private fun resetGame() {
        board = Array(3) { arrayOfNulls<String>(3) }
        for (i in 0..2) {
            for (j in 0..2) {
                boardButtons[i][j]?.apply {
                    setImageResource(0)
                    setBackgroundResource(R.drawable.edit_text_shape)
                }
            }
        }
        acknowledgmentTextView.visibility = View.GONE
        replayButton.visibility = View.GONE
        isPlayer1Turn = true
        currentPlayerTurn = "Player 1"
        updateTurnText()
        updateScoreText()  // Reset score display
    }
}
