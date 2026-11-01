package com.nexuralabs.calculator.core.presentation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Everything a screen thinks, with none of what it draws.
 *
 * A subclass owns one state value, one channel of effects and one way in:
 * [onEvent]. The screen reads [state] and sends events; it never mutates the
 * state, and it never decides anything.
 *
 * [onEvent] is `suspend` on purpose. A holder that starts its own coroutines
 * needs a dispatcher, a scope and a way to wait for quiet before a test can
 * assert anything; a holder with a suspend entry point is driven by `runBlocking`
 * in a test and by the host's scope in the app, and both see identical code with
 * no scheduling in between.
 *
 * Effects travel on a buffered channel rather than in the state: an effect that
 * arrives while the screen is recomposing should wait rather than vanish, and an
 * effect already shown must not be shown again on the next frame.
 */
abstract class StateHolder<S : ScreenState, E : ScreenEvent, F : ScreenEffect>(initial: S) {

    private val current = MutableStateFlow(initial)

    /** The picture as it stands. Read by the screen, replaced by [update]. */
    val state: StateFlow<S> = current.asStateFlow()

    private val outbox = Channel<F>(Channel.BUFFERED)

    /** One-off messages, each delivered exactly once. */
    val effects: Flow<F> = outbox.receiveAsFlow()

    /** The value a handler is about to change. */
    protected val currentState: S get() = current.value

    /** Replaces the picture with [transform] of the current one. */
    protected fun update(transform: (S) -> S) {
        current.value = transform(current.value)
    }

    /** Says something once. */
    protected suspend fun emit(effect: F) {
        outbox.send(effect)
    }

    /** The single way in. */
    abstract suspend fun onEvent(event: E)
}
