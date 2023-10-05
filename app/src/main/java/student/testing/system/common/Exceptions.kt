package student.testing.system.common

class NullSharedViewModelException : RuntimeException("SharedViewModel is null")

/**
 * 1.) Make sure that you are using RequestState or his parent in your UseCase or Repository
 * <b/>
 *
 * 2.) Call [student.testing.system.presentation.viewmodels.protect]
 * <b/>
 *
 * on your calling of [student.testing.system.presentation.viewmodels.OperationViewModel.executeOperation]
 * <b/>
 *
 * Explanation:
 * <b/>
 *
 * Kotlin compiler may do wrong auto cast of fun's generics inside coroutine if there is not at least one of this:
 * <b/>
 *
 * 1.) val/var for result of method
 * <b/>
 *
 * 2.) Any val/var after calling method
 * <b/>
 *
 * 3.) Explicit generics with fun's calling
 * <b/>
 *
 * 4.) Located inside async {}
 * <b/>
 *
 * [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
 */
class GenericsAutoCastIsWrong : RuntimeException(
    "\n1.) Make sure that you are using RequestState or his parent in your UseCase or Repository\n" +
            "2.) Call protect() extension on your calling of executeOperation()"
)