#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

if [[ "${1:-}" == "--quick" ]]; then
  ./gradlew assertModuleGraph :architecture-test:unitTest --continue
  exit 0
fi

python3 scripts/check-docs.py

tasks=(assertModuleGraph :architecture-test:unitTest unitTest lintDebug)
if [[ -n "$(find . -path '*/src/test/screenshots/*.png' -not -path '*/build/*' -print -quit)" ]]; then
  tasks+=(verifyRoborazziDebug)
fi

./gradlew "${tasks[@]}" --continue
