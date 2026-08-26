# Clips Rename Script

A lightweight Kotlin script designed to restore the original clip names of gameplay recordings trimmed using VLC Media Player.

## Background & Problem

1. Gameplay highlights are captured using **NVIDIA Instant Replay (ShadowPlay)** (e.g., `Valorant 2024.01.15 - 18.22.10.01.DVR.mp4`).
2. When using **VLC's built-in Record button** to cut specific segments from a 2–3 minute replay, VLC adds its own prefix and suffix formatting to the output file:
   - **VLC Output**: `vlc-record-2024-01-15-18h25m00s-Valorant 2024.01.15 - 18.22.10.01.DVR.mp4-.mp4`
3. This script removes the VLC record prefix and trailing `-.mp4` artifact, restoring the clean original clip name.

## Prerequisites

- [Kotlin CLI](https://kotlinlang.org/docs/command-line.html) installed and accessible in your `PATH`.

## Usage

### Windows (Batch Script)

Run the included batch wrapper with the path to your clips folder:

```cmd
rename-clips.bat "C:\Users\YourUser\Videos\Captures"
```

Or run directly inside the current folder:

```cmd
rename-clips.bat
```

### Kotlin Script Directly

```bash
kotlin script.main.kts "C:\Users\YourUser\Videos\Captures"
```

## Example Transformation

| State | Filename |
| :--- | :--- |
| **Original NVIDIA Capture** | `Valorant 2024.01.15 - 18.22.10.01.DVR.mp4` |
| **After VLC Trim (Before Script)** | `vlc-record-2024-01-15-18h25m00s-Valorant 2024.01.15 - 18.22.10.01.DVR.mp4-.mp4` |
| **Restored (After Script)** | `Valorant 2024.01.15 - 18.22.10.01.DVR.mp4` |
