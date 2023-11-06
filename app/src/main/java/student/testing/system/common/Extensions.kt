package student.testing.system.common

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.formatToString(pattern: String): String? = SimpleDateFormat(pattern).format(this)

// TODO use before send some inputted data to server
fun Any?.trimString(): String = this@trimString.toString().trim()

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.viewModelScopedTo(
    navController: NavController,
    route: String
): T {
    val parentEntry = remember(this) { navController.getBackStackEntry(route) }
    return hiltViewModel(parentEntry)
}

// extension находится в том же файле где и items с аргументом count,
// даже импорт иногда не помогет
// поэтому скопировал сюда
inline fun <T> LazyListScope.iTems(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> contentType(items[index]) }
) {
    itemContent(items[it])
}