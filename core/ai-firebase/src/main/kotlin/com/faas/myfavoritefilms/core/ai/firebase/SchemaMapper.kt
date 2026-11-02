package com.faas.myfavoritefilms.core.ai.firebase

import com.faas.myfavoritefilms.core.ai.AiSchema
import com.google.firebase.ai.type.Schema

internal fun AiSchema.toFirebaseSchema(): Schema = when (this) {
    is AiSchema.Obj -> Schema.obj(
        properties = properties.mapValues { (_, value) -> value.toFirebaseSchema() },
        optionalProperties = optional,
        description = description,
    )
    is AiSchema.Arr -> Schema.array(items = items.toFirebaseSchema(), description = description)
    is AiSchema.Integer -> Schema.integer(description = description)
    is AiSchema.Decimal -> Schema.double(description = description)
    is AiSchema.Text -> Schema.string(description = description)
    is AiSchema.Enumeration -> Schema.enumeration(values = values, description = description)
}
