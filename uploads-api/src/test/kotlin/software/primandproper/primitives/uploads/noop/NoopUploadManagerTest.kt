package software.primandproper.primitives.uploads.noop

import kotlinx.coroutines.test.runTest
import software.primandproper.primitives.uploads.Attributer
import software.primandproper.primitives.uploads.Lister
import software.primandproper.primitives.uploads.RangeReader
import software.primandproper.primitives.uploads.UrlSigner
import software.primandproper.primitives.uploads.listAll
import software.primandproper.primitives.uploads.readBytes
import software.primandproper.primitives.uploads.saveBytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Port of platform-go's `uploads/noop.TestUploadManager` — every op is a safe no-op. */
class NoopUploadManagerTest {
    private val noop = NoopUploadManager

    @Test
    fun `save drains the source and open returns empty`() =
        runTest {
            noop.saveBytes("k", "ignored".toByteArray())
            assertEquals(0, noop.readBytes("k").size)
        }

    @Test
    fun `exists is always false and delete is a no-op`() =
        runTest {
            assertFalse(noop.exists("anything"))
            noop.delete("anything")
        }

    @Test
    fun `capabilities are satisfied and inert`() =
        runTest {
            assertEquals(0, (noop as RangeReader).openRange("k", 0, 10).readBytes().size)
            assertTrue((noop as Attributer).attributes("k").contentType == null)
            assertEquals(emptyList(), listAll(noop as Lister, ""))
            assertEquals("", (noop as UrlSigner).signedUrl("k"))
        }
}
