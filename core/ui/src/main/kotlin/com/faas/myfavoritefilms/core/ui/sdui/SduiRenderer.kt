package com.faas.myfavoritefilms.core.ui.sdui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import com.faas.myfavoritefilms.core.designsystem.component.BACKDROP_ASPECT_RATIO
import com.faas.myfavoritefilms.core.designsystem.component.MoviePoster
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faas.myfavoritefilms.core.common.sdui.KeyValueItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.designsystem.component.LabelChips
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme

fun LazyListScope.sduiItems(
    components: List<SduiComponent>,
    itemModifier: Modifier = Modifier,
) {
    items(
        count = components.size,
        key = { index -> "sdui-$index" },
        contentType = { index -> components[index]::class.simpleName },
    ) { index ->
        SduiComponentView(component = components[index], modifier = itemModifier)
    }
}

@Composable
fun SduiComponentView(component: SduiComponent, modifier: Modifier = Modifier) {
    when (component) {
        is SduiComponent.SectionTitle -> Text(
            text = component.text,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier,
        )
        is SduiComponent.KeyValueList -> SduiKeyValueList(component, modifier)
        is SduiComponent.ChipGroup -> Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            component.title?.let { SduiBlockTitle(it) }
            LabelChips(labels = component.chips)
        }
        is SduiComponent.TextBlock -> Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            component.title?.let { SduiBlockTitle(it) }
            Text(text = component.text, style = MaterialTheme.typography.bodyMedium)
        }
        is SduiComponent.LinkButton -> {
            val uriHandler = LocalUriHandler.current
            OutlinedButton(
                onClick = { runCatching { uriHandler.openUri(component.url) } },
                modifier = modifier.fillMaxWidth(),
            ) { Text(component.label) }
        }
        is SduiComponent.PersonCarousel -> SduiPersonCarousel(component, modifier)
        is SduiComponent.VideoCarousel -> SduiVideoCarousel(component, modifier)
        SduiComponent.Divider -> HorizontalDivider(modifier)
    }
}

@Composable
private fun SduiPersonCarousel(component: SduiComponent.PersonCarousel, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        component.title?.let { SduiBlockTitle(it) }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(component.people) { person ->
                Column(
                    modifier = Modifier.width(PersonWidth),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    MoviePoster(
                        url = person.imageUrl,
                        contentDescription = person.name,
                        aspectRatio = 1f,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    person.subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SduiVideoCarousel(component: SduiComponent.VideoCarousel, modifier: Modifier) {
    val uriHandler = LocalUriHandler.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        component.title?.let { SduiBlockTitle(it) }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(component.videos) { video ->
                Card(
                    onClick = { runCatching { uriHandler.openUri(video.url) } },
                    modifier = Modifier.width(VideoWidth),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        MoviePoster(
                            url = video.thumbnailUrl,
                            contentDescription = video.title,
                            aspectRatio = BACKDROP_ASPECT_RATIO,
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp).size(28.dp),
                            )
                        }
                    }
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}

private val PersonWidth = 88.dp
private val VideoWidth = 240.dp

@Composable
private fun SduiBlockTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun SduiKeyValueList(component: SduiComponent.KeyValueList, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        component.title?.let { SduiBlockTitle(it) }
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            component.items.forEachIndexed { index, item ->
                if (index > 0) HorizontalDivider()
                SduiKeyValueRow(item)
            }
        }
    }
}

@Composable
private fun SduiKeyValueRow(item: KeyValueItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        sduiIcon(item.icon)?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = item.label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = item.value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun sduiIcon(key: String?): ImageVector? = when (key) {
    "calendar" -> Icons.Filled.DateRange
    "star" -> Icons.Filled.Star
    "money" -> Icons.Filled.ShoppingCart
    "place" -> Icons.Filled.Place
    "person" -> Icons.Filled.Person
    "info" -> Icons.Filled.Info
    "thumb_up" -> Icons.Filled.ThumbUp
    "build" -> Icons.Filled.Build
    else -> null
}

@Preview(showBackground = true)
@Composable
private fun SduiPreview() {
    MoviesTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(
                SduiComponent.KeyValueList(
                    title = "Ficha técnica",
                    items = listOf(
                        KeyValueItem("Título original", "The Matrix", "info"),
                        KeyValueItem("Orçamento", "$63,000,000", "money"),
                    ),
                ),
                SduiComponent.ChipGroup("Produtoras", listOf("Warner Bros.", "Village Roadshow")),
                SduiComponent.LinkButton("Ver no IMDb", "https://www.imdb.com"),
            ).forEach { SduiComponentView(it) }
        }
    }
}
