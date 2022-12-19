package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import android.util.Log
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
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

    override fun goWaitMultiStartState(context: Context, manager: ModelManager) {
        setState(States.WAIT_MULTI_START)
        Log.i("DEBUG-AMOV", "goWaitMultiStartState: ")
    }
}