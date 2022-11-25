package pt.isec.amov.mathit.model.data

class Player(private var name: String) {
    var score: Long = 0
        set(value) {
            if(value >= 0)
                field = value
        }

    override fun toString(): String {
        return "Score: $score Player: $name"
    }
}