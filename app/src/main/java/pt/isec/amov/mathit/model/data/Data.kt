package pt.isec.amov.mathit.model.data

import pt.isec.amov.mathit.model.data.levels.Levels

class Data {
    var playerName: String? = "Player" + (1..99999).shuffled().last()
        set(value) {
            if(!value.isNullOrEmpty())
                field = value
        }

    var level : Levels? = null

    fun getNextLevel() : Levels? {
        return level?.getNextLevel(level)
    }
}