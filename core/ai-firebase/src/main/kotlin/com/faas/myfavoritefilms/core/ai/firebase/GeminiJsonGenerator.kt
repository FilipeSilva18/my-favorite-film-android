package com.faas.myfavoritefilms.core.ai.firebase

import com.faas.myfavoritefilms.core.ai.AiException
import com.faas.myfavoritefilms.core.ai.AiJsonGenerator
import com.faas.myfavoritefilms.core.ai.AiSchema
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FirebaseAIException
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.QuotaExceededException
import com.google.firebase.ai.type.RequestTimeoutException
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import java.io.IOException

internal class GeminiJsonGenerator : AiJsonGenerator {
    override suspend fun generate(instructions: String, input: String, schema: AiSchema): String {
        val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = JSON_MIME_TYPE
                responseSchema = schema.toFirebaseSchema()
                temperature = 0f
            },
            systemInstruction = content { text(instructions) },
        )
        val response = try {
            model.generateContent(input)
        } catch (e: CancellationException) {
            throw e
        } catch (e: QuotaExceededException) {
            throw AiException.QuotaExceeded(e)
        } catch (e: RequestTimeoutException) {
            throw IOException("AI request timed out", e)
        } catch (e: FirebaseAIException) {
            throw AiException.Failed(e.message ?: "AI request failed", e)
        }
        return response.text?.takeIf { it.isNotBlank() } ?: throw AiException.Failed("Empty AI response")
    }

    private companion object {
        const val MODEL_NAME = "gemini-3.5-flash-lite"
        const val JSON_MIME_TYPE = "application/json"
    }
}
