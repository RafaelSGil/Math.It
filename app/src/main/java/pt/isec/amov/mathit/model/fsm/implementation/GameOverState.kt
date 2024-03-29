package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.MainMenuActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class GameOverState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.GAME_OVER
    }

    override fun goStartState(context: Context, manager: ModelManager) {
        setState(States.START)
        ContextCompat.startActivity(
            context,
            MainMenuActivity.getNewIntent(context, manager),
            null
        )
    }

    override fun getLevel(): Levels? {
        return data.getLevel()
    }
}