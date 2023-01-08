package pt.isec.amov.mathit.model.data.multiplayer

class Player(name: String) {
    private var _name = name
    var name: String
        get() { return _name}
        set(value) {_name = value}

    var score: Long = 0
        set(value) {
            if(value >= 0)
                field += value
        }

    var level : Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false
        if (_name != other._name) return false
        return true
    }

    override fun toString(): String {
        return "Score: $score Player: $name"
    }

    override fun hashCode(): Int {
        return _name.hashCode()
    }
}