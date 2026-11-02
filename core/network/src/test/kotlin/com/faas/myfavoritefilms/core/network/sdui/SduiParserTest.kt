package com.faas.myfavoritefilms.core.network.sdui

import com.faas.myfavoritefilms.core.common.sdui.KeyValueItem
import com.faas.myfavoritefilms.core.common.sdui.PersonItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.common.sdui.VideoItem
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SduiParserTest {

    private val parser = SduiParser(Json { ignoreUnknownKeys = true })

    @Test
    fun `parses every known type`() {
        val payload = """
            { "schemaVersion": 1, "components": [
              { "type": "section_title", "text": "Sobre" },
              { "type": "key_value_list", "title": "Ficha", "items": [
                  { "label": "Status", "value": "Lançado", "icon": "info" } ] },
              { "type": "chip_group", "title": "Países", "chips": ["Brasil", "EUA"] },
              { "type": "text", "text": "Olá" },
              { "type": "link", "label": "IMDb", "url": "https://imdb.com" },
              { "type": "divider" }
            ] }
        """.trimIndent()

        val components = parser.parse(payload)

        assertEquals(
            listOf(
                SduiComponent.SectionTitle("Sobre"),
                SduiComponent.KeyValueList("Ficha", listOf(KeyValueItem("Status", "Lançado", "info"))),
                SduiComponent.ChipGroup("Países", listOf("Brasil", "EUA")),
                SduiComponent.TextBlock(null, "Olá"),
                SduiComponent.LinkButton("IMDb", "https://imdb.com"),
                SduiComponent.Divider,
            ),
            components,
        )
    }

    @Test
    fun `drops unknown types and malformed blocks keeping the rest`() {
        val payload = """
            { "components": [
              { "type": "carousel_3d", "items": [] },
              { "type": "link", "label": "Sem url" },
              { "type": "text", "text": "fica" }
            ] }
        """.trimIndent()

        assertEquals(listOf(SduiComponent.TextBlock(null, "fica")), parser.parse(payload))
    }

    @Test
    fun `parses carousels dropping invalid items`() {
        val payload = """
            { "components": [
              { "type": "person_carousel", "title": "Cast", "people": [
                  { "name": "Keanu Reeves", "subtitle": "Neo", "imageUrl": "https://img/keanu.jpg" },
                  { "subtitle": "No name" },
                  { "name": "Carrie-Anne Moss" } ] },
              { "type": "video_carousel", "title": "Videos", "videos": [
                  { "title": "Trailer", "url": "https://youtu.be/a", "thumbnailUrl": "https://img/a.jpg" },
                  { "title": "No url" } ] },
              { "type": "person_carousel", "people": [ { "subtitle": "only invalid" } ] },
              { "type": "video_carousel", "videos": [] }
            ] }
        """.trimIndent()

        assertEquals(
            listOf(
                SduiComponent.PersonCarousel(
                    "Cast",
                    listOf(PersonItem("Keanu Reeves", "Neo", "https://img/keanu.jpg"), PersonItem("Carrie-Anne Moss")),
                ),
                SduiComponent.VideoCarousel("Videos", listOf(VideoItem("Trailer", "https://youtu.be/a", "https://img/a.jpg"))),
            ),
            parser.parse(payload),
        )
    }

    @Test
    fun `invalid payload becomes an empty list`() {
        assertTrue(parser.parse("not json").isEmpty())
        assertTrue(parser.parse("""{ "outra": 1 }""").isEmpty())
    }
}
