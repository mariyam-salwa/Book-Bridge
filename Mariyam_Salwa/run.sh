#!/bin/sh
# Compiles and runs BookBridge on macOS / Linux:  sh run.sh
rm -rf bin && mkdir bin
javac -d bin $(find src -name "*.java") || { echo "Compilation failed. Is the JDK installed? Try: javac -version"; exit 1; }
java -cp bin app.BookBridgeApp
