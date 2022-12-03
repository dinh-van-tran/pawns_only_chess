package chess

class Pawn(player: Player = Player.BLACK, occupiedCell: Cell): ChessPiece(player, occupiedCell) {
    private var firstMove = true

    override fun checkValidMove(destinationCell: Cell): ChessGame.MoveResult {
        val destinationPawn = destinationCell.chessPieceWeakReference.get()
        if (destinationPawn != null) {
            return if (this.player == destinationPawn.player) {
                ChessGame.MoveResult.INVALID_INPUT
            } else {
                // TODO: handle capture enemy piece
                ChessGame.MoveResult.INVALID_INPUT
            }
        }

        val currentCell = this.occupiedCell
        val isSameColumn = currentCell.x == destinationCell.x
        if (!isSameColumn) {
            return ChessGame.MoveResult.INVALID_MOVE
        }

        val yOffset = if (player == Player.WHITE) {
            destinationCell.y - currentCell.y
        } else {
            currentCell.y - destinationCell.y
        }
        val isPawnMoveOneStepForward = (yOffset == 1)
        val isPawnMoveOneOrTwoStepForward = (yOffset in 1..2)

        if (firstMove && isPawnMoveOneOrTwoStepForward) {
            firstMove = false
            return ChessGame.MoveResult.VALID
        }

        return if (isPawnMoveOneStepForward) {
            ChessGame.MoveResult.VALID
        } else {
            ChessGame.MoveResult.INVALID_MOVE
        }
    }
}
