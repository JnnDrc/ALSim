$ErrorActionPreference = "Stop"

$AppName = "ALSim"
$MainClass = "net.pimenta.alsim.Main"
$JarName = "alsim.jar"

$Dist = "dist"

$Root = Resolve-Path "$PSScriptRoot\.."
Set-Location $Root

Write-Host "==> Building..."
mvn clean package dependency:copy-dependencies `
    -DincludeGroupIds=org.openjfx `
    -DoutputDirectory=target/javafx

Write-Host "==> Preparing dist..."
if(Test-Path $Dist) {
    Remove-Item $Dist -Recurse -Force
}

New-Item -ItemType Directory -Path $Dist | Out-Null

Write-Host "==> Packaging program..."
jpackage `
    --name $AppName `
    --input target `
    --main-jar $JAR_NAME `
    --main-class $MAIN_CLASS `
    --module-path target/javafx `
    --add-modules javafx.controls `
    --type app-image `
    --dest $DIST

Write-Host "==> Copying resources..."
Copy-Item "resources\examples" "$Dist\$AppName" -Recurse

Write-Host "==> Creating ZIP..."
Compress-Archive `
    -Path "$Dist\$AppName" `
    -DestinationPath "$Dist\${AppName}-windows-x64.zip" `
    -Force

Write-Host ""
Write-Host "==> Done:"
Write-Host "    $Dist\${AppName}-windows-x64.zip"