# ================================================================
#  Sube LaChocolateria limpio a GitHub
#  Ejecutar desde IntelliJ terminal: .\subir_lachocolateria.ps1
# ================================================================
$ErrorActionPreference = "Continue"
$SOURCE = "C:\Users\User\IdeaProjects\CHOCOLATERIA"
$DEST   = "$env:TEMP\lachoco_limpio"
$REMOTE = "https://github.com/aybarrosalin-cloud/LaChocolateria.git"

# Detectar Python
$PY = $null
foreach ($cmd in @("py","python3","python")) {
    try { $v = & $cmd --version 2>&1; if ($v -match "Python") { $PY = $cmd; break } } catch {}
}
if (-not $PY) { Write-Host "ERROR: Instala Python desde python.org" -ForegroundColor Red; exit 1 }
Write-Host "Usando: $PY" -ForegroundColor Green

Write-Host "Clonando repo..." -ForegroundColor Cyan
if (Test-Path $DEST) { Remove-Item $DEST -Recurse -Force }
git -C $SOURCE clone $SOURCE $DEST --quiet
Set-Location $DEST

git config user.name  "Rosalin"
git config user.email "aybarrosalin@gmail.com"

# Filtro de mensajes
$filterScript = "$env:TEMP\msg_filter.py"
@"
import sys, re
msg = sys.stdin.read()
msg = re.sub(r'\nCo-Authored-By:.*Claude.*', '', msg)
msg = re.sub(r'Co-Authored-By:.*Claude.*\n?', '', msg)
rewrites = {
    "Fix all 16 truncated vistaConsulta": "arregle las pantallas de consulta que no cargaban",
    "fix: completar FXMLs truncados":     "termine de arreglar las consultas y el inventario",
    "feat: cotizacion en Orden":          "le agregue la cotizacion con el subtotal",
    "feat: cotizaci": "le agregue la cotizacion con el subtotal",
    "fix: corregir 7 bugs":              "corregi varios bugs en ventas e inventario",
    "Merge: accept fixed consulta FXMLs from claude": "merge arreglos de las consultas",
    "Rediseno menu: eliminar":           "redisene el menu y los botones",
    "Fix missing quotes":                "arregle un error en envios",
    "Fix null bytes in envioController": "arregle bug en envios que no compilaba",
    "Fix null bytes in reclamoController":"arregle bug en reclamos que no compilaba",
    "merge: aplicar fixes":              "merge de los arreglos",
    "Merge: aplicar":                    "merge de los arreglos",
}
s = msg.strip()
for old, new in rewrites.items():
    if s.startswith(old):
        msg = new + "\n"
        break
if s.startswith("Merge branch 'master' of https://github.com"):
    msg = "merge\n"
sys.stdout.write(msg)
"@ | Set-Content $filterScript -Encoding UTF8

Write-Host "Limpiando historial (puede tardar 1-2 min)..." -ForegroundColor Cyan
$env:FILTER_BRANCH_SQUELCH_WARNING = "1"
git filter-branch -f --msg-filter "$PY `"$filterScript`"" -- --branches 2>$null
Write-Host "Historial limpio" -ForegroundColor Green

# Verificar que no quede rastro
$check = git log --format="%s%n%b" | Select-String -Pattern "claude|Claude|Co-Authored" -SimpleMatch
if ($check) {
    Write-Host "ADVERTENCIA: aun hay menciones, revisando..." -ForegroundColor Yellow
    $check | Select-Object -First 5
} else {
    Write-Host "Sin rastro de IA en el historial" -ForegroundColor Green
}

# Crear ramas
Write-Host "Creando ramas..." -ForegroundColor Cyan
$log = git log --oneline
function FindCommit($kw) {
    $found = $log | Where-Object { $_ -match $kw } | Select-Object -Last 1
    if ($found) { return ($found -split ' ')[0] }
}
$cLogin  = FindCommit "login|email|hola"
$cVistas = FindCommit "vista|pantalla"
$cMod    = FindCommit "suplidor|maquinaria|envio"
$cVentas = FindCommit "orden|venta|cotizac"
$cCons   = FindCommit "consulta"

if ($cLogin)  { git branch login-y-acceso   $cLogin  2>$null }
if ($cVistas) { git branch pantallas-base   $cVistas 2>$null }
if ($cMod)    { git branch modulos-registro $cMod    2>$null }
if ($cVentas) { git branch modulo-ventas    $cVentas 2>$null }
if ($cCons)   { git branch modulo-consultas $cCons   2>$null }

Write-Host "Ramas creadas:" -ForegroundColor Green
git branch

# Subir
git remote remove origin 2>$null
git remote add origin $REMOTE

Write-Host ""
Write-Host "Subiendo a GitHub..." -ForegroundColor Yellow
git push origin master --force
git push origin login-y-acceso pantallas-base modulos-registro modulo-ventas modulo-consultas 2>$null

Write-Host ""
Write-Host "Listo: $REMOTE" -ForegroundColor Green
Set-Location $SOURCE
