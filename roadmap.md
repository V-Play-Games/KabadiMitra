Based on the actual screens in the zip (login → home/Bhav Board → lot capture → estimate → buyer match → QR/OTP handover → receipt → hisaab → suraksha, plus the recycler/admin web screens), here's a build roadmap scoped specifically to the Kotlin/Compose collector app, with the portal/backend treated as the API it talks to.

## 1. Tech stack

| Layer | Choice | Why |
|---|---|---|
| UI | Jetpack Compose + Material 3, single `:app` module | No multi-module overhead for a 6-person/36-hour build |
| Architecture | MVVM + UDF, Repository pattern, package-by-feature | Fast to parallelize across teammates |
| DI | Hilt | Standard, low boilerplate |
| Local DB | Room (KSP) | Matches PRD's offline-first mandate |
| Async | Coroutines + Flow | — |
| Networking | Retrofit + OkHttp + kotlinx.serialization | Talks to the FastAPI backend |
| Background sync | WorkManager, outbox table | Survives days offline per PRD §7 |
| Camera | CameraX | Lot photo + scale photo |
| On-device ML | TFLite (MobileNetV3 classifier), ML Kit Text Recognition (scale OCR), ML Kit Barcode Scanning / ZXing (QR) | Matches PRD §7 exactly |
| Signing | **Google Tink** (Ed25519), not AndroidKeyStore | See risk note below — AndroidKeyStore's hardware provider doesn't support Curve25519/Ed25519, only RSA/EC; a software library is required |
| Audio | Plain `MediaPlayer` for short pre-recorded clips | Media3/ExoPlayer is overkill for ~60 short clips |
| Prefs | DataStore | Language choice, PIN, device keypair alias |
| Min/target SDK | minSdk 24, target/compile 34–35 | Covers the 2 GB RAM budget-phone demographic |

## 2. Package structure

```
com.kabadimitra.collector
├── core/
│   ├── designsystem/     Theme, Color, Type (bundle Noto Sans Devanagari — default
│   │                     system font renders Marathi/Hindi inconsistently across OEMs),
│   │                     components/ (KmCTAButton, StatusChip, TrendArrow, VoiceIconButton,
│   │                     BottomNavBar)
│   ├── audio/            AudioClipPlayer, ClipRegistry (screen → clip id map)
│   ├── camera/           CameraXController, ImageCompressor (target ~100 KB)
│   ├── ml/               MaterialClassifier (TFLite), ScaleOcrReader (ML Kit)
│   ├── crypto/           RecordSigner (Tink Ed25519), KeypairStore (DataStore-backed)
│   ├── location/         LocationProvider (FusedLocationProviderClient)
│   ├── network/          ApiService, NetworkMonitor
│   └── sync/             OutboxDao, SyncWorker, ConflictResolver
├── data/
│   ├── local/            AppDatabase, entities/, dao/
│   ├── remote/           DTOs
│   ├── repository/       LotRepository, PriceRepository, RecyclerRepository,
│   │                     LedgerRepository, CollectorRepository
│   └── di/                Hilt modules
├── ui/
│   ├── navigation/        KmNavHost, Routes
│   ├── onboarding/        language + phone + OTP
│   ├── home/               Bhav Board home
│   ├── priceboard/         full rates tab
│   ├── lot/capture/        step 1: photo + classify + weight
│   ├── lot/estimate/       step 2: price + Jalao Mat Kamao + pickup location
│   ├── buyers/              Sahi Kharidar list + pooling banner
│   ├── handover/           Pakki Parchi: QR + OTP
│   ├── receipt/            verified receipt
│   ├── hisaab/              ledger
│   ├── safety/              Suraksha cards
│   └── settings/            PIN lock, language change
└── worker/
```

## 3. Screen → implementation map (from your actual designs)

| Screen | Composable | Key build items | Data source |
|---|---|---|---|
| Onboarding | `OnboardingScreen` | Language pill row, phone field, custom OTP box row, persistent audio-hint banner | `CollectorRepository` local-first, phone+OTP against backend when online |
| Home / Bhav Board | `HomeScreen` | Greeting header, speaker+bell icon buttons, orange "कबाड़ बेचें" CTA card, live rate list w/ trend arrows, price-alert banner | `PriceRepository.observeTodayRates()` (Flow from Room, backed by sync) |
| Lot capture (step 1) | `LotCaptureScreen` | CameraX capture w/ dashed-frame overlay, "फोटो ली गई" badge, classifier suggestion chip + confidence %, Yes/"बदलें" override, **custom Compose numeric keypad** (not system IME — deliberate for low-literacy/offline reliability) | `MaterialClassifier`, draft `Lot` in Room |
| Lot estimate (step 2) | `LotEstimateScreen` | Summary chips, big ₹ estimate + formula subtext, "Jalao Mat, Kamao" dual-bar comparison + warning banner, editable GPS pickup card | Pricing engine (weight × 14-day median), `LocationProvider` |
| Sahi Kharidar (buyers) | `BuyersScreen` | Ranked recycler cards (CPCB badge, best-offer badge, payout, pickup slot, select), bottom pooling banner | `RecyclerRepository.matchRecyclers(lot)` — rule-based, no ML |
| Handover | `HandoverScreen` | Status timeline, QR (encodes signed lot payload), Lot ID chip, 4-digit OTP with speaker | `RecordSigner` (Ed25519), ZXing QR gen |
| Receipt | `ReceiptScreen` | Checkmark, ₹ total, payment-method chip, detail table, "confirmed by both" trust badge, Share + "हिसाब देखें" | Signed `Transaction` entity, Android share intent |
| Hisaab | `HisaabScreen` | Month summary card (+% vs last month), weekly bar chart (plain Compose `Canvas` — skip a charting lib to protect APK size), transaction list w/ मिला/बाकी chips | `LedgerRepository` aggregate query |
| Suraksha | `SurakshaScreen` | 5 colour-coded safety cards, tap-to-play audio | Static content + `AudioClipPlayer` |

Bottom nav (Home / Bhav / Hisaab / Suraksha) is a persistent `Scaffold`; the lot→buyers→handover→receipt chain is a pushed full-screen stack reached from the Home CTA.

## 4. Data layer (Room, sketch)

```kotlin
@Entity data class Lot(
    @PrimaryKey val id: String,       // "KM-2026-0417"
    val collectorId: String,
    val materialCategory: String,
    val weightKg: Double,
    val photoPath: String,
    val classifierConfidence: Float?,
    val estimateRupees: Int,
    val pickupLat: Double, val pickupLng: Double,
    val status: LotStatus,            // DRAFT, OFFERED, ACCEPTED, HANDED_OVER, SYNCED
    val createdAt: Long
)

@Entity data class Transaction(
    @PrimaryKey val lotId: String,
    val recyclerId: String,
    val finalWeightKg: Double,
    val finalRatePerKg: Int,
    val paymentMethod: String,        // CASH, UPI
    val collectorSignature: String,
    val recyclerSignature: String?,
    val gpsMatchMeters: Int?,
    val handoverTimestamp: Long
)

@Entity data class PriceEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val material: String, val area: String,
    val ratePerKg: Int, val date: Long
)
```
Plus `Recycler`, `Collector`, `LedgerEntry`, `OutboxItem` (payload + retry count for `SyncWorker`).

## 5. Phased build plan

Mapped onto the PRD's 36-hour window, but broken into Android-specific tasks so it's actually assignable:

**Hours 0–3 — Scaffolding**
Repo, version catalog, Hilt, Compose theme matching the mockups' navy/orange/green palette, Devanagari font bundled, empty `NavHost` with all 9 routes stubbed, Room schema created.

**Hours 3–8 — Static screens on mock data (parallel, UI owner)**
Build all 9 Composables against a Room database pre-seeded from a fixture JSON, with taps wired to real navigation. This unblocks demo-able UI before ML/sync land.

**Hours 6–14 — Offline data + pricing engine (parallel, storage/backend owners)**
Repositories, DataStore prefs, price-median + trend calc, "Jalao Mat, Kamao" degradation-factor lookup, rule-based recycler matching sorted by net payout.

**Hours 8–18 — Camera + on-device ML (parallel, ML owner)**
CameraX wired into `LotCaptureScreen`, TFLite classifier (~4 MB model in `assets/`), confidence-threshold UI logic, ML Kit OCR for scale-photo weight reading as an alt-input next to the manual keypad, image compression pipeline.

**Hours 14–22 — Sync + networking**
Retrofit `ApiService`, outbox + `WorkManager`, `NetworkMonitor`-driven offline banner. Since the recycler's accept happens async on the portal, decide now: poll-on-resume/periodic `WorkManager` pull vs. adding FCM — FCM is more correct but adds a Play Services dependency the team should sign off on explicitly.

**Hours 18–26 — Signing + handshake + receipt**
Tink-based Ed25519 keypair (see risk below), record-hash construction, QR generation, OTP flow, dual-signed local record, `ReceiptScreen` render + share intent.

**Hours 20–28 — Voice layer (parallel)**
Bundle the ~60 recorded clips, `AudioClipPlayer`, wire every speaker icon plus auto-hint banners. IVR/missed-call is backend+telephony, out of this app's scope.

**Hours 24–30 — Ledger, safety, pooling polish**
Hisaab chart + list, Suraksha cards, pooling banner (P1/stretch).

**Hours 28–34 — Hardening**
2 GB RAM device/emulator test, airplane-mode full Flow-A run, R8/shrink pass, vector drawables not PNGs, APK size check against the <15 MB target, TalkBack spot-check, ≥48dp tap targets.

**Hours 34–36 — Demo buffer**
Pre-seed 2–3 known-good demo lots, record a backup video of the full flow.

**Post-hackathon (v1.1+)** — aggregator mode, Kamai Passport export, FCM push, Crashlytics, GitHub Actions CI (lint + unit tests + `assembleRelease`), instrumented UI tests, modularize only if the team grows past this sprint.

## 6. Testing tied to the PRD's own success metrics

| PRD metric | Android-side test |
|---|---|
| Lot creation <3 min, no help | Compose UI test with a timer + real usability session, 5 non-technical users |
| >80% task success w/o reading | Same session, track completion without touching audio buttons |
| 100% offline completion | Instrumented test toggling airplane mode via `adb`, run Flow A end-to-end |
| APK <15 MB / runs on 2 GB RAM | `assembleRelease` + size check; run on an Android Go / 2 GB emulator profile |
| Price estimate ±15% | Unit tests on the pricing engine against fixture price data |

## 7. Risks worth flagging to the team now

- **Ed25519 signing**: I checked — AndroidKeyStore's hardware-backed provider only supports RSA and EC (NIST curves); it explicitly rejects Curve25519/Ed25519 key generation. Plan on Tink (or BouncyCastle) for the actual signing, with the private key stored in app-private storage (optionally AES-wrapped via an AndroidKeyStore key) rather than assuming Keystore hardware backing for Ed25519 directly.
- **Custom keypad, not system IME**: matches the design and avoids autocorrect/locale keyboard issues on low-literacy, low-end devices — worth locking in early since retrofitting later touches every numeric field.
- **Push vs. poll for recycler acceptance**: the demo script needs the recycler's "Accept" to reach the collector's phone live; decide early whether that's an FCM dependency or a WorkManager poll, since it affects the sync module's shape.
