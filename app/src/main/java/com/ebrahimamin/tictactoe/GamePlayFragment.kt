package com.ebrahimamin.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlin.random.Random

class GamePlayFragment : Fragment() {

    private lateinit var buttons: Array<Array<ImageButton>>
    private lateinit var playerScoreText: TextView
    private lateinit var aiScoreText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var acknowledgmentTextView: TextView
    private lateinit var replayButton: Button
    private lateinit var tableLayout: TableLayout
    private lateinit var toggleButton: ToggleButton
    private lateinit var computerNameTextView: TextView

    private var playerScore = 0
    private var aiScore = 0
    private var board = Array(3) { Array(3) { "" } }
    private var playerSymbol = "X"
    private var aiSymbol = "O"
    private var playerTurn = true
    private var isHard = false // Default difficulty to Hard

    private val handler = Handler(Looper.getMainLooper())
    private val args: GamePlayFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userSymbol = args.userSymbol

        if (userSymbol == 1) {
            playerSymbol = "X"
            aiSymbol = "O"
        } else {
            playerSymbol = "O"
            aiSymbol = "X"
        }

        playerScoreText = view.findViewById(R.id.textView)
        aiScoreText = view.findViewById(R.id.textView2)
        progressBar = view.findViewById(R.id.progressBar)
        acknowledgmentTextView = view.findViewById(R.id.acknowledgmentTextView)
        replayButton = view.findViewById(R.id.replayButton)
        tableLayout = view.findViewById(R.id.tableLayout)
        toggleButton = view.findViewById(R.id.toggleButton)
        computerNameTextView = view.findViewById(R.id.computerTextView) // Ensure this ID matches your XML

        updateScore()

        buttons = arrayOf(
            arrayOf(
                view.findViewById(R.id.imageButton1),
                view.findViewById(R.id.imageButton2),
                view.findViewById(R.id.imageButton3)
            ),
            arrayOf(
                view.findViewById(R.id.imageButton4),
                view.findViewById(R.id.imageButton5),
                view.findViewById(R.id.imageButton6)
            ),
            arrayOf(
                view.findViewById(R.id.imageButton7),
                view.findViewById(R.id.imageButton8),
                view.findViewById(R.id.imageButton9)
            )
        )

        for (row in buttons.indices) {
            for (col in buttons[row].indices) {
                buttons[row][col].setOnClickListener {
                    onButtonClick(row, col)
                }
            }
        }

        replayButton.setOnClickListener {
            onReplayButtonClick()
        }

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            isHard = isChecked
            updateComputerName()
            resetBoard()
        }
    }

    private fun updateComputerName() {
        computerNameTextView.text = if (isHard) getString(R.string.computer_hard) else getString(R.string.computer_easy)
    }

    private fun onButtonClick(row: Int, col: Int) {
        if (board[row][col].isEmpty() && playerTurn) {
            board[row][col] = playerSymbol
            buttons[row][col].setImageResource(if (playerSymbol == "X") R.drawable.ic_x else R.drawable.ic_o)
            if (checkForWinner(playerSymbol)) {
                playerScore++
                updateScore()
                showAcknowledgment(getString(R.string.you_wins))
                highlightWinningCells(playerSymbol)
                replayButton.visibility = View.VISIBLE
            } else if (isBoardFull()) {
                showAcknowledgment(getString(R.string.draw))
                replayButton.visibility = View.VISIBLE
            } else {
                playerTurn = false
                showProgressBarForAiMove()
            }
        }
    }

    private fun showProgressBarForAiMove() {
        progressBar.visibility = View.VISIBLE
        handler.postDelayed({
            progressBar.visibility = View.GONE
            aiMove()
        }, 1100)
    }

    private fun aiMove() {
        if (isHard) {
            makeSmartMove() // Advanced AI logic
        } else {
            makeRandomMove() // Simple random move for easy mode
        }
        if (checkForWinner(aiSymbol)) {
            aiScore++
            updateScore()
            showAcknowledgment(getString(R.string.computer_wins))
            highlightWinningCells(aiSymbol)
            replayButton.visibility = View.VISIBLE
        } else if (isBoardFull()) {
            showAcknowledgment(getString(R.string.draw))
            replayButton.visibility = View.VISIBLE
        } else {
            playerTurn = true
        }
    }

    private fun makeRandomMove() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (row in board.indices) {
            for (col in board[row].indices) {
                if (board[row][col].isEmpty()) {
                    emptyCells.add(Pair(row, col))
                }
            }
        }
        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells[Random.nextInt(emptyCells.size)]
            board[row][col] = aiSymbol
            buttons[row][col].setImageResource(if (aiSymbol == "X") R.drawable.ic_x else R.drawable.ic_o)
        }
    }



    private fun makeSmartMove() {
        val bestMove = findBestMove()
        if (bestMove != null) {
            val (row, col) = bestMove
            board[row][col] = aiSymbol
            buttons[row][col].setImageResource(if (aiSymbol == "X") R.drawable.ic_x else R.drawable.ic_o)
        }
    }

    private fun findBestMove(): Pair<Int, Int>? {
        var bestMove: Pair<Int, Int>? = null
        var bestValue = Int.MIN_VALUE
        for (row in board.indices) {
            for (col in board[row].indices) {
                if (board[row][col].isEmpty()) {
                    board[row][col] = aiSymbol
                    val moveValue = minimax(board, 0, false, Int.MIN_VALUE, Int.MAX_VALUE)
                    board[row][col] = "" // Undo move
                    if (moveValue > bestValue) {
                        bestMove = Pair(row, col)
                        bestValue = moveValue
                    }
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: Array<Array<String>>, depth: Int, isMaximizing: Boolean, alpha: Int, beta: Int): Int {
        val score = evaluateBoard()
        if (score == 10) return score - depth
        if (score == -10) return score + depth
        if (isBoardFull()) return 0

        var alphaTemp = alpha
        var betaTemp = beta

        return if (isMaximizing) {
            var best = Int.MIN_VALUE
            for (row in board.indices) {
                for (col in board[row].indices) {
                    if (board[row][col].isEmpty()) {
                        board[row][col] = aiSymbol
                        best = maxOf(best, minimax(board, depth + 1, false, alphaTemp, betaTemp))
                        board[row][col] = ""
                        alphaTemp = maxOf(alphaTemp, best)
                        if (betaTemp <= alphaTemp) break
                    }
                }
            }
            best
        } else {
            var best = Int.MAX_VALUE
            for (row in board.indices) {
                for (col in board[row].indices) {
                    if (board[row][col].isEmpty()) {
                        board[row][col] = playerSymbol
                        best = minOf(best, minimax(board, depth + 1, true, alphaTemp, betaTemp))
                        board[row][col] = ""
                        betaTemp = minOf(betaTemp, best)
                        if (betaTemp <= alphaTemp) break
                    }
                }
            }
            best
        }
    }




    private fun evaluateBoard(): Int {
        // Check rows, columns, and diagonals for a win condition
        for (i in 0..2) {
            if (board[i][0] == aiSymbol && board[i][1] == aiSymbol && board[i][2] == aiSymbol) return 10
            if (board[i][0] == playerSymbol && board[i][1] == playerSymbol && board[i][2] == playerSymbol) return -10
            if (board[0][i] == aiSymbol && board[1][i] == aiSymbol && board[2][i] == aiSymbol) return 10
            if (board[0][i] == playerSymbol && board[1][i] == playerSymbol && board[2][i] == playerSymbol) return -10
        }
        if (board[0][0] == aiSymbol && board[1][1] == aiSymbol && board[2][2] == aiSymbol) return 10
        if (board[0][0] == playerSymbol && board[1][1] == playerSymbol && board[2][2] == playerSymbol) return -10
        if (board[0][2] == aiSymbol && board[1][1] == aiSymbol && board[2][0] == aiSymbol) return 10
        if (board[0][2] == playerSymbol && board[1][1] == playerSymbol && board[2][0] == playerSymbol) return -10
        return 0
    }

    private fun checkForWinner(symbol: String): Boolean {
        for (i in 0..2) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true
            }
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true
        }
        return false
    }

    private fun highlightWinningCells(symbol: String) {
        val drawableRes = if (symbol == playerSymbol) R.drawable.win else R.drawable.lose
        for (i in 0..2) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                buttons[i][0].setBackgroundResource(drawableRes)
                buttons[i][1].setBackgroundResource(drawableRes)
                buttons[i][2].setBackgroundResource(drawableRes)
                return
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                buttons[0][i].setBackgroundResource(drawableRes)
                buttons[1][i].setBackgroundResource(drawableRes)
                buttons[2][i].setBackgroundResource(drawableRes)
                return
            }
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            buttons[0][0].setBackgroundResource(drawableRes)
            buttons[1][1].setBackgroundResource(drawableRes)
            buttons[2][2].setBackgroundResource(drawableRes)
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            buttons[0][2].setBackgroundResource(drawableRes)
            buttons[1][1].setBackgroundResource(drawableRes)
            buttons[2][0].setBackgroundResource(drawableRes)
        }
    }

    private fun isBoardFull(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell.isEmpty()) return false
            }
        }
        return true
    }

    private fun updateScore() {
        playerScoreText.text = "$playerScore : $aiScore"
    }

    private fun showAcknowledgment(message: String) {
        acknowledgmentTextView.text = message
        acknowledgmentTextView.visibility = View.VISIBLE
        handler.postDelayed({
            acknowledgmentTextView.visibility = View.GONE
        }, 2000)
    }

    private fun resetBoard() {
        board = Array(3) { Array(3) { "" } }
        for (row in buttons) {
            for (button in row) {
                button.setImageResource(0) // Clear the image
                button.setBackgroundResource(R.drawable.edit_text_shape) // Restore default background
            }
        }
        playerTurn = true
        replayButton.visibility = View.GONE
        tableLayout.visibility = View.VISIBLE
    }

    private fun onReplayButtonClick() {
        resetBoard()
        tableLayout.visibility = View.VISIBLE
    }
}
