package pt.isec.amov.mathit.model.data

class Data {
    var playerName: String? = "Player" + (1..99999).shuffled().last()
        set(value) {
            if(!value.isNullOrEmpty())
                field = value
        }
}