#!/usr/bin/env sh

cd example-module
../gradlew assembleDebug
cp build/outputs/apk/debug/example-module-debug.apk module.jar
zip example-module.zip manifest.json module.jar
rm module.jar
mv example-module.zip ..
cd ..
