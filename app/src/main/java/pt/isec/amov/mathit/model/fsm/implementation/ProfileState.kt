package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class ProfileState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data){

    override fun getState(): States {
        return States.PROFILE
    }

    override fun goStartState(context: Context, manager: ModelManager) {
        setState(States.START)
    }
}