# -*- encoding: utf-8 -*-

=begin
	Writes Erviz utility scripts (sh)
	
	NOTE: New Line : LF
=end

require 'FileUtils'

class ShWriter
	
	#public
	def initialize(out_type, notation, font)
		@out_type = out_type
		@notation = notation
		@font = font
	end
	
	#public
	def write(file_path)
		# w: CR+LF, wb: LF
		File.open(file_path, "wb:UTF-8"){ |file|
		
			file.print <<-"EOB"
#!/bin/sh

# -- config ----------------------------
export NOTATION=#{@notation}
export OUT_TYPE=#{@out_type}
export ENTITY_COLOR=yellow
export FONT_NAME=#{@font}
export STOP_BEFORE_NORMAL_EXIT=0
export STOP_BEFORE_ABNORMAL_EXIT=0
export SHOW_PROGRESS=1
# --------------------------------------

PATH="$PATH:`dirname $0`/../bin"
CONVERT_SH="convert-files.sh"

"${CONVERT_SH}" $@
			EOB
		
		}
	end
	
end
