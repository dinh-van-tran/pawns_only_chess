package chess

import kotlin.math.abs

class Pawn(player: Player = Player.BLACK, occupiedCell: Cell): ChessPiece(player, occupiedCell) {
    private var firstMove = true

    override fun moveOrCapture(newPosition: Cell): ChessGame.MoveResult {
        val moveType = detectMoveType(newPosition)

        val moveResult = if(moveType == MoveType.MOVE) {
             move(newPosition)
        } else {
            capture(newPosition)
        }

        if (moveResult == ChessGame.MoveResult.SUCCESSFUL) {
            increasePlayerMoveCount()
        }

        return moveResult
    }

    override fun move(newPosition: Cell): ChessGame.MoveResult {
        val checkResult = checkValidMove(newPosition)
        if (checkResult != ChessGame.MoveResult.VALID) {
            return checkResult
        }

        moveToNewPosition(newPosition)

        increasePlayerMoveCount()

        return ChessGame.MoveResult.SUCCESSFUL
    }

    override fun capture(newPosition: Cell): ChessGame.MoveResult {
        val (checkResult, capturedChessPiece) = checkValidCapture(newPosition)
        if (checkResult != ChessGame.MoveResult.VALID) {
            return checkResult
        }

        capturedChessPiece?.capture()
        moveToNewPosition(newPosition)

        increasePlayerMoveCount()

        return ChessGame.MoveResult.SUCCESSFUL
    }

    override fun checkValidMove(destinationCell: Cell): ChessGame.MoveResult {
        val (pawnOnlyMoveForward, yOffset) = checkPawnOnlyMoveForward(destinationCell)
        if (!pawnOnlyMoveForward) {
            return ChessGame.MoveResult.INVALID_MOVE
        }

        val destinationPawn = destinationCell.chessPieceWeakReference.get()
        if (destinationPawn != null) {
            return ChessGame.MoveResult.INVALID_INPUT
        }

        val isPawnMoveOneOrTwoStepForward = (yOffset in 1..2)
        if (firstMove && isPawnMoveOneOrTwoStepForward) {
            firstMove = false
            return ChessGame.MoveResult.VALID
        }

        val isPawnMoveOneStepForward = (yOffset == 1)
        return if (isPawnMoveOneStepForward) {
            ChessGame.MoveResult.VALID
        } else {
            ChessGame.MoveResult.INVALID_MOVE
        }
    }

    override fun checkValidCapture(destinationCell: Cell): Pair<ChessGame.MoveResult, ChessPiece?> {
        val (pawnOnlyMoveForward, yOffset) = checkPawnOnlyMoveForward(destinationCell)
        if (!pawnOnlyMoveForward) {
            return Pair(ChessGame.MoveResult.INVALID_MOVE, null)
        }

        val isMoveOneStepForward = (yOffset == 1)
        val isMoveDiagonal = abs(this.occupiedCell.x - destinationCell.x) == 1
        val isValidPawnMoveForCapturing = isMoveOneStepForward && isMoveDiagonal
        if (!isValidPawnMoveForCapturing) {
            return Pair(ChessGame.MoveResult.INVALID_MOVE, null)
        }

        var capturedChessPiece = destinationCell.chessPieceWeakReference.get()
        if (capturedChessPiece == null) {
            val backPosition = Board.INSTANCE.getBackCell(destinationCell, player.moveDirection)
            val enPassantCapturedChessPiece = backPosition?.chessPieceWeakReference?.get()
            if (enPassantCapturedChessPiece == null) {
                return Pair(ChessGame.MoveResult.INVALID_MOVE, null)
            }

            val isEnPassantHappensRightAway = (player.moveCount - moveCount) == 1
            if (!isEnPassantHappensRightAway) {
                return Pair(ChessGame.MoveResult.INVALID_MOVE, null)
            }

            capturedChessPiece = enPassantCapturedChessPiece
        }

        return if (this.player == capturedChessPiece.player) {
            Pair(ChessGame.MoveResult.INVALID_INPUT, null)
        } else {
            Pair(ChessGame.MoveResult.VALID, capturedChessPiece)
        }
    }

    private fun checkPawnOnlyMoveForward(destinationCell: Cell): Pair<Boolean, Int> {
        val yOffset = (destinationCell.y - this.occupiedCell.y) * player.moveDirection
        return Pair(yOffset > 0, yOffset)
    }

    override fun detectMoveType(destinationCell: Cell): MoveType {
        val xOffset = destinationCell.x - this.occupiedCell.x

        return if (xOffset == 0) {
            MoveType.MOVE
        } else {
            MoveType.CAPTURE
        }
    }
}
