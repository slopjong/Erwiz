# -*- encoding: utf-8 -*-

=begin
	Generates Erviz utility script
=end

require 'FileUtils'
require './#ruby-scripts/cmd_writer.rb'
require './#ruby-scripts/sh_writer.rb'

class ScriptGenerator
	
	#public
	def ScriptGenerator.clean(lang, script_type)
		
		dst_dir_name = get_dest_dir_name(lang, script_type)
		
		if File.exist?(dst_dir_name) then
			pattern = "#{dst_dir_name}/*.#{script_type}"
			Dir.glob(pattern) { |filename|
				File.delete(filename)
			}
		else
			FileUtils.mkdir_p(dst_dir_name)
		end
		
	end
	
	#public
	def initialize(lang, script_type, in_type, out_type, notation)
		@lang = lang
		@script_type = script_type
		@in_type = in_type
		@out_type = out_type
		@notation = notation if in_type == "text"
		@font = "MS UI Gothic" if lang == "ja" && script_type == "cmd" && in_type == "text"
	end
	
	#public
	def generate()
		dst_file_path = "#{get_dest_dir_name()}/#{get_dest_file_name()}"
		
		if @script_type == "cmd" then
			writer = CmdWriter.new(@out_type, @notation, @font)
			writer.write(dst_file_path)
		elsif @script_type == "sh" then
			writer = ShWriter.new(@out_type, @notation, @font)
			writer.write(dst_file_path)
		else
			puts "unknown script type [#{@script_type}]"
		end
		
	end
	
	#private (class method)
	def ScriptGenerator.get_dest_dir_name(lang, script_type)
		os_map = {"cmd" => "win", "sh" => "unix"}
		return "#{lang}-#{os_map[script_type]}"
	end
	
	#private
	def get_dest_dir_name()
		return ScriptGenerator.get_dest_dir_name(@lang, @script_type)
	end	
	
	#private
	def get_dest_file_name()
		if @notation != nil then
			return "#{@in_type}2#{@out_type}-#{@notation}.#{@script_type}"
		else
			return "#{@in_type}2#{@out_type}.#{@script_type}"
		end
	end	
	
end
