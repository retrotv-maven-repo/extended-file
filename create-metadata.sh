#!/bin/bash

./gradlew clean
./gradlew build --write-verification-metadata sha256 --refresh-dependencies
