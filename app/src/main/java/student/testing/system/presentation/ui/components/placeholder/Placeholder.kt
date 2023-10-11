package student.testing.system.presentation.ui.components.placeholder

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

fun Modifier.placeholder(
    visible: Boolean,
    brush: Brush
): Modifier = if (visible) this.background(brush) else this
