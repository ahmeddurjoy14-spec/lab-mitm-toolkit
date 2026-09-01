# LAB MITM Toolkit - Build Guide

## Quick Build (Online)

APK build করতে এই ধাপগুলো follow করো:

### Method 1: Online Builder (Easiest)

1. `/sdcard/lab/apk/` folder টা zip করো
2. এই sites গুলোর একটাতে upload করো:
   - https://build.appz.center
   - https://www.androidbuildercenter.com
   - https://www.buildcenter.com

### Method 2: Termux Build

```bash
# Termux এ install করো
pkg update && pkg upgrade
pkg install aapt apksigner

# Build করো
cd /sdcard/lab/apk
bash build_apk.sh
```

### Method 3: GitHub Actions (Automatic)

GitHub তে repo তুলো, CI/CD automatically APK build করবে।

## Files Structure

```
/sdcard/lab/apk/
├── AndroidManifest.xml
├── build_apk.sh
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   └── activity_scan.xml
│   ├── values/
│   │   ├── colors.xml
│   │   ├── strings.xml
│   │   └── styles.xml
│   ├── drawable/
│   │   ├── ic_launcher_background.xml
│   │   └── ic_launcher_foreground.xml
│   ├── mipmap-anydpi-v26/
│   │   └── ic_launcher.xml
│   └── xml/
│       └── network_security.xml
└── src/com/lab/mitm/
    ├── MainActivity.java
    ├── ScanActivity.java
    ├── TrafficActivity.java
    ├── ProxySetupActivity.java
    └── SettingsActivity.java
```

## App Features

- 🔍 Network Scanner (Ping sweep)
- 📡 Traffic Monitor (Real-time bandwidth)
- 🔒 Proxy Setup Guides
- 🌐 VPN MITM Instructions
- 💻 ADB Proxy Guide

## Permissions Required

- INTERNET
- ACCESS_WIFI_STATE
- CHANGE_WIFI_STATE
- ACCESS_NETWORK_STATE
- ACCESS_FINE_LOCATION
- ACCESS_COARSE_LOCATION
