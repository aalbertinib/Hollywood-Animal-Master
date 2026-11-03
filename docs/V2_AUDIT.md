# Hollywood Animal Master – V2 audit & redesign

Branch: `feature/v2-redesign` (from `tech/new-ui`). Date: 2026-11-02. Game version checked: 0.8.73EA.

## 1. Logic audit (V1 = `main` 1.0.0, also present on `tech/new-ui`)

| # | Finding | Impact | V2 |
|---|---------|--------|----|
| L1 | Weeks 3–8 decayed `(commercial × 1000 − own)` instead of subtracting own screenings each week from the decayed demand. | Over-rents by `own × (1 − 0.8ⁿ)` per week. With 5.0 commercial and 3,200 own screenings V1 says rent 1,440 / 1,152 / 921 / 737 / 589 / 471 in weeks 3–8; the correct values are 800 / 0 / 0 / 0 / 0 / 0. | Fixed: own screenings are subtracted every week (`DistributionPlanner`). |
| L2 | The "with overrides" path used the correct per-week formula, the default path didn't (L1). Setting any override silently changed every other week. | Results jump when you touch one week. | Fixed: one code path. |
| L3 | An own-screenings override above the base value was silently rejected. | Can't model buying cinemas mid-run. | Any value ≥ 0 is accepted. |
| L4 | Loading a saved movie put `3200.0` in an integer-only field. The next keystroke dropped the dot, giving `32000`. A pasted `3,200` became `3.2`. This is likely the "can't set a point" comment on the Steam guide. | Wrong inputs after load or paste. | `NumberField` shows `3200`, drops grouping in integer fields, and accepts `,` or `.` as the decimal separator. Tests: `NumberFieldFilterTest`. |
| L5 | Auto-save "conflict" compared only score and screenings by title and ignored overrides. "Save as" was triggered by renaming. | Confusing dialogs and duplicated entries. | Replaced with upsert by id, auto-save once a movie is saved, Duplicate, and Undo on delete. |
| L6 | `Double.toFixed` used `toString()`, which can switch to scientific notation. | Wrong text for large values. | `toPlainString` never uses scientific notation. |

## 2. What the community and patch notes say (researched 2026-11-02)

No official formula exists. Weappy has not published one, and players say demand also depends on ads, holidays, competition, scandals and unresolved issues.

| Source | Date | Method |
|---|---|---|
| SomeGuyOnPhone, Steam guide (the source of V1) | Oct 2025 | Week 1 = commercial × 2000, week 2 = × 1000, then −20 %/week, 8 weeks |
| nosavynada | Dec 2025 | Commercial × 2 base (+ bonuses), then −⅓ per week. Weeks 9–11 halve. |
| jijilechat (attendance report) | Feb 2026 | Typical attendance by week: 90 % → 45 % → 20 % → 15 % |
| Judge Mental, "how many screenings?" | Apr 2026 | avg(commercial, artistic) × 2000. W2 = W1/2 + 1000. Then halve. Holiday: +3–4k in week 1 only. Use Setup distribution, not Extend. |
| Judge Mental, distribution phase thread | Jun 2026 | Same curve, example 10,000 / 6,000 / 3,000 / 1,500 / 1,250 / 625 / 300 / 150. "Do not rent cinemas in weeks 5–8." |
| Voice From Beyond | Apr 2025 | Screenings actually needed = booked × attendance %, as shown on the results screen. |

Patch notes relevant to distribution (Steam news API):
- 0.8.50.5EA (Aug 2025): holiday attendance boost +50 %. Scandals hurt attendance less.
- Christmas Update (Dec 2025): Christmas added to the release calendar.
- 0.8.67EA (Jan 2026): slightly more distribution income. Rivals buy cinemas more aggressively.
- 0.8.71EA (May 2026): cinema maintenance is no longer free.
- 0.8.72EA / 0.8.73EA (Aug / Oct 2026): no distribution change.
- Act 2 closed beta (from 8 Dec 2026): studios lose their cinemas and distributors appear. Every method here will need revisiting.

**Conclusion: what is "best"?** Everything published after the guide V1 used reports a steeper drop than −20 %/week. V2 therefore defaults new movies to **Fast decay** (Judge Mental's curve). The most efficient method is **calibration**: after each week, enter the game's Screenings and Attendance. The planner then rescales the remaining weeks to the measured audience (screenings × attendance ÷ forecast). Sold-out weeks (≥ 97 %) never scale the forecast down. Saved V1 movies keep the Steam guide method, so their numbers only change by the L1 fix.

## 3. Code and design audit (`tech/new-ui`)

- **Web build broken**: `KoinInit` used JVM-only `@Volatile` / `synchronized`, so `compileKotlinWasmJs` failed. Fixed with `GlobalContext.getOrNull()`.
- **Incomplete M3 schemes**: the 10 theme presets set about 20 of the 36+ color roles. `surfaceContainer*`, `surfaceBright/Dim`, `outlineVariant` etc. fell back to baseline purple in every preset. The theme also hand-animated 37 colors. Replaced with full schemes generated from a seed (MaterialKolor 4.0.5, Material Color Utilities), with 6 seeds × 5 palette styles × 3 contrast levels and Android 12+ wallpaper color.
- **Dead code**: a `desktopMain` source set that no target compiles. `main` also carries `.backup` files and a template `Greeting.kt`.
- **Icons**: `main` used emoji as icons (`EmojiIcon`, `TextIcon`), so screen readers read them. V2 uses Material Symbols, and icon-only buttons have labels and tooltips.
- **Strings**: split between a `Strings` object and resources. V2 puts all strings in `composeResources/values/strings.xml`, ready for translations.
- **Architecture**: three ViewModels kept in sync by hand. V2 has one `PlannerViewModel` (single state + one-off messages), one `SettingsViewModel`, and use cases reduced to the ones actually used.

## 4. V2 UI

- Adaptive `NavigationSuiteScaffold`: bottom bar on phones, navigation rail on wide windows. Destinations: Plan, Library, Guide, Settings.
- **Plan**: movie inputs, method picker (with a custom curve), summary KPIs, stacked bar chart (own vs to rent; hover or tap a bar for its numbers), and week cards grouped as Initial run (1–4) and Extension (5–8/12). Each week card holds the own-screenings override and the actual results that drive calibration.
- **Library**: search, open, duplicate, delete with Undo, and delete all (with confirmation).
- **Guide**: weekly routine, methods compared, patch notes, sources.
- **Settings**: theme mode, seed palette, palette style, contrast, wallpaper colors (Android 12+), always-on-top and window reset (desktop).
- Two panes from 840 dp. Content max width 720 dp on single column. Tabular figures for all numbers.

## 5. Design & layout audit (round 2, checked at 375 px and 1280 px)

| Finding | Fix |
|---|---|
| ⅓, → and ≈ render as boxes on Web: Compose's bundled font has no fallback. | Replaced in strings. `StringGlyphTest` (jvmTest) fails if an unsupported character comes back. |
| Methods had no link to where they come from. | Each method row and the Guide have a **Source** link (Steam guide, Judge Mental's thread, nosavynada's thread). |
| Only the small chevron opened a week. | The whole header row toggles, with a 48 dp+ target and an expanded/collapsed state for screen readers. |
| No way to open a whole group. | Each group header (weeks 1–4, 5–8/12) has **Expand all / Collapse all** and its rent subtotal. Open weeks survive navigation. |
| On phones the results start about two screens below the inputs. | A summary strip ("To rent … · peak …") is pinned under the top bar until the plan scrolls into view, with a **View plan** jump. |
| A "Forecast" tag on every week was noise. | Only **Calibrated** / **Ran** / **Own changed** weeks are tagged. |
| The selected method was only shown by a radio dot. | The selected row has a tinted container. |
| The artistic hint sat under both score fields, misaligned. | Each score field carries its own supporting text (range / "Optional"). |
| The chart hid own capacity left empty. | A dashed own-capacity line per week, capped so a large cinema chain doesn't flatten the bars. The legend wraps on phones. |
| Rail items touched the top edge. | Custom rail/bar (same M3 adaptive rule) with header spacing. |
| No separation when content scrolled under the top bar. | Pinned scroll behavior: the bar tints to `surfaceContainer` while content is underneath. |
| The plan pane stretched on very wide windows. | Plan pane max width 880 dp, centered. |

## 6. Full audit, round 3 (data, state, tests), 2026-11-03

| # | Area | Finding | Fix | Test |
|---|------|---------|-----|------|
| R1 | Data | One entry that fails to decode made the whole library load as empty, and the next save wrote that empty list back: **every saved movie was lost**. Present since V1 (`main`: `MovieResultRepository.loadMovieResults` catches and returns an empty list; `saveMovieResult` writes it back). | Each movie is decoded on its own; unreadable text is copied to `movies_unreadable_backup` before anything can overwrite it. An empty library (`{}`) still counts as readable. | `MovieResultRepositoryTest` |
| R2 | Data | Desktop and Android wrote `movies.json` in place: a crash mid-write left half a file, which then triggered R1. | Desktop writes a temp file and moves it over; Android uses `AtomicFile` (minSdk 24 has no `java.nio.file`). | Build only |
| R3 | State | Deleting the open movie within 600 ms of an edit: the pending auto-save wrote it back under a **new id**, so the deleted movie reappeared. Same after "Delete all". | Delete / clear cancel the pending save, and an auto-save only writes when the open movie still has the id it was scheduled for. | `PlannerViewModelTest` |
| R4 | State | Edit a saved movie, then New / Open / Duplicate within 600 ms: the edit was **dropped**. | The pending save is written immediately instead of being cancelled. A mutation check (putting back the old cancel) makes two tests fail. | `PlannerViewModelTest` |
| R5 | State | New or Open on an unsaved draft discarded it silently. | Snackbar with **Undo**. New on an already empty draft does nothing, so a double press can't push the Undo away (found while testing in the browser). | `PlannerViewModelTest`, `AppUiTest` |
| R6 | State | Undo after deleting the open movie restored the last saved copy and dropped what was typed after. | Undo saves the draft on screen under its original id. | `PlannerViewModelTest` |
| R7 | Logic | A sold-out week (≥ 97 %) reset calibration to ×1 (raw formula), even when an earlier week had measured the audience lower. | Sold out is a lower bound: keep `max(measured, previous estimate)`. | `DistributionPlannerTest` |
| R8 | A11y | The chart description always said the peak is "in week 1"; with calibration or real results it can be another week. | Peak week is computed. | Build only |
| R9 | A11y | The open movie in the Library was marked by tint only. | "Open" tag. | `AppUiTest` |
| R10 | Copy | "This removes 1 saved movies". | Plurals (`library_clear_body`, `msg_exported`). The dialog also suggests exporting first. | Build only |
| R11 | Desktop | No minimum window size (layout collapses below phone width); "Reset window size" left an empty file that threw on the next launch. | Minimum 360 × 560; a blank settings file means default size. | Build only |
| F1 | Feature | Web saves live in localStorage (erased with site data) and there was no way to move a library between desktop, phone and browser. | **Library → Export library** copies JSON to the clipboard; **Import library** (also on the empty Library screen) merges it, newest copy of each movie wins. | `MovieResultRepositoryTest`, `PlannerViewModelTest`, `AppUiTest` |

**Tests.** 19 → 59, all on the JVM (`./gradlew :composeApp:jvmTest`):

- Repositories run against an in-memory `KeyValueStore`; `StorageProvider` now implements that interface.
- `PlannerViewModelTest` drives auto-save timing with `kotlinx-coroutines-test`.
- `PlanScreenUiTest` (Compose UI test) covers row click, Expand/Collapse all, typing `6,5`, and the out-of-range error.
- `AppUiTest` runs the whole app on in-memory storage: New → Undo, delete → Undo, import.

The JS / Wasm test tasks compile but were not run (they need a Karma browser).

**Not changed, on purpose.**

- Ctrl+S / Ctrl+N shortcuts: in a browser these are the browser's own shortcuts. I haven't checked whether Compose for Web can claim them.
- `rememberPlainTooltipPositionProvider` is deprecated in Material 3 1.9. I haven't checked the replacement's signature, so I left it.

## 7. Not verified

- iOS was not compiled (needs macOS / Xcode).
- The Android APK was compiled, but not run on a device; the `AtomicFile` storage path has no device test.
- The browser pane reported the page as hidden during round 3, so on-screen checks there were limited. The New → Undo and import flows were verified through `AppUiTest` instead.
- The Fast decay and calibration numbers come from community reports, not from the game's code.
