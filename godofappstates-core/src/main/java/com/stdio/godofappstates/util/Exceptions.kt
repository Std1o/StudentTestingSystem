package com.stdio.godofappstates.util

/**
 * 1.) Make sure that you are using RequestState or his parent in your UseCase or Repository
 *
 * 2.) Call [student.testing.system.presentation.viewmodels.protect]
 *
 * on your calling of [student.testing.system.presentation.viewmodels.StatesViewModel.executeOperation]
 *
 * Explanation:
 *
 * Kotlin compiler may do wrong auto cast of fun's generics inside coroutine if there is not at least one of this:
 *
 * 1.) val/var for result of method
 *
 * 2.) Any val/var after calling method
 *
 * 3.) Explicit generics with fun's calling
 *
 * 4.) Located inside async {}
 *
 * [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
 */
class GenericsAutoCastIsWrong : RuntimeException(
    "\n1.) Make sure that you are using RequestState or his parent in your UseCase or Repository\n" +
            "2.) Call protect() extension on your calling of executeOperation()"
)