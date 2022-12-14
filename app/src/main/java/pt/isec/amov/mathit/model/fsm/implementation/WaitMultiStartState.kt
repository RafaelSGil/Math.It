package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class WaitMultiStartState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.WAIT_MULTI_START
    }

    override fun goWaitForLobbyState(context: Context, manager: ModelManager) {
        setState(States.WAIT_FOR_LOBBY)
    }

    override fun goStartState(context: Context, manager: ModelManager) {
        setState(States.START)
    }

}