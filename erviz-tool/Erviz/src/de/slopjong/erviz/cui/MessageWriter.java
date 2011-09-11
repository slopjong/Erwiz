package de.slopjong.erviz.cui;

import java.text.SimpleDateFormat;
import java.util.Date;

//import java.nio.charset.Charset;

/**
 * A writer class for console messages.
 * 
 * @author kono
 */
final class MessageWriter {

	private boolean debugMode = false;
	
	public MessageWriter() {
	}
	
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	public void write(String msg, int numOfNewLines) {
		try {
			//system default encoding
			System.err.print(msg);
			for (int i = 0; i < numOfNewLines; i++) {
				System.err.println();
			}
			
			//fixed encoding
			//final String newLine = System.getProperties().getProperty("line.separator");
			//for (int i = 0; i < numOfNewLines; i++) {
			//	msg += newLine;
			//}
			//byte[] bytes = msg.getBytes("UTF-8");
			//System.err.write(bytes);
			
			//system default encoding
			//final String newLine = System.getProperties().getProperty("line.separator");
			//for (int i = 0; i < numOfNewLines; i++) {
			//	msg += newLine;
			//}
			//byte[] bytes = msg.getBytes(Charset.defaultCharset());
			//System.err.write(bytes);
			
		} catch (Exception ex) {
			System.err.println(ex);
			return;
		}
	}
	
	public void newLine() {
		write("", 1);
	}
	
	public void debug(String msg, int numOfNewLines) {
		debug(msg, numOfNewLines, false);
	}
	
	public void debug(String msg, int numOfNewLines, boolean showTime) {
		if (this.debugMode) {
			if (showTime) {
				msg += " [" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()) + "]";
			}
			write(msg, numOfNewLines);
		}
	}
	
	public void exception(Exception... exceptions) {
		
		for (Exception ex : exceptions) {
			//always
			write(ex.getMessage(), 1);
			
			//debug mode only
			debug(ex.toString(), 1);
			for (StackTraceElement trace : ex.getStackTrace()) {
				debug(trace.toString(), 1);
			}
		}
		
	}
	
}
