package chess

class ChessGame {
    private val board: Board = Board()
    private var playerTurn: Player = Player.WHITE

    fun play() {
        println("Pawns-Only Chess")
        println("First Player's name:")
        playerTurn.printableName = readln()

        println("Second Player's name:")
        playerTurn.nextPlayer().printableName = readln()

        board.print()

        while (true) {
            println("${playerTurn.printableName}'s turn:")
            val command = readln()
            if (command == "exit") {
                println("Bye!")
                break
            }

            val (currentPositionOrNull, destinationPositionOrNull) = parseMovePositions(command)
            val moveResult = move(currentPositionOrNull, destinationPositionOrNull)
            when (moveResult) {
                MoveResult.INVALID_INPUT, MoveResult.INVALID_MOVE -> println("Invalid Input")
                MoveResult.NO_PIECE -> println("No ${playerTurn.printableSideName} pawn at $currentPositionOrNull")
                MoveResult.SUCCESSFUL -> board.print()
                else -> {

                }
            }
        }
    }

    enum class MoveResult {
        VALID,
        INVALID_INPUT,
        INVALID_MOVE,
        NO_PIECE,
        SUCCESSFUL,
    }

    private fun move(currentPositionOrNull: String?, destinationPositionOrNull: String?): MoveResult {
        if (currentPositionOrNull == null || destinationPositionOrNull == null) {
            return MoveResult.INVALID_INPUT
        }

        val currentPosition = currentPositionOrNull!!
        val destinationPosition = destinationPositionOrNull!!

        val moveResult = board.move(currentPosition, destinationPosition, playerTurn)
        when (moveResult) {
            MoveResult.SUCCESSFUL -> {
                switchPlayer()
            }
            else -> {}
        }

        return moveResult
    }

    private fun parseMovePositions(moveString: String): Pair<String?, String?> {
        val moveStringTrim = moveString.trim()
        val validMoveRegex = Regex("^[a-h][1-8][a-h][1-8]$")
        if (!validMoveRegex.matches(moveStringTrim)) {
            return Pair(null, null)
        }

        val currentPosition = moveStringTrim.substring(startIndex = 0, endIndex = 2)
        val destinationPosition = moveStringTrim.substring(startIndex = 2)

        return Pair(currentPosition, destinationPosition)
    }

    private fun switchPlayer() {
        playerTurn = if (playerTurn == Player.BLACK) {
            Player.WHITE
        } else {
            Player.BLACK
        }
    }
}