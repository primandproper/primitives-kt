package software.primandproper.primitives.circuitbreaking.partitioned

import kotlinx.coroutines.test.runTest
import software.primandproper.primitives.circuitbreaking.CircuitState
import kotlin.test.Test
import kotlin.test.assertEquals

class NoopKeyedCircuitBreakerTest {
    @Test
    fun alwaysProceedsForAnyKey() =
        runTest {
            val cb = NoopKeyedCircuitBreaker.forKey("123")
            assertEquals("ok", cb.execute { "ok" })
            assertEquals(CircuitState.CLOSED, cb.state.value)

            // Failures never trip it.
            repeat(50) { runCatching { cb.execute { throw RuntimeException("boom") } } }
            assertEquals(CircuitState.CLOSED, cb.state.value)
        }
}
