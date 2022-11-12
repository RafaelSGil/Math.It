package pt.isec.amov.mathit.model.fsm

import pt.isec.amov.mathit.model.data.Data

class StatesContext(
    private val data: Data,
    private var state : IState
    ){

    fun setState(state: IState){
        this.state = state
    }

    fun getState() : States? {
        return state.getState()
    }
}