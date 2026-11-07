package tj.vudu.app

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var toggle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildScreen()
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 10)
        } else {
            VuduNotification.show(this)
        }
        render()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            VuduNotification.show(this)
        }
    }

    private fun buildScreen() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(0xFFF5F7F6.toInt())
        }
        val title = TextView(this).apply {
            text = "Вуду"
            textSize = 30f
            setTextColor(0xFF17231D.toInt())
            gravity = Gravity.CENTER
        }
        status = TextView(this).apply {
            textSize = 25f
            gravity = Gravity.CENTER
            setPadding(0, 48, 0, 36)
        }
        toggle = Button(this).apply {
            textSize = 17f
            isAllCaps = false
            setOnClickListener {
                VuduState.toggle(this@MainActivity)
                VuduNotification.show(this@MainActivity)
                render()
            }
        }
        val hint = TextView(this).apply {
            text = "Отметь состояние после омовения.\nИзменить его можно также из уведомления."
            textSize = 15f
            gravity = Gravity.CENTER
            setTextColor(0xFF65736B.toInt())
            setPadding(0, 28, 0, 0)
        }
        root.addView(title, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(status, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(toggle, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(hint, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setContentView(root)
    }

    private fun render() {
        val hasWudu = VuduState.hasWudu(this)
        status.text = if (hasWudu) "✓ Омовение есть" else "○ Омовения нет"
        status.setTextColor(if (hasWudu) 0xFF16865B.toInt() else 0xFFB34A42.toInt())
        toggle.text = if (hasWudu) "Переключить: омовения нет" else "Переключить: омовение есть"
    }
}
