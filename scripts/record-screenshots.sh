#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

if [[ $# -ge 1 ]]; then
  module="$1"
  [[ "$module" == :* ]] || module=":$module"
  ./gradlew "$module:recordRoborazziDebug"
else
  ./gradlew recordRoborazziDebug
fi

if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo
  echo "Changed screenshot references (review each one before committing):"
  git status --porcelain -- '*/src/test/screenshots/*.png' || true
fi
