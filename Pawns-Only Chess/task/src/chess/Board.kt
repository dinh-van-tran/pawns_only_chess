package chess

enum class Board {
    INSTANCE;

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
            pawnList.add(Pawn(player = Player.WHITE, occupiedCell = rows[initYPositionForWhite][initXPositionForWhite]))
        }
    }

    fun move(currentPosition: String, destinationPosition: String, player: Player): ChessGame.MoveResult {
        val (moveResult, currentPiece, destinationCell) = checkValidMove(currentPosition, destinationPosition, player)
        if (moveResult != ChessGame.MoveResult.VALID) {
            return moveResult
        }

        return currentPiece!!.moveOrCapture(destinationCell!!)
    }

    private fun checkValidMove(currentPosition: String, destinationPosition: String, playerTurn: Player): Triple<ChessGame.MoveResult, ChessPiece?, Cell?> {
        val inValidInput = Triple(ChessGame.MoveResult.INVALID_INPUT, null, null)

        val currentCell = getCell(currentPosition)
        val destinationCell = getCell(destinationPosition)
        if (currentCell == null || destinationCell == null) {
            return inValidInput
        }

        val noPiece = Triple(ChessGame.MoveResult.NO_PIECE, null, null)
        val currentPiece = currentCell.chessPieceWeakReference.get()
            ?: return noPiece
        if (currentPiece.player != playerTurn) {
            return noPiece
        }

        if (currentPosition == destinationPosition) {
            return inValidInput
        }

        val destinationPawn = destinationCell.chessPieceWeakReference.get()
        if (destinationPawn != null && currentPiece.player == destinationPawn.player) {
            return inValidInput
        }

        return Triple(ChessGame.MoveResult.VALID, currentPiece, destinationCell)
    }

    private fun getCell(position: String): Cell {
        return rows[parseYPosition(position)][parseXPosition(position)]
    }

    fun getBackCell(position: Cell, direction: Int): Cell? {
        val backPosition = position.y - direction
        val realXPosition = position.x - 1
        val realYPosition = backPosition - 1

        if (realYPosition !in 0..7) {
            return null
        }

        return rows[realYPosition][realXPosition]
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
