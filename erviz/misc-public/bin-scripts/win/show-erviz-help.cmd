@echo off
setlocal

call "%~dp0\messages.cmd"

set ERVIZ_CMD=%~dp0\erviz.cmd

call "%ERVIZ_CMD%" -h

echo. 1>&2
echo %MSG_PRESS_ANY_KEY_TO_EXIT% 1>&2
pause > NUL

exit /b 0
