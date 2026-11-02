package com.faas.myfavoritefilms.core.common.sdui

sealed interface SduiComponent {
    data class SectionTitle(val text: String) : SduiComponent

    data class KeyValueList(
        val title: String?,
        val items: List<KeyValueItem>,
    ) : SduiComponent

    data class ChipGroup(
        val title: String?,
        val chips: List<String>,
    ) : SduiComponent

    data class TextBlock(
        val title: String?,
        val text: String,
    ) : SduiComponent

    data class LinkButton(
        val label: String,
        val url: String,
    ) : SduiComponent

    data class PersonCarousel(
        val title: String?,
        val people: List<PersonItem>,
    ) : SduiComponent

    data class VideoCarousel(
        val title: String?,
        val videos: List<VideoItem>,
    ) : SduiComponent

    data object Divider : SduiComponent
}

data class PersonItem(
    val name: String,
    val subtitle: String? = null,
    val imageUrl: String? = null,
)

data class VideoItem(
    val title: String,
    val url: String,
    val thumbnailUrl: String? = null,
)

data class KeyValueItem(
    val label: String,
    val value: String,
    val icon: String? = null,
)
