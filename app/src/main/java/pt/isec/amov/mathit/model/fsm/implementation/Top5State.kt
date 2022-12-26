package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.MainMenuActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class Top5State(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.TOP_5
    }

    override fun goStartState(context: Context, manager: ModelManager) {
        setState(States.START)
        Log.i("DEBUG-AMOV", "goStartState: ")
        ContextCompat.startActivity(
            context,
            MainMenuActivity.getNewIntent(context, manager),
            null
        )
    }
}