@echo off
setlocal enabledelayedexpansion

rem Tested on Windows XP Professional SP3


rem -- initialize --------------------------------------------------------------

rem load messages
call "%~dp0\messages.cmd"

rem commands
set ERVIZ_CMD="%~dp0\erviz.cmd"
set DOT_CMD=dot

rem ERD notation (ie, idef1x)
if not defined NOTATION set NOTATION=ie

rem output file type (dot, png, jpeg, pdf, svg, ...)
if not defined OUT_TYPE set OUT_TYPE=png

rem entity color (white, red, blue, green, yellow, orange)
if not defined ENTITY_COLOR set ENTITY_COLOR=white

rem font
rem if the font name is not specified by the invoker, default font is used.

rem whether stop before exit or not
if not defined STOP_BEFORE_NORMAL_EXIT set STOP_BEFORE_NORMAL_EXIT=0
if not defined STOP_BEFORE_ABNORMAL_EXIT set STOP_BEFORE_ABNORMAL_EXIT=0

rem whether show progress or not
if not defined SHOW_PROGRESS set SHOW_PROGRESS=0

rem ----------------------------------------------------------------------------


rem -- main --------------------------------------------------------------------

:FILE_LOOP
	if "%~1" == "" goto FILE_LOOP_END
	
	set IN_FILE="%~nx1"
	
	if %SHOW_PROGRESS% == 1 (
		echo %MSG_INPUT_FILE% : [!IN_FILE:~1,-1!] 1>&2
	)
	
	call :convert_file "%~1"
	if ERRORLEVEL 1 (
		if %STOP_BEFORE_ABNORMAL_EXIT% == 1 (
			echo. 1>&2
			echo %MSG_PRESS_ANY_KEY_TO_EXIT% 1>&2
			pause > NUL
		)
		exit /b 1
	)
	
	shift /1
	goto FILE_LOOP
:FILE_LOOP_END

if %STOP_BEFORE_NORMAL_EXIT% == 1 (
	echo. 1>&2
	echo %MSG_PRESS_ANY_KEY_TO_EXIT% 1>&2
	pause > NUL
)

exit /b 0
rem ----------------------------------------------------------------------------


rem function -------------------------------------------------------------------
:convert_file
	
	set IN_FILE="%~dpnx1"
	set IN_EXT=%~x1
	set IN_TYPE=%IN_EXT:~1%
	
	if "%IN_TYPE%" == "txt" (
		if "%OUT_TYPE%" == "dot" (
			rem text to dot
			set OUT_FILE="%~dp1%~n1-%NOTATION%.%OUT_TYPE%"
			call %ERVIZ_CMD% -f "%FONT_NAME%" -c "%ENTITY_COLOR%" -n "%NOTATION%" < %IN_FILE% > !OUT_FILE!
		) else (
			rem text to image
			set OUT_FILE="%~dp1%~n1-%NOTATION%.%OUT_TYPE%"
			call %ERVIZ_CMD% -f "%FONT_NAME%" -c "%ENTITY_COLOR%" -n "%NOTATION%" < %IN_FILE% | "%DOT_CMD%" -T%OUT_TYPE% > !OUT_FILE!
		)
	) else if "%IN_TYPE%" == "dot" (
		if "%OUT_TYPE%" == "dot" (
			rem dot to dot (append tilde before extension)
			set OUT_FILE="%~dp1%~n1~.%OUT_TYPE%"
			%DOT_CMD% -T%OUT_TYPE% < %IN_FILE% > !OUT_FILE!
		) else (
			rem dot to image
			set OUT_FILE="%~dp1%~n1.%OUT_TYPE%"
			%DOT_CMD% -T%OUT_TYPE% < %IN_FILE% > !OUT_FILE!
		)
	) else (
		echo. 1>&2
		echo %MSG_ERROR%: %MSG_INVALID_IN_FILE_TYPE%  file=[!IN_FILE:~1,-1!]  type=[%IN_TYPE%] 1>&2
		exit /b 1
	)
	
	if not ERRORLEVEL 1 (
		call :validate_output_file %OUT_FILE%
	)
	
	if ERRORLEVEL 1 (
		call :delete_empty_output_file %OUT_FILE%
		
		echo. 1>&2
		echo %MSG_ERROR%: %MSG_CONV_FAULT%  file=[!IN_FILE:~1,-1!] 1>&2
		exit /b 1
	)
	
exit /b 0
rem ----------------------------------------------------------------------------


rem function -------------------------------------------------------------------
:validate_output_file
	
	rem file not found
	if not exist "%~1" (
		exit /b 11
	)
	
	rem file size is zero
	if %~z1 equ 0 (
		exit /b 12
	)
	
exit /b 0
rem ----------------------------------------------------------------------------


rem function -------------------------------------------------------------------
:delete_empty_output_file
	
	rem file exists and file size is zero
	if exist "%~1" (
		if %~z1 equ 0 (
			del "%~1"
		)
	)
	
exit /b 0
rem ----------------------------------------------------------------------------
