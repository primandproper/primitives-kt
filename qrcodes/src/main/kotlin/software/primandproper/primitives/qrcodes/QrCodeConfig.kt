package software.primandproper.primitives.qrcodes

import software.primandproper.primitives.observability.Logger
import software.primandproper.primitives.observability.NoopLogger
import software.primandproper.primitives.observability.NoopTracerProvider
import software.primandproper.primitives.observability.TracerProvider

/** The default QR-code PNG size (width and height in pixels), matching platform-go's hard-coded `barcode.Scale(256, 256)`. */
public const val DEFAULT_QR_SIZE: Int = 256

/**
 * QR-code builder configuration. platform-go's `qrcodes` package has no config struct — the issuer is
 * a DI-provided `Issuer` and the image size is hard-coded — so this groups the two tunables a caller
 * sets when wiring a [QrCodeBuilder], mirroring how `:cache-api` exposes a portable `CacheConfig`.
 *
 * @param issuer the service name embedded in the `otpauth` label and `issuer` query parameter.
 * @param size the generated PNG's width and height in pixels; defaults to [DEFAULT_QR_SIZE].
 */
public data class QrCodeConfig(
    val issuer: String,
    val size: Int = DEFAULT_QR_SIZE,
) {
    /** Builds the production ZXing-backed [QrCodeBuilder] from this config. */
    public fun newBuilder(
        logger: Logger = NoopLogger,
        tracerProvider: TracerProvider = NoopTracerProvider,
    ): QrCodeBuilder = DefaultQrCodeBuilder(issuer, size, logger, tracerProvider)
}
