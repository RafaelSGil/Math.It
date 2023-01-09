package pt.isec.amov.mathit.model.fsm

import android.content.Context
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.levels.Levels

abstract class StateAdapter(
    protected var context : StatesContext,
    protected var data : Data
    ) : IState {

    protected fun setState(state: States){
        context.setState(state.createState(context, data))
    }

    override fun getState(): States? {
        return null
    }

    override fun addPoints(points: Int) {
        return
    }

    override fun getPoints(): Int {
        return 0
    }

    override fun reset() {
        return
    }

    override fun getLevel(): Levels? {
        return null
    }

    override fun goGameOverState(context : Context, manager: ModelManager) {
        return
    }

    override fun goMultiPlayerState(context : Context, manager: ModelManager, mode: String) {
        return
    }

    override fun goMultiPlayerTopState(context : Context, manager: ModelManager) {
        return
    }

    override fun goMultiPointsTopState(context : Context, manager: ModelManager) {
        return
    }

    override fun goMultiTimeTopState(context : Context, manager: ModelManager) {
        return
    }

    override fun goNextLevelState(context : Context, manager: ModelManager) {
        return
    }

    override fun goNextLevelState(context: Context, manager: ModelManager, time: Int) {
        return
    }

    override fun goPauseState(context : Context, manager: ModelManager, time: Int) {
        return
    }

    override fun goProfileState(context : Context, manager: ModelManager) {
        return
    }

    override fun goSetProfileState(context : Context, manager: ModelManager) {
        return
    }

    override fun goSinglePlayerState(context : Context, manager: ModelManager) {
        return
    }

    override fun goSinglePlayerState(
        context: Context,
        manager: ModelManager,
        board: String
    ) {
        return
    }

    override fun goSinglePlayerTopState(context : Context, manager: ModelManager) {
        return
    }

    override fun goStartState(context : Context, manager: ModelManager) {
        return
    }

    override fun goTop5State(context : Context, manager: ModelManager) {
        return
    }

    override fun goWaitForLobbyState(context : Context, manager: ModelManager) {
        return
    }

    override fun goWaitMultiStartState(context : Context, manager: ModelManager) {
        return
    }

    override fun changeLocalPlayerName(name: String?) {
       return
    }

    override fun changeLocalPlayerProfilePic(imagePath: String?) {
        return
    }
}