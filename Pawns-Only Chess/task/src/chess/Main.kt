package chess

fun main() {
    val board = Board()

    println("Pawns-Only Chess")
    println("First Player's name:")
    val firstPlayerName = readln()
    println("Second Player's name:")
    val secondPlayerName = readln()

    board.print()
    println()

    var playerTurn = firstPlayerName
    while (true) {
        println("$playerTurn's turn:")
        val command = readln()
        if (command == "exit") {
            println("Bye!")
            break
        }

        val moveResult = board.move(command)
        when (moveResult) {
            Board.MoveResult.INVALID_INPUT -> {
                println("Invalid Input")
                continue
            }
            else -> {

            }
        }

        playerTurn = if (playerTurn == firstPlayerName) {
            secondPlayerName
        } else {
            firstPlayerName
        }

    }
}