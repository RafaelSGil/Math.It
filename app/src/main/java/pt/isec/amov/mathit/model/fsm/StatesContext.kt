package pt.isec.amov.mathit.model.fsm

import android.content.Context
import android.content.SharedPreferences
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.Player
import pt.isec.amov.mathit.model.fsm.implementation.SinglePlayerState
import pt.isec.amov.mathit.model.fsm.implementation.StartState

class StatesContext(sharedPreferences: SharedPreferences){
    private val data: Data = Data(sharedPreferences)
    private var state : IState = StartState(this, data)

    fun setState(state: IState){
        this.state = state
    }

    fun getState() : States? {
        return state.getState()
    }

    fun goGameOverState(context : Context, manager: ModelManager) {
        this.state.goGameOverState(context, manager)
    }

    fun goMultiPlayerState(context : Context, manager: ModelManager) {
        this.state.goMultiPlayerState(context, manager)
    }

    fun goMultiPlayerTopState(context : Context, manager: ModelManager) {
        this.state.goMultiPlayerTopState(context, manager)
    }

    fun goMultiPointsTopState(context : Context, manager: ModelManager) {
        this.state.goMultiPointsTopState(context, manager)
    }

    fun goMultiTimeTopState(context : Context, manager: ModelManager) {
        this.state.goMultiTimeTopState(context, manager)
    }

    fun goNextLevelState(context : Context, manager: ModelManager) {
        this.state.goNextLevelState(context, manager)
    }

    fun goPauseState(context : Context, manager: ModelManager) {
        this.state.goPauseState(context, manager)
    }

    fun goProfileState(context : Context, manager: ModelManager) {
        this.state.goProfileState(context, manager)
    }

    fun goSetProfileState(context : Context, manager: ModelManager) {
        this.state.goSetProfileState(context, manager)
    }

    fun goSinglePlayerState(context : Context, manager: ModelManager) {
        this.state.goSinglePlayerState(context, manager)
    }

    fun goSinglePlayerTopState(context : Context, manager: ModelManager) {
        this.state.goSinglePlayerTopState(context, manager)
    }

    fun goStartState(context : Context, manager: ModelManager) {
        this.state.goStartState(context, manager)
    }

    fun goTop5State(context : Context, manager: ModelManager) {
        this.state.goTop5State(context, manager)
    }

    fun goWaitForLobbyState(context : Context, manager: ModelManager) {
        this.state.goWaitForLobbyState(context, manager)
    }

    fun goWaitMultiStartState(context : Context, manager: ModelManager) {
        this.state.goWaitMultiStartState(context, manager)
    }

    // DATA
    fun getLocalPlayerName(): String? {
        return data.playerName
    }

    fun getLocalPlayerProfilePic(): String? {
        return data.profilePicImagePath
    }

    fun changeLocalPlayerName(name: String?) {
        state.changeLocalPlayerName(name)
    }

    fun changeLocalPlayerProfilePic(imagePath: String?) {
        state.changeLocalPlayerProfilePic(imagePath)
    }

}