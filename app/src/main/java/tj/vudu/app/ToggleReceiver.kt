package tj.vudu.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ToggleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == VuduNotification.ACTION_TOGGLE) {
            VuduState.toggle(context)
            VuduNotification.show(context)
        }
    }
}
