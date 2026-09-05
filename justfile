set shell := ["powershell.exe", "-NoLogo", "-Command"]

jar_name := "click2minimize-plugin.jar"
java_home := "c:\\Users\\Nayan\\Desktop\\runelite exploration\\HelloWorldPlugin\\jdk-11\\jdk-11.0.24+8"
plugin_dir := env_var("USERPROFILE") + "\\.runelite\\plugins"

# Run the plugin in developer mode
run:
    $env:JAVA_HOME='{{java_home}}'; .\gradlew.bat run

# Build the jar file
build:
    $env:JAVA_HOME='{{java_home}}'; .\gradlew.bat jar

# Deploy the jar to the RuneLite plugins directory
deploy: build
    if (!(Test-Path '{{plugin_dir}}')) { New-Item -ItemType Directory -Force -Path '{{plugin_dir}}' | Out-Null }
    Copy-Item -Path "build\libs\{{jar_name}}" -Destination "{{plugin_dir}}\{{jar_name}}" -Force
    Write-Host "Deployed {{jar_name}} to {{plugin_dir}}"

# Watch for changes to the jar file and do something
watch-jar:
    $lastWrite = (Get-Item "build\libs\{{jar_name}}").LastWriteTime
    Write-Host "Watching for changes to {{jar_name}}..."
    while($true) { Start-Sleep -Seconds 2; $currentWrite = (Get-Item "build\libs\{{jar_name}}").LastWriteTime; if ($currentWrite -ne $lastWrite) { Write-Host 'Jar changed! Deploying...'; just deploy; $lastWrite = $currentWrite } }

# Watch source files and rebuild/deploy automatically on change
watch-source:
    Write-Host 'Watching src folder for changes...'
    $watcher = New-Object System.IO.FileSystemWatcher
    $watcher.Path = 'src'
    $watcher.IncludeSubdirectories = $true
    $watcher.EnableRaisingEvents = $true
    while($true) { $change = $watcher.WaitForChanged([System.IO.WatcherChangeTypes]::All, 1000); if ($change.ChangeType -ne 0) { Write-Host 'Source changed! Rebuilding...'; just deploy } }
