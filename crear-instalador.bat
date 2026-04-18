@echo off
setlocal EnableDelayedExpansion

echo =============================================
echo   Chocolateria - Crear Aplicacion Windows
echo =============================================
echo.

:: Detectar JAVA_HOME
if "%JAVA_HOME%"=="" (
    echo [ERROR] JAVA_HOME no esta configurado.
    echo Por favor instala JDK 21 o superior y configura JAVA_HOME.
    pause
    exit /b 1
)

set JPACKAGE=%JAVA_HOME%\bin\jpackage.exe
if not exist "%JPACKAGE%" (
    echo [ERROR] jpackage no encontrado en %JPACKAGE%
    echo Asegurate de usar JDK 21 o superior.
    pause
    exit /b 1
)

:: Detectar mvn
where mvn >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Maven (mvn) no encontrado en PATH.
    echo Agrega Maven al PATH e intenta de nuevo.
    pause
    exit /b 1
)

:: Compilar y empaquetar
echo [1/3] Compilando el proyecto con Maven...
call mvn clean package -DskipTests
if errorlevel 1 (
    echo [ERROR] La compilacion fallo. Revisa los errores arriba.
    pause
    exit /b 1
)
echo     OK - Compilacion exitosa.
echo.

:: Construir ruta al modpath de JavaFX desde repo local Maven
set M2=%USERPROFILE%\.m2\repository\org\openjfx
set FX_VER=21.0.6
set FX_CLASSIFIER=win

set FX_MODPATH=^
%M2%\javafx-controls\%FX_VER%\javafx-controls-%FX_VER%-%FX_CLASSIFIER%.jar;^
%M2%\javafx-fxml\%FX_VER%\javafx-fxml-%FX_VER%-%FX_CLASSIFIER%.jar;^
%M2%\javafx-web\%FX_VER%\javafx-web-%FX_VER%-%FX_CLASSIFIER%.jar;^
%M2%\javafx-swing\%FX_VER%\javafx-swing-%FX_VER%-%FX_CLASSIFIER%.jar;^
%M2%\javafx-media\%FX_VER%\javafx-media-%FX_VER%-%FX_CLASSIFIER%.jar;^
%M2%\javafx-base\%FX_VER%\javafx-base-%FX_VER%-%FX_CLASSIFIER%.jar;^
%M2%\javafx-graphics\%FX_VER%\javafx-graphics-%FX_VER%-%FX_CLASSIFIER%.jar

:: Verificar que existen los JARs de JavaFX nativos
if not exist "%M2%\javafx-controls\%FX_VER%\javafx-controls-%FX_VER%-%FX_CLASSIFIER%.jar" (
    echo [AVISO] No se encontro javafx-controls-%FX_VER%-win.jar en el repositorio Maven.
    echo Ejecuta primero: mvn dependency:resolve
    echo O descarga manualmente los JARs de JavaFX SDK para Windows.
)

:: Limpiar instalador anterior
if exist "target\installer" rmdir /s /q "target\installer"

echo [2/3] Generando imagen de aplicacion con jpackage...
"%JPACKAGE%" ^
    --type app-image ^
    --name Chocolateria ^
    --app-version 1.0 ^
    --vendor "La Chocolateria" ^
    --input target\lib ^
    --main-jar ..\CHOCOLATERIA-1.0-SNAPSHOT.jar ^
    --main-class com.example.chocolateria.application.Launcher ^
    --module-path "%FX_MODPATH%" ^
    --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing,javafx.media ^
    --java-options "--add-opens=javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED" ^
    --java-options "--add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED" ^
    --dest target\installer ^
    --win-console

if errorlevel 1 (
    echo.
    echo [ERROR] jpackage fallo. Revisa los mensajes de error arriba.
    pause
    exit /b 1
)

echo     OK - Imagen creada en target\installer\Chocolateria\
echo.
echo [3/3] Copiando JAR principal a la carpeta app...
copy /y "target\CHOCOLATERIA-1.0-SNAPSHOT.jar" "target\installer\Chocolateria\app\" >nul

echo.
echo =============================================
echo   LISTO!
echo   Carpeta de la aplicacion:
echo   %CD%\target\installer\Chocolateria\
echo.
echo   Para distribuirla: comprime esa carpeta en ZIP
echo   y enviala. El usuario ejecuta:
echo   Chocolateria\Chocolateria.exe
echo =============================================
echo.
pause
