package chess

abstract class ChessPiece(val player: Player = Player.BLACK, var occupiedCell: Cell) {
    var moveCount: Int = 0

    init {
        occupiedCell.occupyCell(this)
    }

    enum class MoveType {
        MOVE,
        CAPTURE,
    }

    protected fun moveToNewPosition(newPosition: Cell) {
        occupiedCell.releaseCell()
        assert(occupiedCell.chessPieceWeakReference.get() == null)

        occupiedCell = newPosition
        newPosition.occupyCell(this)
        assert(occupiedCell.x == newPosition.x && occupiedCell.y == newPosition.y)
        assert(newPosition.chessPieceWeakReference.get() == this)
    }

    protected fun increasePlayerMoveCount() {
        player.moveCount++
        moveCount = player.moveCount++
    }

    fun capture() {
        occupiedCell.releaseCell()
        assert(occupiedCell.chessPieceWeakReference.get() == null)
    }

    abstract fun moveOrCapture(newPosition: Cell): ChessGame.MoveResult

    abstract protected fun move(newPosition: Cell): ChessGame.MoveResult

    abstract protected fun capture(newPosition: Cell): ChessGame.MoveResult

    abstract fun checkValidMove(destinationCell: Cell): ChessGame.MoveResult

    abstract fun checkValidCapture(destinationCell: Cell): Pair<ChessGame.MoveResult, ChessPiece?>

    abstract fun detectMoveType(destinationCell: Cell): MoveType
}
