package software.primandproper.primitives.httpclient.ktor

import io.ktor.client.call.body
import software.primandproper.primitives.httpclient.HttpHeaders
import software.primandproper.primitives.httpclient.HttpResponse
import io.ktor.client.statement.HttpResponse as KtorResponse
import io.ktor.http.HttpMethod as KtorMethod
import software.primandproper.primitives.httpclient.HttpMethod as PlatformMethod

/** Maps the transport-agnostic verb onto Ktor's [KtorMethod]. */
internal fun PlatformMethod.toKtorMethod(): KtorMethod =
    when (this) {
        PlatformMethod.GET -> KtorMethod.Get
        PlatformMethod.POST -> KtorMethod.Post
        PlatformMethod.PUT -> KtorMethod.Put
        PlatformMethod.PATCH -> KtorMethod.Patch
        PlatformMethod.DELETE -> KtorMethod.Delete
        PlatformMethod.HEAD -> KtorMethod.Head
        PlatformMethod.OPTIONS -> KtorMethod.Options
    }

/** Reads a Ktor [KtorResponse] fully into the transport-agnostic [HttpResponse]. */
internal suspend fun KtorResponse.toHttpResponse(): HttpResponse {
    val headerBuilder = HttpHeaders.Builder()
    headers.forEach { name, values ->
        values.forEach { headerBuilder.add(name, it) }
    }
    val bytes = body<ByteArray>()
    return HttpResponse(statusCode = status.value, headers = headerBuilder.build(), body = bytes)
}
