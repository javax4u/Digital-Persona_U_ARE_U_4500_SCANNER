@echo off
cls
echo REGISTERING THE DIGITALPERSONA U.are.U DEVICE SERVICE
echo.
echo This script can be used to enable/disable DigitalPersona U.are.U UPOS
echo for JavaPOS support in your JavaPOS environment.
echo.
echo Modify the JAVA_POS_CONFIG_PATH variable in this file to point to your 
echo JavaPOS configuration directory.
echo.
echo To register the Device Service, run reigster.bat
echo To unregister the Device Service, run register.bat -u.
echo.

REM check java
set JAVA_BIN=%JAVA_HOME%\bin

if exist "%JAVA_BIN%\java.exe" goto java_ok
echo "Cannot find 'java'. check your JAVA_HOME"
exit /B 1

:java_ok

set JAVA="%JAVA_BIN%\java.exe"

if exist ".\jpos113.jar" (
set DP_JAVAPOS_BIN_DIR=.
set DP_JAVAPOS_SAMPLE_DIR=..\..\Samples\UareUSampleJavaPOS
set LIB_OUT_DIR=.
)

if not exist ".\jpos113.jar" (
set DP_JAVAPOS_BIN_DIR=..\..\..\..\Source\Java\javapos\third-party
set DP_JAVAPOS_SAMPLE_DIR=..\..\..\..\Source\Java\javapos\sample
set LIB_OUT_DIR=..\..\..\..\Windows\lib\java
)

set JAVA_POS_CONFIG_PATH=%DP_JAVAPOS_SAMPLE_DIR%\config

set PATH=%DP_JAVAPOS_BIN_DIR%;%PATH%

set CP="%JAVA_POS_CONFIG_PATH%";"%LIB_OUT_DIR%\dpjavapos.jar";"%DP_JAVAPOS_BIN_DIR%\jpos113.jar";"%DP_JAVAPOS_BIN_DIR%\xercesImpl-2.6.2.jar";"%DP_JAVAPOS_BIN_DIR%\xmlParserAPIs.jar"

%JAVA% -cp %CP% com.digitalpersona.javapos.services.biometrics.Registration %1 %2 %3 %4 %5 %6 %7 %8 %9

pause