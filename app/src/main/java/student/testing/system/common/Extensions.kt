package student.testing.system.common

import android.view.View

fun View.showIf(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}