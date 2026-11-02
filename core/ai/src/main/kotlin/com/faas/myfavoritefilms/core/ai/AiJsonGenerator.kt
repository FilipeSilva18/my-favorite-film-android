package com.faas.myfavoritefilms.core.ai

interface AiJsonGenerator {
    suspend fun generate(instructions: String, input: String, schema: AiSchema): String
}
