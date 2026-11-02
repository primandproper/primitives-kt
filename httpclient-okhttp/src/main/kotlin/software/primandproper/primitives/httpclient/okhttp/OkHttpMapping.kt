package software.primandproper.primitives.httpclient.okhttp

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import software.primandproper.primitives.httpclient.HttpHeaders
import software.primandproper.primitives.httpclient.HttpMethod
import software.primandproper.primitives.httpclient.HttpRequest
import software.primandproper.primitives.httpclient.HttpResponse

// OkHttp requires a request body for these verbs and forbids one for GET/HEAD.
private val BODY_REQUIRED = setOf(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH)

/** Translates the transport-agnostic [HttpRequest] into an OkHttp [Request]. */
internal fun HttpRequest.toOkHttpRequest(): Request {
    val builder = Request.Builder().url(url)
    headers.forEach { name, values ->
        values.forEach { builder.addHeader(name, it) }
    }

    val contentType = headers.first("content-type")?.toMediaTypeOrNull()
    val requestBody =
        body?.toRequestBody(contentType)
            ?: if (method in BODY_REQUIRED) ByteArray(0).toRequestBody() else null

    builder.method(method.name, requestBody)
    return builder.build()
}

/** Reads an OkHttp [Response] fully into the transport-agnostic [HttpResponse]. */
internal fun Response.toHttpResponse(): HttpResponse {
    val headerBuilder = HttpHeaders.Builder()
    for (i in 0 until headers.size) {
        headerBuilder.add(headers.name(i), headers.value(i))
    }
    val bytes = body?.bytes() ?: ByteArray(0)
    return HttpResponse(statusCode = code, headers = headerBuilder.build(), body = bytes)
}
