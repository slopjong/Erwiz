@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

for %%i in (common) do (
	
	cd "%~dp0%%i"
	
	echo [%%i] 1>&2
	call .\make-image.cmd "conversion.dot"
	if ERRORLEVEL 1 (
		echo Error 1>&2
		echo Press any key to exit... 1>&2
		pause > NUL
		exit /b 1
	)
	
)

echo Completed 1>&2
echo Press any key to exit... 1>&2
pause > NUL

exit /b 0
