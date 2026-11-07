package tj.vudu.app

import android.content.Context

object VuduState {
    private const val PREFS = "vudu_state"
    private const val KEY_HAS_WUDU = "has_wudu"

    fun hasWudu(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_HAS_WUDU, false)

    fun set(context: Context, value: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_HAS_WUDU, value).apply()
    }

    fun toggle(context: Context): Boolean {
        val next = !hasWudu(context)
        set(context, next)
        return next
    }
}
