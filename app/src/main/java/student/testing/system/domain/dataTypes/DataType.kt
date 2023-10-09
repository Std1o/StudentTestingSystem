package student.testing.system.domain.dataTypes

import student.testing.system.domain.operationTypes.OperationType

sealed interface DataType {
    data object NotSpecified : DataType
}