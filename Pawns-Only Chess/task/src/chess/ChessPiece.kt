package chess

abstract class ChessPiece(val player: Player = Player.BLACK, var occupiedCell: Cell) {

    init {
        occupiedCell.occupyCell(this)
    }

    fun move(newPosition: Cell): ChessGame.MoveResult {
        val checkResult = checkValidMove(newPosition)
        if (checkResult != ChessGame.MoveResult.VALID) {
            return checkResult
        }

        occupiedCell.releaseCell()
        assert(occupiedCell.chessPieceWeakReference.get() == null)

        occupiedCell = newPosition
        newPosition.occupyCell(this)
        assert(occupiedCell.x == newPosition.x && occupiedCell.y == newPosition.y)
        assert(newPosition.chessPieceWeakReference.get() == this)

        return ChessGame.MoveResult.SUCCESSFUL
    }

    abstract fun checkValidMove(destinationCell: Cell): ChessGame.MoveResult
}
