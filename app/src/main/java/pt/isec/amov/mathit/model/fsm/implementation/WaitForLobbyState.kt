package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.MultiPlayerActivity
import pt.isec.amov.mathit.controllers.SinglePlayerActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class WaitForLobbyState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.WAIT_FOR_LOBBY
    }

    override fun goMultiPlayerState(context: Context, manager: ModelManager, mode: String) {
        setState(States.MULTI_PLAYER)
        ContextCompat.startActivity(
            context,
            MultiPlayerActivity.getNewIntent(context, manager, mode),
            null
        )
    }

    override fun addPoints(points: Int) {
        data.singleplayerScore = points
    }

    override fun getLevel(): Levels? {
        return data.getLevel()
    }

    override fun getPoints(): Int {
        return data.singleplayerScore
    }


}