# Etabli Plume

> A fast offline reference for typesetting commands.

`iOS` `Android` · Apache-2.0 · Part of the [Etabli Suite](https://github.com/etabli-dev)

> **Note:** Renamed from EtabliTeX to avoid implying affiliation with TeX/LaTeX (whose names are governed by Knuth's TeX license and the LPPL).

Etabli Plume is a categorized reference and snippet picker for LaTeX typesetting commands: commands, Greek letters, math operators, formatting, environments and bibliography, with a preview pane and favorites. Offline-only, no dependencies. (Not affiliated with the LaTeX Project or TeX.)

## Availability

- **App Store (iOS):** available.
- **Google Play:** available.
- **F-Droid (main repo):** built from this repo's `/android` source.

## Privacy

No analytics. No third-party SDKs. No accounts. Credentials, where needed, live only in the platform secure store (iOS Keychain / Android EncryptedSharedPreferences). This app is fully offline.

## Repository layout

```
ios/        SwiftUI app
android/    Kotlin + Jetpack Compose app
fastlane/   F-Droid / store listing metadata
```

Both platforms are one product, sharing the Coder Design System tokens.

## Tech

iOS: SwiftUI. Android: Compose, bundled dictionary JSON

**Status:** Complete on both platforms

## Support development

- 💚 **[Liberapay](https://liberapay.com/rabanheller/)** — recurring, 0% commission, shown on F-Droid.
- ☕ [Buy Me a Coffee](https://buymeacoffee.com/rabanheller) — one-off tip (also the in-app link on iOS/Android).

## License

Apache License 2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE).

Copyright 2026 Raban Heller.
