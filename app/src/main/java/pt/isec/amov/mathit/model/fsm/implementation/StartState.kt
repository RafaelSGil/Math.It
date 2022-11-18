package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import androidx.core.content.ContextCompat.startActivity
import pt.isec.amov.mathit.controllers.ProfileActivity
import pt.isec.amov.mathit.controllers.SinglePlayerActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
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
        startActivity(context, SinglePlayerActivity.getNewIntent(context, manager), null)
    }

    override fun goMultiPlayerState(context: Context, manager : ModelManager) {
        setState(States.MULTI_PLAYER)
    }

    override fun goProfileState(context: Context, manager : ModelManager) {
        setState(States.PROFILE)
        startActivity(context, ProfileActivity.getNewIntent(context, manager), null)
    }

    override fun goTop5State(context: Context, manager : ModelManager) {
        setState(States.TOP_5)
    }
}