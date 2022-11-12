package pt.isec.amov.mathit.model.fsm

import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.fsm.implementation.*

enum class States {
    START,
    PROFILE, SET_PROFILE,
    TOP_5, SINGLE_PLAYER_TOP, MULTI_PLAYER_TOP, MULTI_POINTS_TOP, MULTI_TIME_TOP,
    SINGLE_PLAYER, PAUSE,
    NEXT_LEVEL, GAME_OVER,
    WAIT_MULTI_START, MULTI_PLAYER, WAIT_FOR_LOBBY;

    //State factory
    fun createState(context : StatesContext, data: Data) : IState{
        return when(this){
            START -> StartState(context, data)
            PROFILE -> ProfileState(context, data)
            SET_PROFILE -> SetProfileState(context, data)
            TOP_5 -> Top5State(context, data)
            SINGLE_PLAYER_TOP -> SinglePlayerTopState(context, data)
            MULTI_PLAYER_TOP -> MultiPlayerTopState(context, data)
            MULTI_POINTS_TOP -> MultiPointsTopState(context, data)
            MULTI_TIME_TOP -> MultiTimeTopState(context, data)
            SINGLE_PLAYER -> SinglePlayerState(context, data)
            PAUSE -> PauseState(context, data)
            NEXT_LEVEL -> NextLevelState(context, data)
            GAME_OVER -> GameOverState(context, data)
            WAIT_MULTI_START -> WaitMultiStartState(context, data)
            MULTI_PLAYER -> MultiPlayerState(context, data)
            WAIT_FOR_LOBBY -> WaitForLobbyState(context, data)
        }
    }
}