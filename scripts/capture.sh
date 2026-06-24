#!/usr/bin/env bash
# capture.sh — deterministic screenshot harness for etabli-plume (v0.1.0)
set -euo pipefail
DBG=com.raban.etabli.tex.debug
OUT="$(cd "$(dirname "$0")/.." && pwd)/vignettes/assets/0.1.0"; mkdir -p "$OUT"
cap(){ for t in 1 2 3; do adb exec-out screencap -p > "$OUT/$1.png"; [ "$(wc -c < "$OUT/$1.png")" -gt 1000 ] && break; sleep 1; done; echo "  + $1.png"; }
nav(){ adb shell input tap "$1" 2225; sleep 0.9; }
adb shell am force-stop "$DBG"; adb shell monkey -p "$DBG" -c android.intent.category.LAUNCHER 1 >/dev/null 2>&1; sleep 4
cap 01-dictionary
adb shell input tap 338 417; sleep 0.8; cap 02-filter-greek
adb shell input tap 970 857; sleep 0.6; adb shell input tap 100 417; sleep 0.6
nav 678; cap 03-favorites
nav 398; cap 04-cheatsheet
nav 950; cap 05-about
echo "Captured $(ls "$OUT"/*.png|wc -l) frames"
