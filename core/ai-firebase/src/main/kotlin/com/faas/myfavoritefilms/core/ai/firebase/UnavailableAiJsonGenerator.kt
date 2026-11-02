package com.faas.myfavoritefilms.core.ai.firebase

import com.faas.myfavoritefilms.core.ai.AiException
import com.faas.myfavoritefilms.core.ai.AiJsonGenerator
import com.faas.myfavoritefilms.core.ai.AiSchema

internal class UnavailableAiJsonGenerator : AiJsonGenerator {
    override suspend fun generate(instructions: String, input: String, schema: AiSchema): String =
        throw AiException.Unavailable("Firebase is not configured (missing google-services.json)")
}
