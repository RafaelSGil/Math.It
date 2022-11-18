package pt.isec.amov.mathit.model.data

class Data {
    var playerName: String? = null
        set(value) {
            if(!value.isNullOrEmpty())
                field = value
        }
        get() = field
}