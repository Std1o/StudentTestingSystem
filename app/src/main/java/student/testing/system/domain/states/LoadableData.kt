package student.testing.system.domain.states

import stdio.godofappstates.annotations.FunctionalityState
import student.testing.system.domain.dataTypes.DataType
import student.testing.system.domain.operationTypes.OperationType

/**
 * LoadableData is used for loading UI content from any data source.
 *
 * Examples of UI content: images, text information, lists.
 *
 * Usage exmaple:
 * ```
 * data class FeedContentState(
 *     val news: LoadableData<News>,
 *     val stories: LoadableData<Stories>
 * )
 * ```
 *
 * Or you can parse LoadableData in ViewModel and update ContentState fields
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
     * Used only for 204 response code
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