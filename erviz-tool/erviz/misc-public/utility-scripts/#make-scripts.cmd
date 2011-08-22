@echo off
setlocal

cd "%~dp0"
ruby ".\#ruby-scripts\main.rb"

echo Press any key to exit... 1>&2
pause > NUL

exit /b 0
