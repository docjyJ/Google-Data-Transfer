@echo off
if exist src/main/resources/client_secret.json (
    gradlew.bat build
    java -jar build/libs/GoogleTransfer-BETA.jar
	pause
	exist
)
echo no client_secret.json found in src/main/resources/
pause
exist
