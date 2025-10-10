#!/bin/bash
# Script to extract project version from gradle.properties
# Usage: ./scripts/get-version.sh

VERSION=$(grep "^project.version=" gradle.properties | cut -d'=' -f2)

if [ -z "$VERSION" ]; then
    echo "Error: Could not find project.version in gradle.properties" >&2
    exit 1
fi

echo "$VERSION"
