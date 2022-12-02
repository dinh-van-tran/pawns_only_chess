package chess

class Board {
    private val pawnList = mutableListOf<Pawn>()
    private val rows = mutableListOf<MutableList<Cell>>()

    init {
        initCells()
        initPawns()
    }

    private fun initCells() {
        for (rowIndex in 1..8) {
            val row = mutableListOf<Cell>()
            for (columnIndex in 1..8) {
                row.add(Cell(x = columnIndex, y = rowIndex))
            }
            rows.add(row)
        }
    }

    private fun initPawns() {
        val initYPositionForBlack = 6
        for (initXPositionForBlack in 0..7) {
            pawnList.add(Pawn(occupiedCell = rows[initYPositionForBlack][initXPositionForBlack]))
        }

        val initYPositionForWhite = 1
        for (initXPositionForWhite in 0..7) {
            pawnList.add(Pawn(side = Chess.Side.WHITE, occupiedCell = rows[initYPositionForWhite][initXPositionForWhite]))
        }
    }

    enum class MoveResult {
        SUCCESSFUL,
        INVALID_INPUT,
        INVALID_MOVE,
    }

    fun move(moveString: String): MoveResult {
        val (currentPosition, destinationPosition) = parseMovePositions(moveString)
        if (currentPosition == null || destinationPosition == null) {
            return MoveResult.INVALID_INPUT
        }

        val (isValidMove, currentPiece, destinationCell) = checkValidMove(currentPosition, destinationPosition)
        if (!isValidMove) {
            return MoveResult.INVALID_MOVE
        }

        return if (currentPiece?.move(destinationCell!!) == true) {
            MoveResult.SUCCESSFUL
        } else {
            MoveResult.INVALID_MOVE
        }
    }

    private fun parseMovePositions(moveString: String): Pair<String?, String?> {
        val moveStringTrim = moveString.trim()
        val validMoveRegex = Regex("^[a-h][1-8][a-h][1-8]$")
        if (!validMoveRegex.matches(moveStringTrim)) {
            return Pair(null, null)
        }

        val currentPosition = moveStringTrim.substring(startIndex = 0, endIndex = 2)
        val destinationPosition = moveStringTrim.substring(startIndex = 2)

        return Pair(currentPosition, destinationPosition)
    }

    private fun getCell(position: String): Cell {
        return rows[parseYPosition(position)][parseXPosition(position)]
    }

    private fun parseXPosition(position: String): Int {
        return when (position.first()) {
            'a' -> 0
            'b' -> 1
            'c' -> 2
            'd' -> 3
            'e' -> 4
            'f' -> 5
            'g' -> 6
            'h' -> 7
            else -> -1
        }
    }

    private fun parseYPosition(position: String): Int {
        return position.last().toString().toInt() - 1
    }

    private fun checkValidMove(currentPosition: String, destinationPosition: String): Triple<Boolean, ChessPiece?, Cell?> {
        val inValidResult = Triple(false, null, null)
        if (currentPosition == destinationPosition) {
            return inValidResult
        }

        val currentCell = getCell(currentPosition)
        val destinationCell = getCell(destinationPosition)
        if (currentCell == null || destinationCell == null) {
            return inValidResult
        }
        val currentPawn = currentCell.chessPieceWeakReference.get() ?: return inValidResult

        val destinationPawn = destinationCell.chessPieceWeakReference.get()
        if (destinationPawn != null && currentPawn.side == destinationPawn.side) {
            return inValidResult
        }

        return Triple(true, currentPawn, destinationCell)
    }

    fun print() {
        for (rowIndex in 7 downTo 0) {
            printRow(rowIndex)
        }
    }

    private fun printRow(rowIndex: Int) {
        val row = rows[rowIndex]

        printBorder()

        var printString = "${rowIndex + 1} "
        for (cell in row) {
            printString += cell.toString()
        }
        println(printString)

        if (rowIndex == 0) {
            printBorder()
            println("    a   b   c   d   e   f   g   h  ")
        }
    }

    private fun printBorder() {
        println("  +---+---+---+---+---+---+---+---+")
    }
}
