package software.primandproper.primitives.uploads.noop

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import software.primandproper.primitives.uploads.Attributer
import software.primandproper.primitives.uploads.Attributes
import software.primandproper.primitives.uploads.Lister
import software.primandproper.primitives.uploads.ObjectInfo
import software.primandproper.primitives.uploads.RangeReader
import software.primandproper.primitives.uploads.SaveOptions
import software.primandproper.primitives.uploads.SignedUrlOptions
import software.primandproper.primitives.uploads.UploadManager
import software.primandproper.primitives.uploads.UrlSigner
import java.io.InputStream

/**
 * A no-op [UploadManager] that also satisfies every optional capability — reads always return empty,
 * writes are discarded, existence is always `false`, and listing yields nothing. Port of platform-go's
 * `uploads/noop.UploadManager`. A safe default for wiring, and for tests that don't care about real
 * storage.
 */
public object NoopUploadManager :
    UploadManager,
    RangeReader,
    UrlSigner,
    Attributer,
    Lister {
    /** Drains [source] so a caller streaming into it still makes progress, mirroring Go's `io.Copy(io.Discard, r)`. */
    override suspend fun save(
        path: String,
        source: InputStream,
        options: SaveOptions,
    ) {
        // Draining the stream is a blocking copy; hop off the caller's dispatcher.
        withContext(Dispatchers.IO) { source.copyTo(NULL_OUTPUT_STREAM) }
    }

    override suspend fun open(path: String): InputStream = InputStream.nullInputStream()

    override suspend fun openRange(
        path: String,
        offset: Long,
        length: Long,
    ): InputStream = InputStream.nullInputStream()

    override suspend fun delete(path: String) {
    }

    override suspend fun exists(path: String): Boolean = false

    override suspend fun attributes(path: String): Attributes = Attributes()

    override fun list(prefix: String): Flow<ObjectInfo> = emptyFlow()

    override suspend fun signedUrl(
        path: String,
        options: SignedUrlOptions,
    ): String = ""

    private val NULL_OUTPUT_STREAM =
        object : java.io.OutputStream() {
            override fun write(b: Int) {}

            override fun write(
                b: ByteArray,
                off: Int,
                len: Int,
            ) {}
        }
}
