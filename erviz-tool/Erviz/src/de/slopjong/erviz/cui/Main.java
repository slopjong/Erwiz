package de.slopjong.erviz.cui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.slopjong.erviz.common.IoUtils;
import de.slopjong.erviz.dot.GraphGenerator;
import de.slopjong.erviz.model.ColorPair;
import de.slopjong.erviz.model.ErdNotation;
import de.slopjong.erviz.model.Model;
import de.slopjong.erviz.plain.ModelParser;


/**
 * This is main class of this application which has main method.
 * 
 * @author kono
 */
public class Main {
	
	//utility object to write console messages
	private static final MessageWriter MW = new MessageWriter();
	
	/**
	 * Main method of this application.
	 * 
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		try {
			//initialize messages
			Message.initialize();
			
			//parse command line options
			final CommandLineOptions options = parseOptions(args);
			
			//read input text
			final List<String> lines = readInputLines(options);			
			
			//parse input text
			final Model model = parseInputLines(lines);
			
			//generate output text
			final String text = generateOutputText(model, options);
			
			//write output text
			writeOutputText(text, options);
			
		} catch (Exception ex) {
			MW.write(Message.ERR_UNKNOWN.getText(), 2);
			MW.exception(ex);
			System.exit(1);
		}
	}
	
	//parse command line options
	private static CommandLineOptions parseOptions(String[] args) {
		CommandLineOptions options = new CommandLineOptions(args);
		
		List<String> errorList = options.parse();
		if (!errorList.isEmpty()) {
			//error information
			MW.write(Message.ERR_INVALID_OPTION.getText(), 1);
			for (String error : errorList) {
				MW.write("  " + error, 1);  //indent is 2 spaces
			}
			MW.newLine();
			
			//help message
			MW.write(Message.HELP.getText(), 1);
			
			System.exit(1);
		
		} else if (options.helpRequested()) {
			MW.write(Message.APP_NAME.getText(), 2);
			MW.write(Message.HELP.getText(), 1);
			
			System.exit(0);
		}
		
		MW.setDebugMode(options.isDebugMode());
		MW.debug(Message.DBG_DEBUG_MODE_ON.getText(), 2);
		
		return options;
	}
	
	//read input text
	private static List<String> readInputLines(CommandLineOptions options) {
		try {
			File file = options.getInputFile();
			List<String> list;
			
			if (file != null) {
				list = IoUtils.readFileLines(file);
			} else {
				list = IoUtils.readStandardInputLines();
			}
			
			MW.debug(Message.DBG_READING_TEXT_COMPLETED.getText(), 2, true);
			return list;
			
		} catch (FileNotFoundException ex) {
			MW.write(Message.ERR_INPUT_FILE_NOT_FOUND.getText(getInputName(options)), 2);
			System.exit(1);
			return null;
		} catch (IOException ex) {
			MW.write(Message.ERR_INPUT_EXCEPTION.getText(getInputName(options)), 2);
			MW.exception(ex);
			System.exit(1);
			return null;
		}
	}
	
	//parse input text
	private static Model parseInputLines(List<String> lines) {
		try {
			ModelParser parser = new ModelParser(lines);
			List<Exception> exList = parser.parse();
			
			if (!exList.isEmpty()) {
				MW.write(Message.ERR_TEXT_PARSING.getText(), 2);
				MW.exception(exList.toArray(new Exception[]{}));
				System.exit(1);
				return null;
			}
			
			MW.debug(Message.DBG_PARSING_TEXT_COMPLETED.getText(), 2, true);
			MW.debug(parser.getModel().toString(""), 2);
			
			return parser.getModel();
			
		} catch (Exception ex) {
			MW.write(Message.ERR_TEXT_PARSING.getText(), 2);
			MW.exception(ex);
			System.exit(1);
			return null;
		}
	}
	
	//generate output text
	private static String generateOutputText(Model model, CommandLineOptions options) {
		try {
			ErdNotation notation = options.getNotation();
			if (notation == ErdNotation.NONE) {
				notation = DefaultValues.ERD_NOTATION;
			}
			
			String fontName = options.getFontName();
			
			ColorPair colorPair = options.getColorPair();
			if (colorPair == ColorPair.NONE) {
				colorPair = DefaultValues.COLOR_PAIR;
			}
			
			GraphGenerator genarator = new GraphGenerator(model, notation, fontName, colorPair);
			String dotText = genarator.execute();
			
			MW.debug(Message.DBG_GENERATING_DOT_COMPLETED.getText(), 2, true);
			return dotText;
			
		} catch (Exception ex) {
			MW.write(Message.ERR_DOT_GENERATION.getText(), 2);
			MW.exception(ex);
			System.exit(1);
			return null;
		}
	}
	
	//write output text
	private static void writeOutputText(String text, CommandLineOptions options) {
		try {
			final File file = options.getOutputFile();
			if (file != null) {
				IoUtils.writeFileText(file, text);
			} else {
				IoUtils.writeStandardOutputText(text);
			}
			
			MW.debug(Message.DBG_WRITING_DOT_COMPLETED.getText(), 2, true);
			
		} catch (FileNotFoundException ex) {
			MW.write(Message.ERR_OUTPUT_FILE_NOT_FOUND.getText(getOutputName(options)), 2);
			System.exit(1);
		} catch (IOException ex) {
			MW.write(Message.ERR_OUTPUT_EXCEPTION.getText(getOutputName(options)), 2);
			MW.exception(ex);
			System.exit(1);
		}
	}
	
	private static String getInputName(CommandLineOptions options) {
		File file = options.getInputFile();
		if (file != null) {
			return file.getAbsolutePath();
		} else {
			return Message.MISC_STDIN.getText();
		}
	}
	
	private static String getOutputName(CommandLineOptions options) {
		File file = options.getOutputFile();
		if (file != null) {
			return file.getAbsolutePath();
		} else {
			return Message.MISC_STDOUT.getText();
		}
	}
	

}
