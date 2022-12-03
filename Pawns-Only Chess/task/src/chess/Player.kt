package chess

enum class Player {
    BLACK(
        printableCharacter = "B",
        printableSideName = "Black",
        name = "black",
        moveDirection = -1,
        initRowPosition = 7,
        winningRow = 1
    ) {

        override fun nextPlayer(): Player {
            return WHITE
        }

    },

    WHITE(
        printableCharacter = "W",
        printableSideName = "White",
        name = "white",
        moveDirection = 1,
        initRowPosition = 2,
        winningRow = 8
    ) {

        override fun nextPlayer(): Player {
            return BLACK
        }

    }; // end of the constants

    var printableSideCharacter: String = ""
    var printableSideName: String = ""
    var printableName : String = ""
    val chessPieceList: MutableList<ChessPiece> = mutableListOf()
    var moveDirection: Int = 1
    var moveCount: Int = 0
    var initRowPosition: Int = 0
    var winningRow: Int = 0

    constructor(
        printableCharacter: String,
        printableSideName: String,
        name: String,
        moveDirection: Int,
        initRowPosition: Int,
        winningRow: Int,
    ) {
        this.printableSideCharacter = printableCharacter
        this.printableSideName = printableSideName
        this.printableName = name
        this.moveDirection = moveDirection
        this.initRowPosition = initRowPosition
        this.winningRow = winningRow
    }

    abstract fun nextPlayer(): Player

    fun initChessPieces() {
        for (initColumnPosition in 'a'..'h') {
            val occupiedCell = Board.INSTANCE.getCell("$initColumnPosition$initRowPosition")
            chessPieceList += Pawn(player = this, occupiedCell = occupiedCell)
        }
    }

    fun getChessPiece(position: String): ChessPiece? {
        for (chessPiece in chessPieceList) {
            if (chessPiece?.occupiedCell?.getPosition() == position) {
                return chessPiece
            }
        }

        return null
    }

    fun move(currentPosition: String, destinationPosition: String): ChessGame.MoveResult {
        val (checkResult, currentPiece, destinationCell) = checkValidMove(currentPosition, destinationPosition)
        if (checkResult != ChessGame.MoveResult.VALID) {
            return checkResult
        }

        var moveResult = currentPiece!!.moveOrCapture(destinationCell!!)
        if (moveResult == ChessGame.MoveResult.SUCCESSFUL) {
            increaseMoveCount(currentPiece)
        }

        return moveResult
    }

    /**
     * Manual set chess piece for testing.
     * @param currentPosition
     * @param destinationPosition
     */
    fun setPositionForPiece(currentPosition: String, destinationPosition: String) {
        val currentPiece = getChessPiece(currentPosition) ?: return
        val destinationCell = Board.INSTANCE.getCell(destinationPosition)

        currentPiece.setPosition(destinationCell)
    }

    fun capturePiece(currentPosition: String) {
        val currentPiece = getChessPiece(currentPosition) ?: return
        currentPiece.capture()
    }


    private fun increaseMoveCount(currentPiece: ChessPiece) {
        moveCount++
        currentPiece.moveCount = moveCount
    }

    private fun checkValidMove(currentPosition: String, destinationPosition: String): Triple<ChessGame.MoveResult, ChessPiece?, Cell?> {
        val inValidInput = Triple(ChessGame.MoveResult.INVALID_INPUT, null, null)
        if (currentPosition == destinationPosition) {
            return inValidInput
        }

        val currentPiece = getChessPiece(currentPosition)
            ?: return Triple(ChessGame.MoveResult.NO_PIECE, null, null)

        val destinationCell = Board.INSTANCE.getCell(destinationPosition)
        if (destinationCell == null) {
            return inValidInput
        }

        val destinationPiece = destinationCell.chessPieceWeakReference.get()
        if (destinationPiece != null && destinationPiece.player == this) {
            return inValidInput
        }

        return Triple(ChessGame.MoveResult.VALID, currentPiece, destinationCell)
    }

    fun isWon(): Boolean {
        for (chessPiece in chessPieceList) {
            if (chessPiece?.occupiedCell?.y == winningRow) {
                return true
            }
        }

        return false
    }

    fun isBeCapturedAllPieces(): Boolean {
        for (chessPiece in chessPieceList) {
            if (chessPiece.occupiedCell != null) {
                return false
            }
        }

        return true
    }

    fun isStalemate(): Boolean {
        for (chessPiece in chessPieceList) {
            if (chessPiece.havePossibleMoves()) {
                return false
            }
        }

        return true
    }
}
