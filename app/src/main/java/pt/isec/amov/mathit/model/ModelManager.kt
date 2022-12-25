package pt.isec.amov.mathit.model

import android.content.Context
import android.content.SharedPreferences
import android.widget.ListView
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class ModelManager(sharedPreferences: SharedPreferences) : java.io.Serializable{
    private var context : StatesContext = StatesContext(sharedPreferences)
    private var previousStates: States? = null

    fun getState() : States?{
        return this.context.getState()
    }

    fun getData() : Data {
        return context.getData()
    }

    fun goGameOverState(context: Context, model: ModelManager) {
        this.context.goGameOverState(context, model)
    }

    fun addPoints(points : Int){
        this.context.addPoints(points)
    }

    fun getPoints() : Int{
        return context.getPoints()
    }

    fun getLevel() : Levels?{
        return context.getLevel()
    }

    fun reset(){
        return context.reset()
    }

    fun redirectNextLevel(context: Context, model: ModelManager){
        when(previousStates){
            States.SINGLE_PLAYER -> goSinglePlayerState(context, model)
            else -> goMultiPlayerState(context, model)
        }
    }

    fun goMultiPlayerState(context: Context, model: ModelManager) {
        this.context.goMultiPlayerState(context, model)
    }

    fun goMultiPlayerTopState(context: Context, model: ModelManager) {
        this.context.goMultiPlayerTopState(context, model)
    }

    fun goMultiPointsTopState(context: Context, model: ModelManager) {
        this.context.goMultiPointsTopState(context, model)
    }

    fun goMultiTimeTopState(context: Context, model: ModelManager) {
        this.context.goMultiTimeTopState(context, model)
    }

    fun goNextLevelState(context: Context, model: ModelManager) {
        previousStates = getState()
        this.context.goNextLevelState(context, model)
    }

    fun goPauseState(context: Context, model: ModelManager) {
        this.context.goPauseState(context, model)
    }

    fun goProfileState(context: Context, model: ModelManager) {
        this.context.goProfileState(context, model)
    }

    fun goSetProfileState(context: Context, model: ModelManager) {
        this.context.goSetProfileState(context, model)
    }

    fun goSinglePlayerState(context: Context, model: ModelManager) {
        this.context.goSinglePlayerState(context, model)
    }

    fun goSinglePlayerTopState(context: Context, model: ModelManager) {
        this.context.goSinglePlayerTopState(context, model)
    }

    fun goStartState(context: Context, model: ModelManager) {
        this.context.goStartState(context, model)
    }

    fun goTop5State(context: Context, model: ModelManager) {
        this.context.goTop5State(context, model)
    }

    fun goWaitForLobbyState(context: Context, model: ModelManager) {
        this.context.goWaitForLobbyState(context, model)
    }

    fun goWaitMultiStartState(context: Context, model: ModelManager) {
        this.context.goWaitMultiStartState(context, model)
    }

    //Data

    fun getLocalPlayerName(): String? {
        return context.getLocalPlayerName()
    }

    fun getLocalPlayerProfilePic(): String? {
        return context.getLocalPlayerProfilePic()
    }

    fun changeLocalPlayerName(name: String?) {
        return context.changeLocalPlayerName(name)
    }

    fun changeLocalPlayerProfilePic(imagePath: String?) {
        return context.changeLocalPlayerProfilePic(imagePath)
    }

    fun startServer(applicationContext: Context) {
        ConnectionManager.startServer(applicationContext, context.getLocalPlayerName()!!)
    }

    fun startServerListener(listView: ListView) {
        ConnectionManager.startServerListener(listView)
    }

    fun closeServerListener() {
        ConnectionManager.closeServerListener()
    }

    fun closeServer() {
        ConnectionManager.closeServer()
    }

    fun startClient(index: Int) {
        ConnectionManager.startClient(index)
    }

    fun isHost(): Boolean {
        return ConnectionManager.isHost()
    }
}