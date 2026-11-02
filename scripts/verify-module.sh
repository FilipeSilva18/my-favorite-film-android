#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "usage: scripts/verify-module.sh <gradle-module-path> [more modules...]" >&2
  echo "example: scripts/verify-module.sh :feature:movies:presentation" >&2
  exit 64
fi

cd "$(dirname "$0")/.."

tasks=(assertModuleGraph :architecture-test:unitTest)
for module in "$@"; do
  [[ "$module" == :* ]] || module=":$module"
  tasks+=("$module:unitTest")
done

./gradlew "${tasks[@]}" --continue
