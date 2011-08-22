@echo off
setlocal

rem  A wrapper for erviz-cui-*.*.*.jar

rem  Tested on Windows XP Professional SP3

call "%~dp0\messages.cmd"
call "%~dp0\lang.cmd"

set CUI_JAR_NAME_PATTERN=erviz-cui-*.*.*.jar
set CUI_JAR_PATH_PATTERN="%~dp0%CUI_JAR_NAME_PATTERN%"

set CORE_JAR_NAME_PATTERN=erviz-core-*.*.*.jar
set CORE_JAR_PATH_PATTERN="%~dp0%CORE_JAR_NAME_PATTERN%"

set MAIN_CLASS=jp.gr.java_conf.simply.erviz.cui.Main

rem  searches the last modified jar file (cui)
for /f "usebackq" %%i in (`dir /a /b /o:-d /TW %CUI_JAR_PATH_PATTERN% 2^> NUL`) do (
	set CUI_JAR_NAME="%~dp0\%%i"
	goto LOOP1_END
)
:LOOP1_END

if not defined CUI_JAR_NAME (
	echo %MSG_ERROR%: %MSG_JAR_NOT_FOUND% ^(%CUI_JAR_NAME_PATTERN%^) 1>&2
	exit /b 1
)

rem  searches the last modified jar file (core)
for /f "usebackq" %%i in (`dir /a /b /o:-d /TW %CORE_JAR_PATH_PATTERN% 2^> NUL`) do (
	set CORE_JAR_NAME="%~dp0\%%i"
	goto LOOP2_END
)
:LOOP2_END

if not defined CORE_JAR_NAME (
	echo %MSG_ERROR%: %MSG_JAR_NOT_FOUND% ^(%CORE_JAR_NAME_PATTERN%^) 1>&2
	exit /b 1
)

rem  executes the jar file
set USER_PROPERTIES=-Duser.language=%USER_LANGUAGE% -Duser.country=%USER_COUNTRY%
java %USER_PROPERTIES% -cp %CUI_JAR_NAME%;%CORE_JAR_NAME% %MAIN_CLASS% %*

if ERRORLEVEL 1 (exit /b 1)

exit /b 0
