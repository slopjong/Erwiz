# -*- encoding: utf-8 -*-

=begin
	Writes Erviz utility scripts (cmd)
=end

require 'FileUtils'

class CmdWriter
	
	#public
	def initialize(out_type, notation, font)
		@out_type = out_type
		@notation = notation
		@font = font
	end
	
	#public
	def write(file_path)
		# w: CR+LF, wb: LF
		File.open(file_path, "w:Shift_JIS"){ |file|
		
			file.print <<-"EOB"
@echo off
setlocal enabledelayedexpansion

rem -- config --------------------------
set NOTATION=#{@notation}
set OUT_TYPE=#{@out_type}
set ENTITY_COLOR=yellow
set FONT_NAME=#{@font}
set STOP_BEFORE_NORMAL_EXIT=0
set STOP_BEFORE_ABNORMAL_EXIT=1
set SHOW_PROGRESS=1
rem ------------------------------------

set PATH=%PATH%;%~dp0..\\bin
set CONVERT_CMD=convert-files.cmd

set PRAMS=
for %%i in (%*) do (
	set PRAMS=!PRAMS! "%%~i"
)

call %CONVERT_CMD% %PRAMS%

exit /b %ERRORLEVEL%
			EOB
		
		}
	end
	

	
end
