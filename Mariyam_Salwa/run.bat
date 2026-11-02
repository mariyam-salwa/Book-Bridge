@echo off
REM Compiles and runs BookBridge on Windows. Double-click this file, or run it in a terminal.
if exist bin rmdir /s /q bin
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
if errorlevel 1 (
    echo.
    echo Compilation failed. Is the JDK installed? Try: javac -version
    del sources.txt
    pause
    exit /b 1
)
del sources.txt
java -cp bin app.BookBridgeApp
