package chess

enum class Player {
    BLACK("B", "black", "black") {
        override fun nextPlayer(): Player {
            return WHITE
        }
    },

    WHITE("W", "white", "white") { // end of the constants
        override fun nextPlayer(): Player {
            return BLACK
        }
    };

    var printableSideCharacter: String = ""
    var printableSideName: String = ""
    var printableName : String = ""

    constructor()

    constructor(
        printableCharacter: String,
        printableSideName: String,
        name: String
    ) {
        this.printableSideCharacter = printableCharacter
        this.printableSideName = printableSideName
        this.printableName = name
    }

    abstract fun nextPlayer(): Player
}
