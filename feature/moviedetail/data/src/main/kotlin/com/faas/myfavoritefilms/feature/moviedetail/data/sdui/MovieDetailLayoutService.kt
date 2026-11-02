package com.faas.myfavoritefilms.feature.moviedetail.data.sdui

import com.faas.myfavoritefilms.core.network.TmdbImages
import com.faas.myfavoritefilms.core.network.sdui.SduiParser
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.CastDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.MovieDetailDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.VideoDto
import kotlinx.serialization.json.JsonArrayBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

internal class MovieDetailLayoutService @Inject constructor(
    private val strings: MovieDetailLayoutStrings,
) {
    fun layoutFor(movie: MovieDetailDto, locale: Locale = Locale.getDefault()): String = buildJsonObject {
        put("schemaVersion", 1)
        putJsonArray("components") {
            technicalSheet(movie, locale)
            castCarousel(movie.credits.cast)
            videoCarousel(movie.videos.results)
            chipGroup(strings.genres, movie.genres.map { it.name })
            chipGroup(strings.productionCompanies, movie.productionCompanies.map { it.name })
            chipGroup(strings.productionCountries, movie.productionCountries.map { it.name })
            chipGroup(
                strings.spokenLanguages,
                movie.spokenLanguages.map { it.name.ifBlank { it.englishName } }.filter { it.isNotBlank() },
            )
            if (!movie.homepage.isNullOrBlank() || !movie.imdbId.isNullOrBlank()) {
                addJsonObject { put("type", SduiParser.TYPE_DIVIDER) }
            }
            movie.homepage?.takeIf { it.isNotBlank() }?.let { link(strings.homepage, it) }
            movie.imdbId?.takeIf { it.isNotBlank() }?.let { link(strings.imdb, "$IMDB_TITLE_URL$it") }

            addJsonObject {
                put("type", "ar_experience")
                put("minAppVersion", 2)
            }
        }
    }.toString()

    private fun JsonArrayBuilder.technicalSheet(movie: MovieDetailDto, locale: Locale) {
        val rows = buildList {
            movie.originalTitle?.takeIf { it.isNotBlank() && it != movie.title }
                ?.let { add(Row(strings.originalTitle, it, "info")) }
            movie.originalLanguage?.takeIf { it.isNotBlank() }
                ?.let { add(Row(strings.originalLanguage, languageName(it, locale), "person")) }
            movie.status?.takeIf { it.isNotBlank() }
                ?.let { add(Row(strings.status, strings.statuses[it] ?: it, "info")) }
            movie.releaseDate?.takeIf { it.isNotBlank() }
                ?.let { add(Row(strings.releaseDate, localizedDate(it, locale), "calendar")) }
            movie.budget.takeIf { it > 0 }?.let { add(Row(strings.budget, usd(it), "money")) }
            movie.revenue.takeIf { it > 0 }?.let { add(Row(strings.revenue, usd(it), "money")) }
            movie.voteCount.takeIf { it > 0 }
                ?.let { add(Row(strings.voteCount, NumberFormat.getIntegerInstance(locale).format(it), "thumb_up")) }
            movie.popularity.takeIf { it > 0 }
                ?.let { add(Row(strings.popularity, String.format(locale, "%.1f", it), "star")) }
        }
        if (rows.isEmpty()) return
        addJsonObject {
            put("type", SduiParser.TYPE_KEY_VALUE_LIST)
            put("title", strings.technicalSheet)
            putJsonArray("items") {
                rows.forEach { row ->
                    addJsonObject {
                        put("label", row.label)
                        put("value", row.value)
                        put("icon", row.icon)
                    }
                }
            }
        }
    }

    private fun JsonArrayBuilder.castCarousel(cast: List<CastDto>) {
        val people = cast.sortedBy { it.order }.take(MAX_CAST)
        if (people.isEmpty()) return
        addJsonObject {
            put("type", SduiParser.TYPE_PERSON_CAROUSEL)
            put("title", strings.cast)
            putJsonArray("people") {
                people.forEach { person ->
                    addJsonObject {
                        put("name", person.name)
                        person.character?.takeIf { it.isNotBlank() }?.let { put("subtitle", it) }
                        TmdbImages.url(person.profilePath, TmdbImages.Size.W185)?.let { put("imageUrl", it) }
                    }
                }
            }
        }
    }

    private fun JsonArrayBuilder.videoCarousel(videos: List<VideoDto>) {
        val playable = videos
            .filter { it.site.equals(YOUTUBE, ignoreCase = true) && it.type in VIDEO_TYPES }
            .sortedWith(compareBy<VideoDto> { VIDEO_TYPES.indexOf(it.type) }.thenByDescending { it.official })
            .take(MAX_VIDEOS)
        if (playable.isEmpty()) return
        addJsonObject {
            put("type", SduiParser.TYPE_VIDEO_CAROUSEL)
            put("title", strings.videos)
            putJsonArray("videos") {
                playable.forEach { video ->
                    addJsonObject {
                        put("title", video.name)
                        put("url", "$YOUTUBE_WATCH_URL${video.key}")
                        put("thumbnailUrl", YOUTUBE_THUMBNAIL_URL.format(video.key))
                    }
                }
            }
        }
    }

    private fun JsonArrayBuilder.chipGroup(title: String, chips: List<String>) {
        if (chips.isEmpty()) return
        addJsonObject {
            put("type", SduiParser.TYPE_CHIP_GROUP)
            put("title", title)
            putJsonArray("chips") { chips.forEach { add(JsonPrimitive(it)) } }
        }
    }

    private fun JsonArrayBuilder.link(label: String, url: String) {
        addJsonObject {
            put("type", SduiParser.TYPE_LINK)
            put("label", label)
            put("url", url)
        }
    }

    private data class Row(val label: String, val value: String, val icon: String)

    private fun usd(value: Long): String =
        NumberFormat.getCurrencyInstance(Locale.US).apply { maximumFractionDigits = 0 }.format(value)

    private fun languageName(code: String, locale: Locale): String =
        Locale.forLanguageTag(code).getDisplayLanguage(locale)
            .replaceFirstChar { it.titlecase(locale) }
            .ifBlank { code }

    private fun localizedDate(isoDate: String, locale: Locale): String =
        runCatching {
            LocalDate.parse(isoDate).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale))
        }.getOrDefault(isoDate)

    private companion object {
        const val IMDB_TITLE_URL = "https://www.imdb.com/title/"
        const val YOUTUBE = "YouTube"
        const val YOUTUBE_WATCH_URL = "https://www.youtube.com/watch?v="
        const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/hqdefault.jpg"
        const val MAX_CAST = 15
        const val MAX_VIDEOS = 10
        val VIDEO_TYPES = listOf("Trailer", "Teaser")
    }
}
