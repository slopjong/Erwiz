@echo off
setlocal enabledelayedexpansion

rem language selection -----------------------------------------------------------------------------
echo Select language number. (Enter 'q' to quit.)
echo.
echo 1: English
echo 2: Japanese
echo.

:LANG_LOOP

set /p LANG_NUM="Language: "

rem remove double qote
if defined LANG_NUM (
	set LANG_NUM=%LANG_NUM:"=%
)

if /i "%LANG_NUM%" == "q" (
	exit /b 0
) else if "%LANG_NUM%" == "1" (
	set LANG_SELECT=en
) else if "%LANG_NUM%" == "2" (
	set LANG_SELECT=ja
) else (
	goto LANG_LOOP
)
rem LANG_LOOP END


rem change directory -------------------------------------------------------------------------------
cd /d %~dp0


rem copy files -------------------------------------------------------------------------------------
set SETUP_DIR=_setup_
set TARGET_DIR_LIST_1=common win %LANG_SELECT% %LANG_SELECT%-win
set TARGET_DIR_LIST_2=bin docs html-docs work

for %%i in (%TARGET_DIR_LIST_1%) do (
	
	set TARGET_DIR_1=%%i
	
	for %%j in (%TARGET_DIR_LIST_2%) do (
		
		set TARGET_DIR_2=%%j
		
		set SRC_DIR=.\%SETUP_DIR%\!TARGET_DIR_1!\!TARGET_DIR_2!
		set DST_DIR=.\!TARGET_DIR_2!
		
		if exist "!SRC_DIR!" (
			echo copying... [!SRC_DIR!\*] -^> [!DST_DIR!\*]
			
			if not exist "!DST_DIR!" (
				mkdir "!DST_DIR!"
			)
			
			rem  last \ of destination indicates that it's a directory. don't remove it.
			xcopy /s /e /h /k /y "!SRC_DIR!" "!DST_DIR!\" > NUL
			if ERRORLEVEL 1 (
				echo.
				echo Error
				echo Press any key to exit...
				pause > NUL
				exit /b 1
			)
		)
		
	)
)


rem copy/move files --------------------------------------------------------------------------------

rem .\work\scripts -> .\work
for %%i in (text2png-ie.cmd, text2png-idef1x.cmd) do (
	set FILE_NAME=%%i
	set SRC=.\work\scripts\!FILE_NAME!
	set DST=.\work\!FILE_NAME!
	
	echo copying... [!SRC!] -^> [!DST!]
	copy "!SRC!" "!DST!" > NUL
)

rem move readme.txt
set FILE_NAME=readme.txt
set SRC=.\docs\!FILE_NAME!
set DST=.\!FILE_NAME!
echo moving... [!SRC!] -^> [!DST!]
move "!SRC!" "!DST!" > NUL


rem completed message ------------------------------------------------------------------------------
echo.
echo Completed

set README=.\readme.txt
echo.
echo See %README%
set /p YN="Show now? (y/N): "

rem remove double qote
if defined YN (
	set YN=%YN:"=%
)

if /i "%YN%" == "y" (
	start %README%
)

exit /b 0

