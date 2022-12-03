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

        return moveResult
    }

    override fun move(newPosition: Cell): ChessGame.MoveResult {
        val checkResult = checkValidMove(newPosition)
        if (checkResult != ChessGame.MoveResult.VALID) {
            return checkResult
        }

        setPosition(newPosition)
        if (firstMove) {
            firstMove = false
        }

        return ChessGame.MoveResult.SUCCESSFUL
    }

    override fun capture(newPosition: Cell): ChessGame.MoveResult {
        val (checkResult, capturedChessPiece) = checkValidCapture(newPosition)
        if (checkResult != ChessGame.MoveResult.VALID) {
            return checkResult
        }

        capturedChessPiece?.capture()
        setPosition(newPosition)

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
        val invalidMove = Pair(ChessGame.MoveResult.INVALID_MOVE, null)

        val (pawnOnlyMoveForward, yOffset) = checkPawnOnlyMoveForward(destinationCell)
        if (!pawnOnlyMoveForward) {
            return invalidMove
        }

        val isMoveOneStepForward = (yOffset == 1)
        val isMoveDiagonal = abs(this.occupiedCell!!.x - destinationCell.x) == 1
        val isValidPawnMoveForCapturing = isMoveOneStepForward && isMoveDiagonal
        if (!isValidPawnMoveForCapturing) {
            return invalidMove
        }

        var capturedChessPiece = destinationCell.chessPieceWeakReference.get()
            ?: return checkValidEnPasssantCapture(destinationCell)

        if (this.player == capturedChessPiece.player) {
            return invalidMove
        }

        return Pair(ChessGame.MoveResult.VALID, capturedChessPiece)
    }

    private fun checkValidEnPasssantCapture(destinationCell: Cell): Pair<ChessGame.MoveResult, ChessPiece?> {
        val invalidMove = Pair(ChessGame.MoveResult.INVALID_MOVE, null)

        val backPosition = Board.INSTANCE.getBackCell(destinationCell, player.moveDirection)
        val enPassantCapturedChessPiece = backPosition?.chessPieceWeakReference?.get()
            ?: return invalidMove

        if (enPassantCapturedChessPiece.player == player) {
            return invalidMove
        }

        val isEnPassantHappensRightAway = player.moveCount == moveCount
        if (!isEnPassantHappensRightAway) {
            return invalidMove
        }

        return Pair(ChessGame.MoveResult.VALID, enPassantCapturedChessPiece)
    }

    private fun checkPawnOnlyMoveForward(destinationCell: Cell): Pair<Boolean, Int> {
        val yOffset = (destinationCell.y - this.occupiedCell!!.y) * player.moveDirection
        return Pair(yOffset > 0, yOffset)
    }

    override fun detectMoveType(destinationCell: Cell): MoveType {
        val xOffset = destinationCell.x - this.occupiedCell!!.x

        return if (xOffset == 0) {
            MoveType.MOVE
        } else {
            MoveType.CAPTURE
        }
    }

    override fun havePossibleMoves(): Boolean {
        val possibleMoves = mutableListOf<String>()
        if (isCaptured) {
            return false
        }

        val forwardCell = occupiedCell!!.getNextCellByOffset(xOffset = 0, yOffset = player.moveDirection)
        if (
            forwardCell != null
            && checkValidMove(forwardCell) == ChessGame.MoveResult.VALID
        ) {
            return true
        }

        val capturePositionLeft = occupiedCell!!.getNextCellByOffset(
            xOffset = -1,
            yOffset = player.moveDirection
        )

        if (capturePositionLeft != null) {
            val (checkResult, _) = checkValidCapture(capturePositionLeft)
            if (checkResult == ChessGame.MoveResult.VALID) {
                return true
            }
        }

        val capturePositionRight = occupiedCell!!.getNextCellByOffset(
            xOffset = 1,
            yOffset = player.moveDirection
        )

        if (capturePositionRight != null) {
            val (checkResult, _) = checkValidCapture(capturePositionRight)
            if (checkResult == ChessGame.MoveResult.VALID) {
                return true
            }
        }

        return false
    }
}
