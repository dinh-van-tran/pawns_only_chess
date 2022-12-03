package chess

enum class ChessGame {
    INSTANCE;

    private var playerTurn: Player = Player.WHITE

    init {
        initPawns()
    }

    private fun initPawns() {
        Player.BLACK.initChessPieces()
        Player.WHITE.initChessPieces()
    }

    fun play() {
        println("Pawns-Only Chess")
        println("First Player's name:")
        playerTurn.printableName = readln()

        println("Second Player's name:")
        playerTurn.nextPlayer().printableName = readln()

        Board.INSTANCE.print()

        while (true) {
            if (!eventLoop()) {
                break
            }
        }
    }

    private fun eventLoop(): Boolean {
        println("${playerTurn.printableName}'s turn:")
        val command = readln()
        if (command == "exit") {
            println("Bye!")
            return false
        }

        val (currentPositionOrNull, destinationPositionOrNull) = parseMovePositions(command)
        val moveResult = move(currentPositionOrNull, destinationPositionOrNull)

        when (moveResult) {
            MoveResult.INVALID_INPUT, MoveResult.INVALID_MOVE -> println("Invalid Input")
            MoveResult.NO_PIECE -> println("No ${playerTurn.printableSideName.lowercase()} pawn at $currentPositionOrNull")
            MoveResult.SUCCESSFUL -> {
                Board.INSTANCE.print()
                val (isWon, wonPlayer) = checkIsWon()
                if (isWon) {
                    println("${wonPlayer?.printableSideName} Wins!")
                    println("Bye!")
                    return false
                }

                if (checkIsStalemate()) {
                    println("Stalemate!")
                    println("Bye!")
                    return false
                }
            }
            else -> {

            }
        }

        return true
    }

    enum class MoveResult {
        VALID,
        INVALID_INPUT,
        INVALID_MOVE,
        NO_PIECE,
        SUCCESSFUL,
    }

    private fun move(currentPosition: String?, destinationPosition: String?): MoveResult {
        if (currentPosition == null || destinationPosition == null) {
            return MoveResult.INVALID_INPUT
        }

        val moveResult = playerTurn.move(currentPosition, destinationPosition)
        when (moveResult) {
            MoveResult.SUCCESSFUL -> {
                playerTurn = playerTurn.nextPlayer()
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

    private fun checkIsWon(): Pair<Boolean, Player?> {
        if (Player.BLACK.isWon()) {
            return Pair(true, Player.BLACK)
        }

        if (Player.WHITE.isWon()) {
            return Pair(true, Player.WHITE)
        }

        return checkIsBeCapturedAllPieces()
    }

    private fun checkIsBeCapturedAllPieces(): Pair<Boolean, Player?> {
        if (Player.BLACK.isBeCapturedAllPieces()) {
            return Pair(true, Player.WHITE)
        }

        if (Player.WHITE.isBeCapturedAllPieces()) {
            return Pair(true, Player.BLACK)
        }

        return Pair(false, null)
    }

    private fun checkIsStalemate(): Boolean {
        return Player.BLACK.isStalemate() || Player.WHITE.isStalemate()
    }
}