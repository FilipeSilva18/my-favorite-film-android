package com.faas.myfavoritefilms.core.network

import kotlinx.serialization.json.Json

val TmdbJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    coerceInputValues = true
}
