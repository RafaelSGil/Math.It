package pt.isec.amov.mathit.model.fsm.implementation

import android.content.Context
import androidx.core.content.ContextCompat
import pt.isec.amov.mathit.controllers.MainMenuActivity
import pt.isec.amov.mathit.controllers.PauseActivity
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Data
import pt.isec.amov.mathit.model.fsm.StateAdapter
import pt.isec.amov.mathit.model.fsm.States
import pt.isec.amov.mathit.model.fsm.StatesContext

class PauseState(
    context: StatesContext,
    data: Data
) : StateAdapter(context, data) {

    override fun getState(): States {
        return States.PAUSE
    }

    override fun goStartState(context: Context, manager: ModelManager) {
        super.goStartState(context, manager)
        ContextCompat.startActivity(
            context,
            MainMenuActivity.getNewIntent(context, manager),
            null
        )
    }
}