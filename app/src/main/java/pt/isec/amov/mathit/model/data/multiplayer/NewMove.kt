package pt.isec.amov.mathit.model.data.multiplayer

data class NewMove(var currentBoard : Int, var tilesSelected : ArrayList<String>,
                    var username : String, var points : Int, var level : Int) {
}