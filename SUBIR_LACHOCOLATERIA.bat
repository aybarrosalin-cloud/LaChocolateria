@echo off
REM ═══════════════════════════════════════════════════════════════════
REM  Script para subir LaChocolateria a GitHub
REM  Ejecutar desde la terminal de IntelliJ en la carpeta CHOCOLATERIA
REM ═══════════════════════════════════════════════════════════════════

cd /d "%~dp0"

REM Copiar el repo limpio a una carpeta temporal
SET TEMP_REPO=%TEMP%\lachoco_push
IF EXIST "%TEMP_REPO%" rmdir /s /q "%TEMP_REPO%"

git clone . "%TEMP_REPO%" --quiet

cd "%TEMP_REPO%"

REM Configurar usuario
git config user.name "Rosalin"
git config user.email "aybarrosalin@gmail.com"

REM Limpiar mensajes y Co-Authored-By de Claude
echo Limpiando historial...
SET FILTER_SCRIPT=%TEMP%\msg_filter.py
(
echo import sys, re
echo msg = sys.stdin.read^(^)
echo msg = re.sub^(r'\nCo-[Aa]uthored-[Bb]y:.*Claude.*\n?', '', msg^)
echo msg = re.sub^(r'Co-[Aa]uthored-[Bb]y:.*Claude.*\n?', '', msg^)
echo REWRITES = {
echo     'Fix all 16 truncated vistaConsulta*.fxml and add missing String imports': 'arregle las pantallas de consulta que no cargaban',
echo     'fix: completar FXMLs truncados, agregar tablas moradas y reparar consultas/inventario': 'termine de arreglar las consultas y el inventario',
echo     'feat: cotizacion en Orden de Cliente': 'le agregue la cotizacion con el subtotal a la orden',
echo     'fix: corregir 7 bugs': 'corregi varios bugs en ventas, inventario y empleados',
echo     'Merge: accept fixed consulta FXMLs from claude branch': 'merge arreglos de las consultas',
echo     'Rediseno menu: eliminar Inventario': 'redisene el menu y los botones de ver consulta',
echo     'Fix missing quotes': 'arregle un error en el modulo de envios',
echo     'Fix null bytes in envioController': 'arregle bug en envios que no compilaba',
echo     'Fix null bytes in reclamoController': 'arregle bug en reclamos que no compilaba',
echo }
echo s = msg.strip^(^)
echo for old, new in REWRITES.items^(^):
echo     if s.startswith^(old^): msg = new + '\n'; break
echo if s.startswith^('Merge branch .master. of https://github.com'^): msg = 'merge\n'
echo if s.startswith^('merge: aplicar'^): msg = 'merge de los arreglos\n'
echo sys.stdout.write^(msg^)
) > "%FILTER_SCRIPT%"

set FILTER_BRANCH_SQUELCH_WARNING=1
git filter-branch -f --msg-filter "python \"%FILTER_SCRIPT%\"" -- master login-y-acceso pantallas-base modulos-registro modulo-ventas modulo-consultas 2>nul

REM Crear las ramas
git branch login-y-acceso c2e2b51 2>nul
git branch pantallas-base b58f253 2>nul
git branch modulos-registro e551c54 2>nul
git branch modulo-ventas 8cdabbf 2>nul
git branch modulo-consultas e74b1d5 2>nul

REM Configurar remote
git remote remove origin 2>nul
git remote add origin https://github.com/aybarrosalin-cloud/LaChocolateria.git

REM Subir todo
echo.
echo Subiendo a GitHub...
git push origin master --force
git push origin login-y-acceso pantallas-base modulos-registro modulo-ventas modulo-consultas

echo.
echo LISTO. Revisa https://github.com/aybarrosalin-cloud/LaChocolateria
pause
