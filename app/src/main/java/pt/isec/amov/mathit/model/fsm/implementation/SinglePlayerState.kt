package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
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

    override fun goStartState(context: Context, manager: ModelManager) {
        setState(States.START)
    }

    override fun addPoints(points: Int) {
        data.singleplayerScore = points
    }

    override fun getLevel(): Levels? {
        return data.getLevel()
    }

    override fun getPoints(): Int {
        return data.score
    }
}