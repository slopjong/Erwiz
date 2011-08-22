=begin
	Main
	
	Note : Set current directory to the parent of "#ruby-scripts"
=end

require './#ruby-scripts/script_generator.rb'

langs = ["ja", "en"]
script_types = ["cmd", "sh"]
in_types = ["text", "dot"]
out_types = ["png", "jpg", "svg", "pdf", "dot"]
notations = ["ie", "ie-strict", "idef1x"]

langs.each{ |lang|
	script_types.each { |script_type|
		
		ScriptGenerator.clean(lang, script_type)
		
		in_types.each { |in_type|
			out_types.each { |out_type|
				
				next if in_type == "dot" && out_type == "dot"
				
				if in_type == "text" then
					notations.each { |notation|
						generator = ScriptGenerator.new(lang, script_type, in_type, out_type, notation)
						generator.generate()
					}
				elsif in_type == "dot" then
					generator = ScriptGenerator.new(lang, script_type, in_type, out_type, nil) #notation not needed
					generator.generate()
				end
				
			}
		}
	}
}



