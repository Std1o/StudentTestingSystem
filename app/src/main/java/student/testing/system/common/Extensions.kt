package student.testing.system.common

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.OperationState
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun Fragment.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    requireActivity().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun Fragment.showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    requireActivity().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun DialogFragment.showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    requireDialog().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun DialogFragment.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    requireDialog().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun Fragment.confirmAction(@StringRes message: Int, listener: DialogInterface.OnClickListener) {
    MaterialAlertDialogBuilder(requireContext())
        .setMessage(message)
        .setNegativeButton(R.string.cancel) { dialog, which ->
            dialog.cancel()
        }
        .setPositiveButton(R.string.ok, listener)
        .show()
}

@SuppressLint("SimpleDateFormat")
fun Date.formatToString(pattern: String): String? = SimpleDateFormat(pattern).format(this)

/** Fragment binding delegate, may be used since onViewCreated up to onDestroyView (inclusive) */
fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: factory(requireView()).also {
                // if binding is accessed after Lifecycle is DESTROYED, create new instance, but don't cache it
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    viewLifecycleOwner.lifecycle.addObserver(this)
                    binding = it
                }
            }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }

fun <T> Flow<T>.launchWhenStartedCollect(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launch {
        this@launchWhenStartedCollect.collect()
    }
}

@OptIn(NotScreenState::class)
fun <T> StateFlow<OperationState<T>>.subscribeInUI(
    fragment: Fragment,
    progressBar: ProgressBar,
    listener: (T) -> Unit
) {
    this@subscribeInUI.onEach {
        progressBar.isVisible = it is OperationState.Loading
        if (it is OperationState.Success) {
            listener.invoke(it.data)
        } else if (it is OperationState.Error) {
            fragment.showSnackbar(it.exception)
        }
    }.launchWhenStartedCollect(fragment.lifecycleScope)
}

@OptIn(NotScreenState::class)
fun <T> StateFlow<LoadableData<T>>.subscribeOnLoadableInUI(
    fragment: Fragment,
    progressBar: ProgressBar,
    listener: (T) -> Unit
) {
    this@subscribeOnLoadableInUI.onEach {
        progressBar.isVisible = it is LoadableData.Loading
        if (it is LoadableData.Success) {
            listener.invoke(it.data)
        } else if (it is LoadableData.Error) {
            fragment.showSnackbar(it.exception)
        }
    }.launchWhenStartedCollect(fragment.lifecycleScope)
}

@OptIn(NotScreenState::class)
fun <T> StateFlow<OperationState<T>>.subscribeInUI(
    dialogFragment: DialogFragment,
    progressBar: ProgressBar,
    listener: (T) -> Unit
) {
    this@subscribeInUI.onEach {
        progressBar.isVisible = it is OperationState.Loading
        if (it is OperationState.Success) {
            listener.invoke(it.data)
        } else if (it is OperationState.Error) {
            dialogFragment.showSnackbar(it.exception)
        }
    }.launchWhenStartedCollect(dialogFragment.lifecycleScope)
}

fun Any?.trimString(): String = this@trimString.toString().trim()

/**
 * Use if you sure that request when success returns 204
 */
@OptIn(NotScreenState::class)
fun <E> ViewModel.makeOperation(
    requestResult: OperationState<Void>,
    successData: E,
): OperationState<E> {
    return when (requestResult) {
        is OperationState.Empty -> OperationState.Success(successData, requestResult.operationType)
        is OperationState.Error -> OperationState.Error(requestResult.exception)
        is OperationState.Loading -> OperationState.Loading()
        OperationState.NoState -> OperationState.NoState
        is OperationState.Success -> OperationState.Success(successData)
    }
}

fun EditText.isNotEmpty(): Boolean {
    val editText = this@isNotEmpty
    val context = editText.context
    return if (editText.text.isEmpty()) {
        editText.error = context.getString(R.string.error_empty_field)
        false
    } else {
        editText.error = null
        true
    }
}

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