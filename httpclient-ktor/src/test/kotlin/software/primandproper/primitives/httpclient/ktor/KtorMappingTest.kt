package software.primandproper.primitives.httpclient.ktor

import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.http.HttpMethod as KtorMethod
import software.primandproper.primitives.httpclient.HttpMethod as PlatformMethod

/** Pure verb-mapping tests — no engine or network required. */
class KtorMappingTest {
    @Test
    fun `maps every verb onto its ktor counterpart`() {
        assertEquals(KtorMethod.Get, PlatformMethod.GET.toKtorMethod())
        assertEquals(KtorMethod.Post, PlatformMethod.POST.toKtorMethod())
        assertEquals(KtorMethod.Put, PlatformMethod.PUT.toKtorMethod())
        assertEquals(KtorMethod.Patch, PlatformMethod.PATCH.toKtorMethod())
        assertEquals(KtorMethod.Delete, PlatformMethod.DELETE.toKtorMethod())
        assertEquals(KtorMethod.Head, PlatformMethod.HEAD.toKtorMethod())
        assertEquals(KtorMethod.Options, PlatformMethod.OPTIONS.toKtorMethod())
    }
}
