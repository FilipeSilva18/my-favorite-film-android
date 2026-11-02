package com.faas.myfavoritefilms.feature.moviedetail.data.sdui

import com.faas.myfavoritefilms.core.common.sdui.PersonItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.common.sdui.VideoItem
import com.faas.myfavoritefilms.core.network.sdui.SduiParser
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.CastDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.CreditsDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.MovieDetailDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.VideoDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.VideosDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.NamedDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class MovieDetailLayoutServiceTest {

    private val service = MovieDetailLayoutService(testLayoutStrings)
    private val parser = SduiParser(Json { ignoreUnknownKeys = true })
    private val ptBr = Locale.forLanguageTag("pt-BR")

    private val matrix = MovieDetailDto(
        id = 603,
        title = "Matrix",
        originalTitle = "The Matrix",
        originalLanguage = "en",
        status = "Released",
        releaseDate = "1999-03-30",
        budget = 63_000_000,
        voteCount = 25_431,
        productionCompanies = listOf(NamedDto("Warner Bros.")),
        imdbId = "tt0133093",
    )

    private fun parse(dto: MovieDetailDto, locale: Locale = ptBr) = parser.parse(service.layoutFor(dto, locale))

    @Test
    fun `technical sheet uses injected labels and locale aware values`() {
        val sheet = parse(matrix).filterIsInstance<SduiComponent.KeyValueList>().single()
        val rows = sheet.items.associate { it.label to it.value }

        assertEquals("Details", sheet.title)
        assertEquals("The Matrix", rows["Original title"])
        assertEquals("Released (localized)", rows["Status"])
        assertEquals("30/03/1999", rows["Release date"])
        assertEquals("$63,000,000", rows["Budget"])
        assertEquals("25.431", rows["Ratings"])
    }

    @Test
    fun `values follow the given locale`() {
        val rows = parse(matrix, Locale.US).filterIsInstance<SduiComponent.KeyValueList>().single()
            .items.associate { it.label to it.value }

        assertEquals("25,431", rows["Ratings"])
        assertEquals("English", rows["Original language"])
    }

    @Test
    fun `blocks and rows without data are omitted`() {
        val components = parse(matrix)

        val sheet = components.filterIsInstance<SduiComponent.KeyValueList>().single()
        assertFalse(sheet.items.any { it.label == "Box office" })
        assertFalse(components.any { it is SduiComponent.ChipGroup && it.title == "Production countries" })
    }

    @Test
    fun `unknown server component is dropped and links are kept`() {
        val components = parse(matrix)

        assertEquals(
            listOf(SduiComponent.LinkButton("View on IMDb", "https://www.imdb.com/title/tt0133093")),
            components.filterIsInstance<SduiComponent.LinkButton>(),
        )
        assertTrue(components.last() is SduiComponent.LinkButton)
    }

    @Test
    fun `cast carousel is ordered by billing and limited`() {
        val cast = (0 until 20).reversed().map { CastDto(id = it, name = "Actor $it", character = "Role $it", profilePath = "/p$it.jpg", order = it) }

        val carousel = parse(matrix.copy(credits = CreditsDto(cast))).filterIsInstance<SduiComponent.PersonCarousel>().single()

        assertEquals("Cast", carousel.title)
        assertEquals(15, carousel.people.size)
        assertEquals(PersonItem("Actor 0", "Role 0", "https://image.tmdb.org/t/p/w185/p0.jpg"), carousel.people.first())
    }

    @Test
    fun `video carousel keeps only YouTube trailers and teasers with trailers first`() {
        val videos = listOf(
            VideoDto(key = "clip", name = "Clip", site = "YouTube", type = "Clip"),
            VideoDto(key = "teaser", name = "Teaser", site = "YouTube", type = "Teaser", official = true),
            VideoDto(key = "fan", name = "Fan trailer", site = "YouTube", type = "Trailer", official = false),
            VideoDto(key = "official", name = "Official trailer", site = "YouTube", type = "Trailer", official = true),
            VideoDto(key = "vimeo", name = "Vimeo", site = "Vimeo", type = "Trailer", official = true),
        )

        val carousel = parse(matrix.copy(videos = VideosDto(videos))).filterIsInstance<SduiComponent.VideoCarousel>().single()

        assertEquals("Trailers and videos", carousel.title)
        assertEquals(listOf("Official trailer", "Fan trailer", "Teaser"), carousel.videos.map { it.title })
        assertEquals(
            VideoItem("Official trailer", "https://www.youtube.com/watch?v=official", "https://img.youtube.com/vi/official/hqdefault.jpg"),
            carousel.videos.first(),
        )
    }

    @Test
    fun `carousels are omitted without cast or playable videos`() {
        val components = parse(matrix.copy(videos = VideosDto(listOf(VideoDto("x", "Clip", "YouTube", "Clip")))))

        assertNull(components.firstOrNull { it is SduiComponent.PersonCarousel })
        assertNull(components.firstOrNull { it is SduiComponent.VideoCarousel })
    }
}
