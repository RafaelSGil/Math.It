package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.GameOverActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class MultiPlayerState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.MULTI_PLAYER
    }

    override fun goWaitForLobbyState(context: Context, manager: ModelManager) {
        setState(States.WAIT_FOR_LOBBY)
    }

    override fun goGameOverState(context: Context, manager: ModelManager) {
        setState(States.GAME_OVER)
        ContextCompat.startActivity(context,GameOverActivity.getNewIntent(
            context, manager, false), null)

    }

    override fun addPoints(points : Int) {
        data.multiplayerScore = points
    }

    override fun getPoints(): Int {
        return data.multiplayerScore
    }

    override fun getLevel(): Levels{
        return data.getLevel()
    }

    override fun reset() {
        data.resetScoresLevels()
    }
}