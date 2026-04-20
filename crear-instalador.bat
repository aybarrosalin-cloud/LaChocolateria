@echo off

echo =============================================
echo   Chocolateria - Crear Aplicacion Windows
echo =============================================
echo.

:: Validar JAVA_HOME
if not defined JAVA_HOME (
    echo ERROR: JAVA_HOME no configurado
    echo Por favor, instala Java JDK 17+ o configura JAVA_HOME
    pause
    exit /b 1
)

echo Usando Java en: %JAVA_HOME%

:: Verificar que jpackage existe
set "JPACKAGE=%JAVA_HOME%\bin\jpackage.exe"
if not exist "%JPACKAGE%" (
    echo ERROR: No se encuentra jpackage.exe en %JPACKAGE%
    echo Asegurate de usar JDK 17+ (no JRE)
    pause
    exit /b 1
)

echo [1/3] Compilando...
call mvnw.cmd clean package -DskipTests
if errorlevel 1 (
    echo ERROR en compilacion
    pause
    exit /b 1
)

:: Buscar el JAR generado
for %%f in (target\*.jar) do set "JAR_FILE=%%f"
if not defined JAR_FILE (
    echo ERROR: No se encontro ningun JAR en target\
    pause
    exit /b 1
)
echo JAR encontrado: %JAR_FILE%

echo.
echo [2/3] Creando app...

:: Crear directorio si no existe
if not exist "target\installer" mkdir "target\installer"

"%JPACKAGE%" --type app-image ^
    --name Chocolateria ^
    --input target ^
    --main-jar "%JAR_FILE%" ^
    --main-class com.example.chocolateria.application.Launcher ^
    --dest target\installer ^
    --win-console ^
    --verbose

if errorlevel 1 (
    echo ERROR en jpackage
    echo Revisa que la clase main sea correcta
    pause
    exit /b 1
)

echo.
echo =============================================
echo LISTO! Aplicacion creada en: target\installer\Chocolateria
echo =============================================

pause