package pt.isec.amov.mathit.model

import pt.isec.amov.mathit.model.fsm.IState
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class ModelManager(
    private val context: StatesContext
) {

    fun getState() : States?{
        return this.context.getState()
    }

    fun setState(state : IState){
        this.context.setState(state)
    }
}