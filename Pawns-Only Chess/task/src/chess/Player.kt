package chess

enum class Player {
    BLACK("B", "black", "black", -1, 0) {
        override fun nextPlayer(): Player {
            return WHITE
        }
    },

    WHITE("W", "white", "white", 1, 0) { // end of the constants
        override fun nextPlayer(): Player {
            return BLACK
        }
    };

    var printableSideCharacter: String = ""
    var printableSideName: String = ""
    var printableName : String = ""
    var moveDirection: Int = 1
    var moveCount: Int = 0

    constructor()

    constructor(
        printableCharacter: String,
        printableSideName: String,
        name: String,
        moveDirection: Int,
        moveCount: Int,
    ) {
        this.printableSideCharacter = printableCharacter
        this.printableSideName = printableSideName
        this.printableName = name
        this.moveDirection = moveDirection
        this.moveCount = moveCount
    }

    abstract fun nextPlayer(): Player
}
