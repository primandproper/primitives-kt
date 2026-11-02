package software.primandproper.primitives.cryptography.encryption.config

import software.primandproper.primitives.cryptography.encryption.EncryptorDecryptor
import software.primandproper.primitives.cryptography.encryption.aes.aesEncryptorDecryptor
import software.primandproper.primitives.cryptography.encryption.salsa20.salsa20EncryptorDecryptor
import software.primandproper.primitives.observability.Logger
import software.primandproper.primitives.observability.NoopLogger
import software.primandproper.primitives.observability.NoopTracerProvider
import software.primandproper.primitives.observability.Observer
import software.primandproper.primitives.observability.TracerProvider

/**
 * Selects and builds an [EncryptorDecryptor] from [config]. Port of platform-go's
 * `config.ProvideEncryptorDecryptor`. The dispatch is exhaustive over [EncryptionProvider]: the
 * provider was already resolved from its string form at the parse edge, so there is no unknown arm.
 *
 * @throws IllegalArgumentException when [config] is null.
 */
public fun EncryptorDecryptor(
    config: EncryptionConfig?,
    key: ByteArray,
    logger: Logger = NoopLogger,
    tracerProvider: TracerProvider = NoopTracerProvider,
): EncryptorDecryptor {
    requireNotNull(config) { "nil config provided" }
    return when (config.provider) {
        EncryptionProvider.AES ->
            aesEncryptorDecryptor(key, Observer("aes_encryptor", logger, tracerProvider))
        EncryptionProvider.SALSA20 ->
            salsa20EncryptorDecryptor(key, Observer("salsa20_encryptor", logger, tracerProvider))
    }
}
