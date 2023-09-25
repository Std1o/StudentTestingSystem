package student.testing.system.domain.states

import student.testing.system.annotations.FunctionalityState

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
sealed interface LoadableData<out R>