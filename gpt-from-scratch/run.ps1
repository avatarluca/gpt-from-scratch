$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot
$sourceRoot = Join-Path $projectRoot "src"
$outputDir = Join-Path $projectRoot "out"
$sources = Get-ChildItem -Path $sourceRoot -Filter "*.java" -Recurse |
    ForEach-Object { $_.FullName }

if (-not $sources) {
    Write-Error "No Java source files found under '$sourceRoot'."
    exit 1
}

New-Item -ItemType Directory -Path $outputDir -Force | Out-Null

& javac -d $outputDir @sources
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

& java -cp $outputDir src.GPT
exit $LASTEXITCODE
