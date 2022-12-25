package pt.isec.amov.mathit.model.data

class Player(name: String) {
    private var _name = name
    var name: String
        get() { return _name}
        set(value) {_name = value}

    var score: Long = 0
        set(value) {
            if(value >= 0)
                field = value
        }

    override fun toString(): String {
        return "Score: $score Player: $name"
    }
}