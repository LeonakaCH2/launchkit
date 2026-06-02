$ErrorActionPreference = "Stop"

$projectRoot = Resolve-Path "$PSScriptRoot\.."
$binPath = Join-Path $projectRoot "bin"
$jarPath = Join-Path $projectRoot "target\launchkit-0.1.0.jar"

Write-Host ""
Write-Host "LaunchKit Windows Installer"
Write-Host "Project root: $projectRoot"
Write-Host ""

if (!(Test-Path $jarPath)) {
    Write-Host "LaunchKit jar not found."
    Write-Host "Please build the project first:"
    Write-Host "  mvn clean package"
    exit 1
}

if (!(Test-Path $binPath)) {
    New-Item -ItemType Directory -Path $binPath | Out-Null
}

$launcherPath = Join-Path $binPath "launchkit.bat"

@"
@echo off
java -jar "%~dp0..\target\launchkit-0.1.0.jar" %*
"@ | Set-Content -Path $launcherPath -Encoding ASCII

Write-Host "Created launcher:"
Write-Host "  $launcherPath"
Write-Host ""

$answer = Read-Host "Do you want to add LaunchKit to your user PATH? [Y/n]"

if ($answer -eq "" -or $answer.ToLower() -eq "y" -or $answer.ToLower() -eq "yes") {
    $currentPath = [Environment]::GetEnvironmentVariable("Path", "User")

    if ($currentPath -split ";" | Where-Object { $_ -eq $binPath }) {
        Write-Host "LaunchKit is already in PATH."
    } else {
        $newPath = "$currentPath;$binPath"
        [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
        Write-Host "Added LaunchKit to user PATH:"
        Write-Host "  $binPath"
        Write-Host ""
        Write-Host "Please close and reopen your terminal."
    }
} else {
    Write-Host "Skipped PATH update."
    Write-Host "You can run LaunchKit with:"
    Write-Host "  $launcherPath"
}

Write-Host ""
Write-Host "Installation complete."