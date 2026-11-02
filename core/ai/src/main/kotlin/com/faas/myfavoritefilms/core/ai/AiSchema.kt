package com.faas.myfavoritefilms.core.ai

sealed interface AiSchema {
    val description: String?

    data class Obj(
        val properties: Map<String, AiSchema>,
        val optional: List<String> = emptyList(),
        override val description: String? = null,
    ) : AiSchema

    data class Arr(
        val items: AiSchema,
        override val description: String? = null,
    ) : AiSchema

    data class Integer(override val description: String? = null) : AiSchema

    data class Decimal(override val description: String? = null) : AiSchema

    data class Text(override val description: String? = null) : AiSchema

    data class Enumeration(
        val values: List<String>,
        override val description: String? = null,
    ) : AiSchema
}
