package chess

abstract class ChessPiece(val player: Player, var occupiedCell: Cell?) {
    var moveCount: Int = 0
    var isCaptured: Boolean = false
        get() {
            return occupiedCell == null
        }

    init {
        occupiedCell?.occupyCell(this)
    }

    enum class MoveType {
        MOVE,
        CAPTURE,
    }

    fun setPosition(newPosition: Cell) {
        occupiedCell?.releaseCell()
        assert(occupiedCell?.chessPieceWeakReference?.get() == null)

        occupiedCell = newPosition
        newPosition.occupyCell(this)
        assert(occupiedCell?.x == newPosition.x && occupiedCell?.y == newPosition.y)
        assert(newPosition.chessPieceWeakReference.get() == this)
    }

    fun capture() {
        occupiedCell?.releaseCell()
        occupiedCell = null
    }

    abstract fun moveOrCapture(newPosition: Cell): ChessGame.MoveResult

    protected abstract fun move(newPosition: Cell): ChessGame.MoveResult

    protected abstract fun capture(newPosition: Cell): ChessGame.MoveResult

    abstract fun checkValidMove(destinationCell: Cell): ChessGame.MoveResult

    abstract fun checkValidCapture(destinationCell: Cell): Pair<ChessGame.MoveResult, ChessPiece?>

    abstract fun detectMoveType(destinationCell: Cell): MoveType

    abstract fun havePossibleMoves(): Boolean
}
