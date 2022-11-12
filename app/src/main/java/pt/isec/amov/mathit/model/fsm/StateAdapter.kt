package pt.isec.amov.mathit.model.fsm

import pt.isec.amov.mathit.model.data.Data

abstract class StateAdapter(
    protected var context : StatesContext,
    protected var data : Data
    ) : IState {

    protected fun setState(state: States){
        context.setState(state.createState(context, data))
    }

    override fun getState(): States? {
        return null
    }
}