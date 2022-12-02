package chess

class Pawn(side: Chess.Side = Chess.Side.BLACK, occupiedCell: Cell): ChessPiece(side, occupiedCell) {


    override fun checkValidMove(destinationCell: Cell): Boolean {
        val destinationPawn = destinationCell.chessPieceWeakReference.get()
        if (destinationPawn != null && this.side == destinationPawn.side) {
            return false
        }

        val currentCell = this.occupiedCell
        val yOffset = if (side == Chess.Side.BLACK) {
            currentCell.y - destinationCell.y
        } else {
            destinationCell.y - currentCell.y
        }

        val isPawnMoveOneStepForward = (yOffset == 1) && (currentCell.x == destinationCell.x)
        return isPawnMoveOneStepForward
    }
}
