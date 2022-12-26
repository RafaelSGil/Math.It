package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.PauseActivity
import pt.isec.amov.mathit.controllers.SinglePlayerActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class NextLevelState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.NEXT_LEVEL
    }

    override fun goSinglePlayerState(context: Context, manager: ModelManager) {
        setState(States.SINGLE_PLAYER)
        ContextCompat.startActivity(
            context,
            SinglePlayerActivity.getNewIntent(context, manager, data.getLevel()),
            null
        )
    }

    override fun goMultiPlayerState(context: Context, manager: ModelManager) {
        setState(States.MULTI_PLAYER)

    }

    override fun goPauseState(context: Context, manager: ModelManager, time : Int) {
        setState(States.PAUSE)
        ContextCompat.startActivity(
            context,
            PauseActivity.getNewIntent(context, manager, time),
            null
        )
    }
}