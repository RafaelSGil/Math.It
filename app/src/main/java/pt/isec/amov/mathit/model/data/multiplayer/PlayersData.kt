package pt.isec.amov.mathit.model.data.multiplayer

object PlayersData {
    private var players : ArrayList<Player> = ArrayList()

    fun addPlayer(player : Player){
        players.add(player)
    }

    fun updatePlayer(player: Player){
        if(players.contains(player)){
            val p = players[players.indexOf(player)]
            p.level = player.level
            p.score = player.score
        }
    }

    fun removePlayer(player: Player){
        players.remove(player)
    }

    fun addAll(players : ArrayList<Player>){
        players.addAll(players)
    }

    fun contains(player: Player) : Boolean{
        return players.contains(player)
    }

    fun getPlayers() : ArrayList<Player>{
        return players
    }

    fun clear(){
        players.clear()
    }
}