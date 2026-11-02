package com.faas.myfavoritefilms.core.network.sdui

import com.faas.myfavoritefilms.core.common.sdui.KeyValueItem
import com.faas.myfavoritefilms.core.common.sdui.PersonItem
import com.faas.myfavoritefilms.core.common.sdui.VideoItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

class SduiParser @Inject constructor(
    private val json: Json,
) {
    fun parse(payload: String): List<SduiComponent> {
        val root = runCatching { json.parseToJsonElement(payload).jsonObject }.getOrNull()
            ?: return emptyList()
        val components = root["components"] as? JsonArray ?: return emptyList()
        return components.mapNotNull { element ->
            runCatching { (element as JsonObject).toComponent() }.getOrNull()
        }
    }

    private fun JsonObject.toComponent(): SduiComponent? = when (string("type")) {
        TYPE_SECTION_TITLE -> SduiComponent.SectionTitle(text = requireString("text"))
        TYPE_KEY_VALUE_LIST -> SduiComponent.KeyValueList(
            title = string("title"),
            items = getValue("items").jsonArray.mapNotNull { item ->
                val obj = item.jsonObject
                val label = obj.string("label") ?: return@mapNotNull null
                val value = obj.string("value") ?: return@mapNotNull null
                KeyValueItem(label = label, value = value, icon = obj.string("icon"))
            },
        ).takeIf { it.items.isNotEmpty() }
        TYPE_CHIP_GROUP -> SduiComponent.ChipGroup(
            title = string("title"),
            chips = getValue("chips").jsonArray.mapNotNull { (it as? JsonPrimitive)?.contentOrNull },
        ).takeIf { it.chips.isNotEmpty() }
        TYPE_TEXT -> SduiComponent.TextBlock(title = string("title"), text = requireString("text"))
        TYPE_LINK -> SduiComponent.LinkButton(label = requireString("label"), url = requireString("url"))
        TYPE_PERSON_CAROUSEL -> SduiComponent.PersonCarousel(
            title = string("title"),
            people = getValue("people").jsonArray.mapNotNull { item ->
                val obj = item as? JsonObject ?: return@mapNotNull null
                val name = obj.string("name") ?: return@mapNotNull null
                PersonItem(name = name, subtitle = obj.string("subtitle"), imageUrl = obj.string("imageUrl"))
            },
        ).takeIf { it.people.isNotEmpty() }
        TYPE_VIDEO_CAROUSEL -> SduiComponent.VideoCarousel(
            title = string("title"),
            videos = getValue("videos").jsonArray.mapNotNull { item ->
                val obj = item as? JsonObject ?: return@mapNotNull null
                val title = obj.string("title") ?: return@mapNotNull null
                val url = obj.string("url") ?: return@mapNotNull null
                VideoItem(title = title, url = url, thumbnailUrl = obj.string("thumbnailUrl"))
            },
        ).takeIf { it.videos.isNotEmpty() }
        TYPE_DIVIDER -> SduiComponent.Divider
        else -> null
    }

    private fun JsonObject.string(key: String): String? =
        (this[key] as? JsonPrimitive)?.contentOrNull?.takeIf { it.isNotBlank() }

    private fun JsonObject.requireString(key: String): String =
        requireNotNull(string(key)) { "SDUI: missing required field '$key'" }

    companion object {
        const val TYPE_SECTION_TITLE = "section_title"
        const val TYPE_KEY_VALUE_LIST = "key_value_list"
        const val TYPE_CHIP_GROUP = "chip_group"
        const val TYPE_TEXT = "text"
        const val TYPE_LINK = "link"
        const val TYPE_DIVIDER = "divider"
        const val TYPE_PERSON_CAROUSEL = "person_carousel"
        const val TYPE_VIDEO_CAROUSEL = "video_carousel"
    }
}
