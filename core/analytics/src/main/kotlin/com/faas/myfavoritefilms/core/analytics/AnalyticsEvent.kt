package com.faas.myfavoritefilms.core.analytics

data class AnalyticsEvent(
    val name: String,
    val params: Map<String, Any> = emptyMap(),
)

object AnalyticsEvents {
    const val SCREEN_VIEW = "screen_view"
    const val SEARCH = "search"
    const val SEARCH_RESULTS = "search_results"
    const val MOVIE_OPENED = "movie_opened"
    const val FAVORITE_ADDED = "favorite_added"
    const val FAVORITE_REMOVED = "favorite_removed"
    const val SETTINGS_CHANGED = "settings_changed"
    const val AI_SEARCH = "ai_search"
}

object AnalyticsParams {
    const val SCREEN_NAME = "screen_name"
    const val SEARCH_TERM = "search_term"
    const val HAS_RESULTS = "has_results"
    const val FIRST_PAGE_COUNT = "first_page_count"
    const val MOVIE_ID = "movie_id"
    const val SOURCE = "source"
    const val SETTING = "setting"
    const val VALUE = "value"
    const val OUTCOME = "outcome"
    const val FILTER_COUNT = "filter_count"
}

enum class MovieSource(val value: String) {
    POPULAR("popular"),
    SEARCH("search"),
    AI_SEARCH("ai_search"),
    FAVORITES("favorites"),
    DETAIL("detail"),
}

object ScreenNames {
    const val MOVIES = "movies"
    const val MOVIE_DETAIL = "movie_detail"
    const val FAVORITES = "favorites"
    const val SETTINGS = "settings"
}

fun screenViewEvent(screenName: String) = AnalyticsEvent(
    name = AnalyticsEvents.SCREEN_VIEW,
    params = mapOf(AnalyticsParams.SCREEN_NAME to screenName),
)

fun movieOpenedEvent(movieId: Int, source: MovieSource) = AnalyticsEvent(
    name = AnalyticsEvents.MOVIE_OPENED,
    params = mapOf(AnalyticsParams.MOVIE_ID to movieId.toLong(), AnalyticsParams.SOURCE to source.value),
)

fun favoriteToggledEvent(movieId: Int, isFavorite: Boolean, source: MovieSource) = AnalyticsEvent(
    name = if (isFavorite) AnalyticsEvents.FAVORITE_ADDED else AnalyticsEvents.FAVORITE_REMOVED,
    params = mapOf(AnalyticsParams.MOVIE_ID to movieId.toLong(), AnalyticsParams.SOURCE to source.value),
)

fun searchEvent(term: String) = AnalyticsEvent(
    name = AnalyticsEvents.SEARCH,
    params = mapOf(AnalyticsParams.SEARCH_TERM to term.take(MAX_PARAM_LENGTH)),
)

fun searchResultsEvent(term: String, firstPageCount: Int) = AnalyticsEvent(
    name = AnalyticsEvents.SEARCH_RESULTS,
    params = mapOf(
        AnalyticsParams.SEARCH_TERM to term.take(MAX_PARAM_LENGTH),
        AnalyticsParams.HAS_RESULTS to (firstPageCount > 0).toString(),
        AnalyticsParams.FIRST_PAGE_COUNT to firstPageCount.toLong(),
    ),
)

fun settingsChangedEvent(setting: String, value: String) = AnalyticsEvent(
    name = AnalyticsEvents.SETTINGS_CHANGED,
    params = mapOf(AnalyticsParams.SETTING to setting, AnalyticsParams.VALUE to value),
)

enum class AiSearchOutcome(val value: String) {
    UNDERSTOOD("understood"),
    NOT_UNDERSTOOD("not_understood"),
    UNAVAILABLE("unavailable"),
}

fun aiSearchEvent(term: String, outcome: AiSearchOutcome, filterCount: Int) = AnalyticsEvent(
    name = AnalyticsEvents.AI_SEARCH,
    params = mapOf(
        AnalyticsParams.SEARCH_TERM to term.take(MAX_PARAM_LENGTH),
        AnalyticsParams.OUTCOME to outcome.value,
        AnalyticsParams.FILTER_COUNT to filterCount.toLong(),
    ),
)

private const val MAX_PARAM_LENGTH = 100
