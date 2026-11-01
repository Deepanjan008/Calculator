# Working on Neo Calculator

This guide exists so that you can open this project, find the file you need, and
change it without asking anyone. It describes how the code is arranged and why,
which rules are checked automatically, and what a good change looks like. If
something here is out of date, that is a bug in the guide - fix it in the same
pull request.

## Before you start

You need JDK 21 and an Android SDK with platform 37 installed. Nothing else:
debug builds need no keystore and no secrets.

```bash
./gradlew assembleDebug         # build the debug APK
./gradlew testDebugUnitTest     # run the JVM test suite
./gradlew lint                  # Android lint
```

Those three commands are exactly what CI runs on your pull request, so if they
pass locally, the build will pass there.

## Where a file goes

The project is split by what a piece of code *knows*, not by what it draws.

| Module | Put things here when they are about | It may depend on |
| :--- | :--- | :--- |
| `:core:domain` | Maths, rules, models, contracts, use cases. No Android import, ever. | nothing |
| `:core:presentation` | The screen vocabulary: state, event, effect, and the holder base class. | nothing |
| `:core:data` | Reading and writing: repositories, preferences, implementations of the domain contracts. | `:core:domain`, `:core:database` |
| `:core:database` | Room: entities, DAOs, the database itself, and its Hilt module. | nothing |
| `:core:ui` | Colours, type, shared composables. | `:core:domain` |
| `:core:navigation` | Routes, and results passed between screens. | `:core:domain` |
| `:feature:<name>` | One screen, or one small group of them. | `:core:domain`, `:core:presentation`, `:core:ui`, `:core:navigation` |

Two rules follow, and they are the ones reviewers actually check:

- **A feature never depends on `:core:data`.** If a screen needs something from
  storage, it needs a use case in `:core:domain` and a contract to go with it.
- **`:core:domain` and `:core:presentation` never import Android.** They are
  plain Kotlin modules so that everything in them can be tested on a laptop
  without an emulator. The day one of them needs `android.*`, the design has
  gone wrong somewhere and it is worth stopping to find where.

## How a screen is built

Every screen has three parts. Small screens have three small parts; that is
fine.

1. **`<Name>Contract.kt`** - one `ScreenState` data class with everything the
   screen draws, one sealed `ScreenEvent` with everything a person can do, and
   one sealed `ScreenEffect` for the things the screen says once (a toast, a
   snackbar). If a screen has nothing to say, its effect type is an empty sealed
   interface - that is honest, not lazy.
2. **`<Name>StateHolder.kt`** - extends `StateHolder`, implements one
   `suspend fun onEvent`, and keeps its actual work in private functions. This is
   where every decision lives: what a key press means, when a value is valid,
   what happens on confirm.
3. **`<Name>Screen.kt`** - a composable that takes the state and an `onEvent`
   lambda and draws it. No logic, no repository, no coroutine that fetches
   anything.

A `ViewModel` may sit in front of the holder, and in this project one does. Its
job is lifetime only: build the holder once, survive a rotation, forward events.
If you find arithmetic, formatting or a rule inside a ViewModel, it belongs one
layer down.

### The habits that keep this pleasant

- Send events, do not call storage. `onEvent(CalculatorUiEvent.KeyPressed("7"))`
  rather than a lambda that reaches into a repository.
- Anything the user can see and the screen must remember goes in the state, not
  in a `remember` flag. A dialog that is open is a fact about the screen.
- If a value can be "not loaded yet", say so in the state. `isEmpty` and
  `hasNotLoaded` are different answers and a screen that confuses them lies to
  the user for one frame.
- Read what you are about to name *before* you destroy it. Once the row is gone,
  "Deleted "Bank"" has nothing left to read the name from.
- One-off messages are effects. Anything in the state is re-read on every
  recomposition, and a toast that fires on every frame is a bug report.

## Tests

Every rule you add gets a test, and the test must not need a device. Build the
state holder with hand-written fakes, call the same entry point the screen calls,
and assert on `state.value`:

```kotlin
val holder = CalculatorStateHolder(/* fakes */)
holder.onEvent(CalculatorUiEvent.KeyPressed("="))
assertEquals("", holder.state.value.preview)
```

Two habits that pay for themselves immediately:

- **Test the refusals.** Not just that `=` gives an answer, but that `=` on an
  unfinished sum records nothing and changes nothing. That is the test that
  catches the next refactor.
- **Write fakes, not mocks.** A fake of a two-method contract is six lines and
  never surprises you at three in the morning.

## The build treats a warning as an error

This is the one rule with no judgement in it. Kotlin compiles with
`allWarningsAsErrors`, Java with `-Werror`, and lint with `warningsAsErrors`, in
every module. A deprecated call, an unused variable, a needless cast - the build
stops.

- **Do not suppress.** No `@Suppress`, no `@SuppressLint`, no `//noinspection`.
  CI greps for all three and fails the pull request.
- **Do not weaken the gate.** Removing `allWarningsAsErrors`, dropping `-Werror`
  or turning off `warningsAsErrors` is not a fix, and CI checks the settings
  themselves before it builds anything.
- **Fix it or delete it.** A warning you cannot fix is usually code you do not
  need. That is not a loss.

If a warning genuinely cannot be fixed - a platform API with no replacement at
our `minSdk` - say so in the pull request and explain why. One documented
exception is a decision; twelve are a habit.

## Sending a change

```bash
git checkout -b fix/preview-after-backspace
# ... make the change ...
./gradlew assembleDebug testDebugUnitTest lint
git commit -m "Clear the preview when a key makes the expression incomplete"
git push -u origin fix/preview-after-backspace
```

Then open a pull request against `main`. Keep it small enough to read in one
sitting; if the description needs two paragraphs of "and also", it is two pull
requests.

**Before you open it:**

- [ ] `assembleDebug`, `testDebugUnitTest` and `lint` pass locally.
- [ ] The new behaviour has a test, including the case it must refuse.
- [ ] Nothing is suppressed and no build setting was loosened.
- [ ] The code sits in the module the table above says it does.
- [ ] The description says what was wrong and why this approach, not just which
      files changed.

## Style

Kotlin official style, four spaces, no wildcard imports. Comments explain why
the code is shaped this way; the code already says what it does. No dead code,
no commented-out blocks, no `TODO` without an issue number behind it. A name
should tell you what the thing is - `data`, `tmp` and `helper` tell you nothing.

## Licence

By opening a pull request you agree your contribution can be distributed under
the licence this project already carries.
