package pt.isec.amov.mathit.model.fsm

import android.content.Context
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels

interface IState {
    fun getState() : States?
    fun goSinglePlayerState(context : Context, manager: ModelManager)
    fun goGameOverState(context : Context, manager: ModelManager)
    fun goMultiPlayerState(context : Context, manager: ModelManager, mode: String)
    fun goMultiPlayerTopState(context : Context, manager: ModelManager)
    fun goMultiPointsTopState(context : Context, manager: ModelManager)
    fun goMultiTimeTopState(context : Context, manager: ModelManager)
    fun goNextLevelState(context : Context, manager: ModelManager)
    fun goNextLevelState(context : Context, manager: ModelManager, time : Int)
    fun goPauseState(context : Context, manager: ModelManager, time : Int)
    fun goProfileState(context : Context, manager: ModelManager)
    fun goSetProfileState(context : Context, manager: ModelManager)
    fun goSinglePlayerTopState(context : Context, manager: ModelManager)
    fun goStartState(context : Context, manager: ModelManager)
    fun goTop5State(context : Context, manager: ModelManager)
    fun goWaitForLobbyState(context : Context, manager: ModelManager)
    fun goWaitMultiStartState(context : Context, manager: ModelManager)
    fun changeLocalPlayerName(name: String?)
    fun changeLocalPlayerProfilePic(imagePath: String?)
    fun addPoints(points : Int)
    fun getPoints() : Int
    fun getLevel() : Levels?
    fun reset()
}