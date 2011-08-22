@echo off
setlocal

set OUTPUT_TYPE=png

:LOOP
if "%~1" == "" (
rem	echo completed
rem	pause > NUL
	exit /b 0
)

cd /d "%~dp1"

set TARGET=%~dpn1
echo [%TARGET%.%OUTPUT_TYPE%"]
dot -T%OUTPUT_TYPE% "%TARGET%.dot" -o "%TARGET%.%OUTPUT_TYPE%"

if ERRORLEVEL 1 (
rem	echo ERROR
rem	pause > NUL
	exit /b 1
)

shift /1
goto LOOP
