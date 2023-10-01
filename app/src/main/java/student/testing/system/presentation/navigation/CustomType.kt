package student.testing.system.presentation.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import java.lang.reflect.Type
import kotlin.reflect.KClass

class CustomType<T : Parcelable>(private val kclass: KClass<T>) :
    NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? = bundle.getParcelable(key)

    override fun parseValue(value: String): T {
        val type: Type = kclass.javaObjectType
        return Gson().fromJson(value, type)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }
}