package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import androidx.core.content.ContextCompat.startActivity
import pt.isec.amov.mathit.controllers.*
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class StartState(
    context: StatesContext,
    data: Data
    ) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.START
    }

    override fun goSinglePlayerState(context: Context, manager : ModelManager) {
        setState(States.SINGLE_PLAYER)
        startActivity(context, SinglePlayerActivity.getNewIntent(context, manager, Levels.LEVEL1), null)
    }

    override fun goProfileState(context: Context, manager : ModelManager) {
        setState(States.PROFILE)
        startActivity(context, ProfileActivity.getNewIntent(context, manager), null)
    }

    override fun goTop5State(context: Context, manager : ModelManager) {
        setState(States.TOP_5)
        startActivity(context, Top5Activity.getNewIntent(context, manager), null)
    }

    override fun goWaitMultiStartState(context: Context, manager: ModelManager) {
        setState(States.WAIT_MULTI_START)
        startActivity(context, MultiPlayerWaitStartActivity.getNewIntent(context, manager), null)
    }
}