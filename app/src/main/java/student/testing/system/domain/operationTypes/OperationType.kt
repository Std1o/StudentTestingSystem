package student.testing.system.domain.operationTypes

// TODO написать нормальный KDoc
/**
 * Для решение коллизий при использовании нескольких операций на одном экране,
 * на success которых ui должно реагировать по-разному
 */
sealed interface OperationType {
    data object DefaultOperation : OperationType
}