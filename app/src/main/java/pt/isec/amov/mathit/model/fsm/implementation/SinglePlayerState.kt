package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.*
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class SinglePlayerState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.SINGLE_PLAYER
    }

    override fun goGameOverState(context: Context, manager: ModelManager) {
        setState(States.GAME_OVER)
        ContextCompat.startActivity(
            context,
            GameOverActivity.getNewIntent(context, manager, true),
            null
        )
    }

    override fun goNextLevelState(context: Context, manager: ModelManager) {
        setState(States.NEXT_LEVEL)
        ContextCompat.startActivity(
            context,
            NextLevelActivity.getNewIntent(context, manager),
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

    override fun reset() {
        data.resetScoresLevels()
    }

    override fun goStartState(context: Context, manager: ModelManager) {
        setState(States.START)
    }
}