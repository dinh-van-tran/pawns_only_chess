package chess

import java.lang.ref.WeakReference

class Cell(val x: Int, val y: Int) {
    var chessPieceWeakReference: WeakReference<ChessPiece> = WeakReference(null)

    fun occupyCell(chessPiece: ChessPiece) {
        chessPieceWeakReference = WeakReference(chessPiece)
    }

    fun releaseCell() {
        chessPieceWeakReference.clear()
    }

    fun getPosition(): String {
        val xInString = Board.INSTANCE.mapXIntToString(x)
        return "$xInString$y"
    }

    fun getNextCellByOffset(xOffset: Int, yOffset: Int): Cell? {
        val nextX = x + xOffset
        val nextY = y + yOffset

        return Board.INSTANCE.getCell(x = nextX, y = nextY)
    }

    override fun toString(): String {
        val side: String = chessPieceWeakReference.get()?.player?.printableSideCharacter ?: " "

        return when (x) {
            8 -> "| $side |"
            else -> "| $side "
        }
    }
}
