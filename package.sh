#!/bin/bash
set -euo pipefail

APP_NAME="ALSim"
MAIN_CLASS="net.pimenta.alsim.Main"
JAR_NAME="alsim.jar"
DIST="dist"

echo "==> Building..."
mvn clean package dependency:copy-dependencies \
    -DincludeGroupIds=org.openjfx \
    -DoutputDirectory=target/javafx

echo "==> Preparing dist..."
rm -rf $DIST
mkdir -p $DIST

echo "==> Packaging program..."
jpackage \
  --name $APP_NAME \
  --input target \
  --main-jar $JAR_NAME \
  --main-class $MAIN_CLASS \
  --module-path target/javafx \
  --add-modules javafx.controls \
  --type app-image \
  --dest $DIST

echo "==> Copying resources..."
cp -r resources/examples "$DIST/$APP_NAME/"

echo "==> Creating ZIP..."
cd $DIST
zip -r "$APP_NAME-linux-x64.zip" "$APP_NAME"

echo "==> Done:"
echo "    $DIST/$APP_NAME-linux-x86_64.zip"