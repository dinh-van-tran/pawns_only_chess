package chess

enum class Board {
    INSTANCE;

    private val rows = mutableListOf<MutableList<Cell>>()

    init {
        initCells()
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

    fun getBackCell(position: Cell, direction: Int): Cell? {
        val backPosition = position.y - direction
        val realXPosition = position.x - 1
        val realYPosition = backPosition - 1

        if (realYPosition !in 0..7) {
            return null
        }

        return rows[realYPosition][realXPosition]
    }

    fun getCell(position: String): Cell {
        val y =position.last().toString().toInt()
        val x = mapXStingToInt(position.first().toString())
        return rows[y - 1][x - 1]
    }

    fun getCell(x: Int, y: Int): Cell? {
        if (x !in 1..8 || y !in 1..8) {
            return null
        }

        return rows[y-1][x-1]
    }

    private fun mapXStingToInt(position: String): Int {
        return when (position) {
            "a" -> 1
            "b" -> 2
            "c" -> 3
            "d" -> 4
            "e" -> 5
            "f" -> 6
            "g" -> 7
            "h" -> 8
            else -> -1
        }
    }

    fun mapXIntToString(position: Int): String {
        return when (position) {
            1 -> "a"
            2 -> "b"
            3 -> "c"
            4 -> "d"
            5 -> "e"
            6 -> "f"
            7 -> "g"
            8 -> "h"
            else -> "z"
        }
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
