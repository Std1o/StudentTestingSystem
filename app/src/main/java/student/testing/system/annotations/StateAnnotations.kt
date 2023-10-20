package student.testing.system.annotations

/**
 * It is the domain-level state. It can be used in the presentation layer, but it cannot be formed there.
 * <p>
 *
 * As a rule, it is formed in UseCase.
 * Or it is LoadableData or OperationState, which are identical in structure, but different in semantics.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE_PARAMETER)
@MustBeDocumented
annotation class FunctionalityState

/**
 * It is the presentation-level state. It must be formed and must be used only in presentation layer.
 */
@MustBeDocumented
annotation class ContentState

// TODO написать норм KDoc
/**
 * ScreenSession - состояние UI компонентов, которые вызвано исключительно действиями пользователя
 * и не имеет прямого отношения к слоям отличным от presentation.
 * Оно формируется на экране и хранится во ViewModel
 *
 * Пример: инпуты, чекбоксы, показ диалогов
 *
 * Отличие от ContentState: ContentState формируется во ViewModel из данных, которые,
 * как правило приходят с других слоев. Он точно не формируется на экране
 */
@MustBeDocumented
annotation class ScreenSession

// TODO написать норм Kdoc
/**
 * Для стейтов из юзккейса, помечается если при этом стейте должен крутиться лоадер.
 */
@MustBeDocumented
annotation class IntermediateState // TODO мб поменять на @Loading
