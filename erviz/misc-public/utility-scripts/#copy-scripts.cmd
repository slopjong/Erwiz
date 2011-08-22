@echo off
setlocal enabledelayedexpansion

cd "%~dp0"

for %%i in (en-win, en-unix, ja-win, ja-unix) do (
	set SRC_DIR=.\%%i
	set DST_DIR=..\..\release-files\_setup_\%%i\work\scripts
	
	echo copy [!SRC_DIR!] -^> [!DST_DIR!] 1>&2
	
	rem  last \ of destination indicates that it's a directory. don't remove it.
	xcopy /s /e /h /k /y "!SRC_DIR!" "!DST_DIR!\" > NUL
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
