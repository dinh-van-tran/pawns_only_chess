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

    override fun toString(): String {
        val side: String = chessPieceWeakReference.get()?.player?.printableSideCharacter ?: " "

        return when (x) {
            8 -> "| $side |"
            else -> "| $side "
        }
    }
}
