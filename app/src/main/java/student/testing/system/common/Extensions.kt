package student.testing.system.common

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.RequestState
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun View.showIf(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

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
fun String.toDate(pattern: String): Date? = SimpleDateFormat(pattern).parse(this)

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
fun <T> StateFlow<RequestState<T>>.subscribeInUI(
    fragment: Fragment,
    progressBar: ProgressBar,
    listener: (T) -> Unit
) {
    this@subscribeInUI.onEach {
        progressBar.showIf(it is RequestState.Loading)
        if (it is RequestState.Success) {
            listener.invoke(it.data)
        } else if (it is RequestState.ValidationError) {
            fragment.showSnackbar(it.messageResId)
        } else if (it is RequestState.Error) {
            fragment.showSnackbar(it.exception)
        }
    }.launchWhenStartedCollect(fragment.lifecycleScope)
}

@OptIn(NotScreenState::class)
fun <T> StateFlow<RequestState<T>>.subscribeInUI(
    dialogFragment: DialogFragment,
    progressBar: ProgressBar,
    listener: (T) -> Unit
) {
    this@subscribeInUI.onEach {
        progressBar.showIf(it is RequestState.Loading)
        if (it is RequestState.Success) {
            listener.invoke(it.data)
        } else if (it is RequestState.ValidationError) {
            dialogFragment.showSnackbar(it.messageResId)
        } else if (it is RequestState.Error) {
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
    requestResult: RequestState<Void>,
    successData: E,
): RequestState<E> {
    return when (requestResult) {
        is RequestState.Empty -> RequestState.Success(successData, requestResult.operationType)
        is RequestState.Error -> RequestState.Error(requestResult.exception)
        RequestState.Loading -> RequestState.Loading
        RequestState.NoState -> RequestState.NoState
        is RequestState.Success -> RequestState.Success(successData)
        is RequestState.ValidationError -> RequestState.ValidationError(requestResult.messageResId)
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

// State - LoadableData
@OptIn(NotScreenState::class)
suspend fun <State, T> CoroutineScope.launchRequest(
    call: suspend () -> State,
    onSuccess: (T) -> Unit = {},
    // можно просто всегда вызываеть в ViewModel contentState.someList.value = RequestState.Loading
    // но чтобы не забывать это сделать, лучше обязать
    onLoading: (LoadableData<T>) -> Unit
): State {
    var requestResult: State
    val request = async {
        onLoading.invoke(RequestState.Loading)
        requestResult = call()
        if (requestResult is RequestState.Success<*>) onSuccess.invoke((requestResult as RequestState.Success<T>).data)
        requestResult
    }
    return request.await()
}