#!/bin/sh

echo "[*] Cleaning Yocto build artifacts..."

# Stop if anything fails
set -e

# Delete tmp (build artifacts) and sstate-cache (safe to keep if you want)
rm -rf build/tmp
rm -rf build/work
rm -rf build/cache

# Optionally delete sstate-cache to fully clean
# rm -rf build/sstate-cache

# Clean downloads (only if disk space is a concern)
# rm -rf downloads

# Clean logs
rm -rf build/*/temp
rm -rf build/*/log.*

echo "[+] Clean complete!"

