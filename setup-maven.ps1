$ErrorActionPreference = 'Stop'

$mvnVersion = '3.9.15'
$mvnUrl = "https://downloads.apache.org/maven/maven-3/$mvnVersion/binaries/apache-maven-$mvnVersion-bin.zip"
$zipPath = "C:\Users\ypth\Downloads\apache-maven-$mvnVersion-bin.zip"
$installRoot = 'C:\Program Files\Apache\Maven'
$mvnDir = Join-Path $installRoot "apache-maven-$mvnVersion"

Write-Host "Downloading Maven from: $mvnUrl"
if (!(Test-Path $zipPath)) {
  Invoke-WebRequest -Uri $mvnUrl -OutFile $zipPath
}

if (!(Test-Path $installRoot)) {
  New-Item -ItemType Directory -Path $installRoot | Out-Null
}

Write-Host "Extracting to: $installRoot"
if (Test-Path $mvnDir) {
  Remove-Item -Recurse -Force $mvnDir
}

Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory($zipPath, $installRoot)

# Configure environment variables
$binDir = Join-Path $mvnDir 'bin'

Write-Host "Setting MAVEN_HOME and updating PATH (Machine)"
setx MAVEN_HOME $mvnDir | Out-Null

$currentPath = [Environment]::GetEnvironmentVariable('Path', 'Machine')
if ($currentPath -notlike "*${binDir}*") {
  setx Path ($currentPath + ';' + $binDir) /M | Out-Null
}

# Current session variables (for immediate use)
$env:MAVEN_HOME = $mvnDir
$env:Path = $env:Path + ';' + $binDir

Write-Host "Done. Verifying mvn..."

$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
  throw 'Maven was installed but mvn is still not found in PATH. Please restart your terminal (or Windows) and try again.'
}

mvn -v

