package jp.gr.java_conf.simply.erviz.cui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.simply.erviz.common.GeneralCommandLineOptions;
import jp.gr.java_conf.simply.erviz.model.ErdNotation;
import jp.gr.java_conf.simply.erviz.model.ColorPair;

/**
 * This class represents the command line options.
 * 
 * @author kono
 * @version 1.0
 */
public final class CommandLineOptions {
	
	//input
	private final GeneralCommandLineOptions options;
	
	//output
	private File inputFile = null;
	private File outputFile = null;
	private ErdNotation notation = ErdNotation.NONE;
	private ColorPair colorPair = ColorPair.NONE;
	private String fontName = null;
	private boolean helpRequested = false;
	private boolean debugMode = false;
	
	/**
	 * Constructs a object of this class.
	 * 
	 * @param args commandline arguments
	 */
	public CommandLineOptions(String[] args) {
		this.options = new GeneralCommandLineOptions(args);
	}
	
	/**
	 * Parses command line options which is set by using constructor.
	 * if error occured, this method returns an error list that has some elements,
	 * otherwise this method returns an emply list.
	 * 
	 * @return an error list
	 */
	public List<String> parse() {
		List<String> errorList = parseArgs();
		if (!errorList.isEmpty()) {
			return errorList;
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Returns true if help is requested in the command line options, false otherwise.
	 * @return true if help is requested in command line options, false otherwise.
	 */
	public boolean helpRequested() {
		return this.helpRequested;
	}
	
	/**
	 * Returns true if debug mode is requested in the command line options, false otherwise.
	 * @return true if help is debug mode in command line options, false otherwise.
	 */
	public boolean isDebugMode() {
		return this.debugMode;
	}
	
	/**
	 * Retrieves the input file path which is specified in command line options.
	 * 
	 * @return the input file path
	 */
	public File getInputFile() {
		return this.inputFile;
	}
	
	/**
	 * Retrieves the output file path which is specified in command line options.
	 * 
	 * @return the output file path
	 */
	public File getOutputFile() {
		return this.outputFile;
	}
	
	/**
	 * Retrieves the notation type which is specified in the command line options.
	 * @return the notation type
	 */
	public ErdNotation getNotation() {
		return this.notation;
	}
	
	/**
	 * Retrieves a pair of colors which is specified in the command line options.
	 * This colors should be used to decide default entity colors.
	 * 
	 * @return the notation type
	 */
	public ColorPair getColorPair() {
		return this.colorPair;
	}
	
	/**
	 * Retrieves the font name which will be writen in output file.
	 * 
	 * @return the font name
	 */
	public String getFontName() {
		return this.fontName;
	}
	
	/**
	 * Parses the specified command line arguments.
	 * The result is saved in this object.
	 * If error occured, this method returns an error list that has some elements,
	 * otherwise this method returns an emply list.
	 * 
	 * @return an error list
	 */
	private List<String> parseArgs() {
		
		this.options.parse();
		
		ErrorInfo errorInfo = new ErrorInfo();
		
		for (final String option : this.options.getAllOptions()) {
			if (option.equals("-h")) {
				setHelpOption(option, errorInfo);
			} else if (option.equals("-d")) {
				setDebugModeOption(option, errorInfo);
			} else if (option.equals("-i")) {
				setInputFileOption(option, errorInfo);
			} else if (option.equals("-o")) {
				setOutputFileOption(option, errorInfo);
			} else if (option.equals("-n")) {
				setNotationOption(option, errorInfo);
			} else if (option.equals("-f")) {
				setFontNameOption(option, errorInfo);
			} else if (option.equals("-c")) {
				setColorNameOption(option, errorInfo);
			} else {
				errorInfo.add(Message.CLO_UNKNOWN_OPTION, option);
			}
		}
		
		for (final String param : this.options.getUnknownParams()) {
			errorInfo.add(Message.CLO_INVALID_PARAM, param);
		}
		
		return errorInfo.getLines();
	}
	
	//Help
	private void setHelpOption(String option, ErrorInfo errorInfo) {
		this.helpRequested = true;
		
		final List<String> params = this.options.getOptionParams(option);
		for (final String param : params) {
			errorInfo.add(Message.CLO_INVALID_PARAM, param);
		}
	}
	
	//Debug Mode
	private void setDebugModeOption(String option, ErrorInfo errorInfo) {
		this.debugMode = true;
		
		final List<String> params = this.options.getOptionParams(option);
		for (final String param : params) {
			errorInfo.add(Message.CLO_INVALID_PARAM, param);
		}
	}
	
	//Input File
	private void setInputFileOption(String option, ErrorInfo errorInfo) {
		final List<String> params = this.options.getOptionParams(option);
		
		if (params.size() >= 1) {
			String param = params.get(0);
			if (!param.trim().equals("")) {
				this.inputFile = new File(param);
			}
		}
		
		if (params.size() >= 2) {
			for (final String param : params.subList(1, params.size())) {
				errorInfo.add(Message.CLO_INVALID_PARAM, param);
			}
		}
		
	}
	
	//Output File
	private void setOutputFileOption(String option, ErrorInfo errorInfo) {
		final List<String> params = this.options.getOptionParams(option);
		
		if (params.size() >= 1) {
			String param = params.get(0);
			if (!param.trim().equals("")) {
				this.outputFile = new File(param);
			}
		}
		
		if (params.size() >= 2) {
			for (final String param : params.subList(1, params.size())) {
				errorInfo.add(Message.CLO_INVALID_PARAM, param);
			}
		}	
	}
	
	//Notation
	private void setNotationOption(String option, ErrorInfo errorInfo) {
		final List<String> params = this.options.getOptionParams(option);
		
		if (params.size() >= 1) {
			final String param = params.get(0);
			
			try {
				this.notation = ErdNotation.valueOf(param.toUpperCase().replace('-', '_'));
			} catch (IllegalArgumentException ex) {
				errorInfo.add(Message.CLO_UNKNOWN_ERD_NOTATION, param);
			}
		}
		
		if (params.size() >= 2) {
			for (final String param : params.subList(1, params.size())) {
				errorInfo.add(Message.CLO_INVALID_PARAM, param);
			}
		}
	}
	
	//Font Name
	private void setFontNameOption(String option, ErrorInfo errorInfo) {
		final List<String> params = this.options.getOptionParams(option);
		
		if (params.size() >= 1) {
			final String param = params.get(0);
			
			this.fontName = param;
		}
		
		if (params.size() >= 2) {
			for (final String param : params.subList(1, params.size())) {
				errorInfo.add(Message.CLO_INVALID_PARAM, param);
			}
		}
	}
	
	//Color Name
	private void setColorNameOption(String option, ErrorInfo errorInfo) {
		final List<String> params = this.options.getOptionParams(option);
		
		if (params.size() >= 1) {
			final String param = params.get(0);
			
			try {
				this.colorPair = ColorPair.valueOf(param.toUpperCase());
			} catch (IllegalArgumentException ex) {
				errorInfo.add(Message.CLO_UNKNOWN_COLOR_NAME, param);
			}
		}
		
		if (params.size() >= 2) {
			for (String param : params.subList(1, params.size())) {
				errorInfo.add(Message.CLO_INVALID_PARAM, param);
			}
		}
		
	}
	
	//private utility class for error information
	private static class ErrorInfo {
		
		//error lines
		private List<String> lines = new ArrayList<String>();
		
		void add(Message message, Object... params) {
			final String line = message.getText(params);
			this.lines.add(line);
		}
		
		List<String> getLines() {
			return this.lines;
		}
		
	}
	
}
