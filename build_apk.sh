#!/bin/bash
# LAB MITM - Build APK
# Usage: bash build_apk.sh

echo "╔═══════════════════════════════════════════════════════════╗"
echo "║  LAB MITM Toolkit - APK Builder                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"

cd /sdcard/lab/apk

# Check if android SDK is available
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo "[!] Android SDK not found"
    echo "[*] Checking common locations..."
    
    if [ -d "/opt/android-sdk" ]; then
        export ANDROID_HOME="/opt/android-sdk"
    elif [ -d "$HOME/Android/Sdk" ]; then
        export ANDROID_HOME="$HOME/Android/Sdk"
    elif [ -d "/usr/local/android-sdk" ]; then
        export ANDROID_HOME="/usr/local/android-sdk"
    fi
    
    if [ -n "$ANDROID_HOME" ]; then
        echo "[+] Found SDK at: $ANDROID_HOME"
    else
        echo "[!] SDK not found. Install Android SDK or use a build service."
        echo "[*] Alternative: Use online build services like:"
        echo "    - https://build.appz.center"
        echo "    - https://www.androidbuildercenter.com"
        exit 1
    fi
fi

# Create output directory
mkdir -p dist

# Check for aapt/aapt2
AAPT=""
if [ -n "$ANDROID_HOME" ]; then
    if [ -f "$ANDROID_HOME/build-tools/34.0.0/aapt2" ]; then
        AAPT="$ANDROID_HOME/build-tools/34.0.0/aapt2"
    elif [ -f "$ANDROID_HOME/build-tools/33.0.0/aapt2" ]; then
        AAPT="$ANDROID_HOME/build-tools/33.0.0/aapt2"
    elif [ -f "$ANDROID_HOME/build-tools/30.0.0/aapt2" ]; then
        AAPT="$ANDROID_HOME/build-tools/30.0.0/aapt2"
    fi
fi

if [ -z "$AAPT" ]; then
    echo "[!] aapt2 not found. Building manually is complex."
    echo "[*] Please use an online APK builder or Termux with Android SDK."
    echo ""
    echo "[*] Quick alternative - Use https://build APK builder:"
    echo "    1. Zip the apk/ folder"
    echo "    2. Upload to an online builder"
    exit 1
fi

echo "[+] APK Builder Ready"
echo "[*] This requires Android SDK tools to fully compile"
echo ""
echo "[*] To build APK on this device, install:"
echo "    pkg install aapt apksigner"
