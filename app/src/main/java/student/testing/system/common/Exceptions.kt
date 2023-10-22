package student.testing.system.common

import kotlin.reflect.KClass
import kotlin.reflect.KType

class NullSharedViewModelException : RuntimeException("SharedViewModel is null")

class WrongFunctionReturnType(kType: KType) : RuntimeException(
    "Expected: OperationState or OperationState superclass\n" +
            "Found: $kType\n\n" +
            "Please make sure that you use sealed interface marked with annotation @OperationState"
)