package software.primandproper.primitives.observability.koin

import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import software.primandproper.primitives.observability.Logger
import software.primandproper.primitives.observability.Observability
import software.primandproper.primitives.observability.Observer
import software.primandproper.primitives.observability.ObserverFactory
import software.primandproper.primitives.observability.TracerProvider

/**
 * Registers an already-assembled [Observability] into a Koin graph, exposing the factory, root
 * logger, and tracer provider, plus a parameterized [Observer] lookup by component name.
 *
 * ```
 * startKoin { modules(observabilityModule(myObservability)) }
 * // in a component:
 * val o11y: Observer = get { parametersOf("stream_manager") }
 * ```
 */
public fun observabilityModule(observability: Observability): Module =
    module {
        single { observability }
        single { observability.observers }
        single<Logger> { observability.logger }
        single<TracerProvider> { observability.tracerProvider }
        factory<Observer> { (name: String) -> get<ObserverFactory>().named(name) }
    }

/** Convenience for `get<Observer> { parametersOf(name) }`. */
public fun org.koin.core.scope.Scope.observer(name: String): Observer = get { parametersOf(name) }
