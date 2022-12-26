package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.MainMenuActivity
import pt.isec.amov.mathit.controllers.NextLevelActivity
import pt.isec.amov.mathit.controllers.PauseActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class PauseState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.PAUSE
    }

    override fun goStartState(context: Context, manager: ModelManager) {
        setState(States.START)
        ContextCompat.startActivity(
            context,
            MainMenuActivity.getNewIntent(context, manager),
            null
        )
    }

    override fun goNextLevelState(context: Context, manager: ModelManager, time : Int) {
        setState(States.NEXT_LEVEL)
        ContextCompat.startActivity(
            context,
            NextLevelActivity.getNewIntent(context, manager, time),
            null
        )
    }
}