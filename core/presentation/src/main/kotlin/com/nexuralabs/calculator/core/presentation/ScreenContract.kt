package com.nexuralabs.calculator.core.presentation

/**
 * The vocabulary a screen and its state holder share.
 *
 * There are three words, and the reason there are three rather than one is that
 * a screen has three different kinds of thing to deal with:
 *
 * - [ScreenState] is what it draws. One immutable value, replaced whole. If two
 *   parts of the screen could disagree about it, they would.
 * - [ScreenEvent] is what a person did. One type per action, sealed, so the
 *   holder's `when` is exhaustive and a new action is a compile error until it
 *   is handled.
 * - [ScreenEffect] is what the screen says once and forgets - a toast, a
 *   snackbar, a navigation. Kept out of the state because state is re-read on
 *   every recomposition and a message is not.
 *
 * None of these carry behaviour. This module has no Android in it, which is the
 * point: the contract can be reasoned about, and tested, on a plain JVM.
 */

/** One immutable picture of a screen. */
interface ScreenState

/** One thing a person can do to a screen. */
interface ScreenEvent

/** One thing a screen says once. */
interface ScreenEffect
