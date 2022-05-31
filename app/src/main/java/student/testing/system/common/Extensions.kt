package student.testing.system.common

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import student.testing.system.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun View.showIf(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun Fragment.showSnackbar(message: String, duration:Int = Snackbar.LENGTH_SHORT){
    requireActivity().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun Fragment.showSnackbar(@StringRes message: Int, duration:Int = Snackbar.LENGTH_SHORT){
    requireActivity().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun DialogFragment.showSnackbar(message: String, duration:Int = Snackbar.LENGTH_SHORT){
    requireDialog().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun Fragment.confirmAction(@StringRes title: Int, listener: DialogInterface.OnClickListener) {
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle(title)
    builder.setPositiveButton(R.string.ok, listener)
    builder.setNegativeButton(R.string.cancel) { dialog, which ->
        dialog.cancel()
    }
    builder.show()
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

fun <T> Flow<T>.launchWhenStartedCollect(viewLifecycleOwner: LifecycleOwner) {
    viewLifecycleOwner.lifecycleScope.launch {
        this@launchWhenStartedCollect.collect()
    }
}