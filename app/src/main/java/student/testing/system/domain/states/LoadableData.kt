package student.testing.system.domain.states

import student.testing.system.annotations.FunctionalityState
import student.testing.system.domain.dataTypes.DataType
import student.testing.system.domain.operationTypes.OperationType

/**
 * LoadableData is used for loading UI content from any data source,
 * such as list, images, text information.
 * <p>
 *
 * Usage exmaple:
 * ```
 * data class FeedContentState(
 *     val news: LoadableData<News>,
 *     val stories: LoadableData<Stories>
 * )
 * ```
 * <p>
 *
 * Or you can parse LoadableData in ViewModel and update ContentState fields
 * <p>
 *
 * ```
 * data class FeedContentState(
 *     val news: List<News>,
 *     val newsIsLoading: Boolean,
 *     val newsError: String,
 *     val stories: List<Stories>,
 *     val storiesIsLoading: Boolean,
 *     val storiesError: String,
 * )
 * ```
 */
@FunctionalityState
sealed interface LoadableData<out R> {
    data object NoState : LoadableData<Nothing>

    data class Success<out T>(
        val data: T,
        val dataType: DataType = DataType.NotSpecified
    ) : LoadableData<T>

    /**
     * Must be converted to Success with own local value in ViewModel
     */
    data class Empty204(
        val code: Int,
        val dataType: DataType = DataType.NotSpecified
    ) : LoadableData<Nothing>

    data class Error(
        val exception: String,
        val code: Int = -1,
        val dataType: DataType = DataType.NotSpecified
    ) : LoadableData<Nothing>

    data class Loading(val operationType: OperationType = OperationType.DefaultOperation) :
        LoadableData<Nothing>
}