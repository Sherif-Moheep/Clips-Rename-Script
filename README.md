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

Run the included batch wrapper:

```cmd
:: Using positional path (defaults to current folder, depth 1)
rename-clips.bat "C:\Users\YourUser\Videos\Captures"

:: Using named arguments
rename-clips.bat --dir "C:\Users\YourUser\Videos\Captures" --anchor "Valorant"

:: Recursively search all subdirectories
rename-clips.bat --dir "C:\Users\YourUser\Videos\Captures" --recursive

:: Or limit search to a specific depth (e.g., 2 levels)
rename-clips.bat --dir "C:\Users\YourUser\Videos\Captures" --depth 2

:: Preview changes without renaming (dry run)
rename-clips.bat --dir "C:\Users\YourUser\Videos\Captures" --dry-run

:: Run directly inside the current folder
rename-clips.bat
```

### Kotlin Script Directly

```bash
kotlin script.main.kts --dir "C:\Users\YourUser\Videos\Captures" --anchor "Valorant" --recursive --dry-run
```

### CLI Options

| Option / Flag | Short | Default | Description |
| :--- | :--- | :--- | :--- |
| `[directory]` | | `.` | Directory containing clips to rename (positional fallback) |
| `--dir` | `-d` | | Directory containing clips to rename |
| `--anchor` | `-a` | `Valorant` | Anchor text to keep from the original filename |
| `--suffix` | `-s` | `-.mp4` | Suffix artifact to remove |
| `--depth` | `-n` | `1` | Maximum directory depth to search |
| `--recursive` | `-r` | `false` | Recursively search all subdirectories (unlimited depth) |
| `--ignore-case` | `-i` | `false` | Ignore case when searching for the anchor text |
| `--dry-run` | | `false` | Preview renames without modifying files |
| `--help` | `-h` | | Show usage and available options |

## Example Transformation

| State | Filename |
| :--- | :--- |
| **Original NVIDIA Capture** | `Valorant 2024.01.15 - 18.22.10.01.DVR.mp4` |
| **After VLC Trim (Before Script)** | `vlc-record-2024-01-15-18h25m00s-Valorant 2024.01.15 - 18.22.10.01.DVR.mp4-.mp4` |
| **Restored (After Script)** | `Valorant 2024.01.15 - 18.22.10.01.DVR.mp4` |
