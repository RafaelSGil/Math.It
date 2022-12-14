package pt.isec.amov.mathit.model.fsm.implementation

import pt.isec.amov.mathit.model.data.Data
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

}