package chess

abstract class ChessPiece(val side: Chess.Side = Chess.Side.BLACK, var occupiedCell: Cell) {

    init {
        occupiedCell.occupyCell(this)
    }

    fun move(newPosition: Cell): Boolean {
        if (!checkValidMove(newPosition)) {
            return false
        }

        occupiedCell.releaseCell()
        newPosition.occupyCell(this)

        return true
    }

    abstract fun checkValidMove(destinationCell: Cell): Boolean
}
