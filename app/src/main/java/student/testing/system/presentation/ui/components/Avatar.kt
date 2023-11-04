package student.testing.system.presentation.ui.components

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun Avatar(name: String) {
    val activity = (LocalContext.current as? Activity)
    val prefs = activity?.getSharedPreferences("PreferencesName", MODE_PRIVATE)
    val text = name.first().toString().uppercase()
    var color = Color(getColor(text, prefs).toULong())
    if (color.value.toLong() == 0.toLong()) {
        color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1F)
        saveColor(text, color.value.toLong(), prefs)
    }
    Text(
        modifier = Modifier
            .padding(16.dp)
            .size(22.dp)
            .drawBehind {
                drawCircle(
                    color = color,
                    radius = this.size.maxDimension,
                )
            },
        text = text,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 16.sp
    )
}

fun saveColor(key: String, value: Long, prefs: SharedPreferences?) =
    prefs?.edit()?.putLong(key, value)?.apply()

fun getColor(key: String, prefs: SharedPreferences?) = prefs?.getLong(key, 0) ?: 0.toLong()