package student.testing.system.annotations

@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    "\"Success\" should never be the whole state of the screen.\n" +
            "This is either a successful operation completion after which " +
            "the ViewModel state can be reset if necessary, or a successful data download, " +
            "which MUST BE SAVED in ContentState."
)
@MustBeDocumented
annotation class NotScreenState

/**
 * It is the domain-level state. It can be used in the presentation layer, but it cannot be formed there.
 * <p>
 *
 * As a rule, it is formed in UseCase.
 * Or it is LoadableData or OperationState, which are identical in structure, but different in semantics.
 */
@MustBeDocumented
annotation class FunctionalityState

/**
 * It is the presentation-level state. It must be formed and must be used only in presentation layer.
 */
@MustBeDocumented
annotation class ContentState
