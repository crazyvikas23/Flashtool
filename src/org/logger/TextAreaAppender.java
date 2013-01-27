package org.logger;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.log4j.Level;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.system.OS;

public class TextAreaAppender extends WriterAppender {
	
	static private StyledText styledText = null;
	static private StringBuilder builder = new StringBuilder();
	public static String timestamp=getTimeStamp();

    public static String getTimeStamp() {
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
    	df.setTimeZone( TimeZone.getTimeZone("PST"));  
    	String date = ( df.format(new Date()));    
    	DateFormat df1 = new SimpleDateFormat("hh-mm-ss") ;    
    	df1.setTimeZone( TimeZone.getDefault()) ;  
    	String time = ( df1.format(new Date()));
    	return date+"_"+time;
    }

	public static void writeFile() {
		String logname=OS.getWorkDir()+OS.getFileSeparator()+"flashtool_"+timestamp+".log";
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(logname));
			writer.write(builder.toString());
		}
		catch (IOException exception) {}
		finally {
			if (writer != null) {
				try {
					MyLogger.getLogger().info("Log written to "+logname);
					writer.close();
				}
				catch (Exception exception) {}
			}
		}
	
	}

	/** Set the target JTextArea for the logging information to appear. */
	static public void setTextArea(StyledText styledText) {
		TextAreaAppender.styledText = styledText;
	}

	/**
	 * Format and then append the loggingEvent to the stored
	 * JTextArea.
	 */
	public void append(LoggingEvent loggingEvent) {
		final String message = this.layout.format(loggingEvent);
		final Level level = loggingEvent.getLevel();
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				// Append formatted message to textarea using the Swing Thread.
				builder.append(message);
				if (level==Level.ERROR) {
					append(styledText.getDisplay().getSystemColor(SWT.COLOR_RED),message);
				}
				else if (level==Level.WARN) {
					append(styledText.getDisplay().getSystemColor(SWT.COLOR_BLUE),message);
				}
				else {
					append(styledText.getDisplay().getSystemColor(SWT.COLOR_BLACK),message);
				}
			}
		});

	}
	
	public static void append(final Color color, final String message) {
		if (styledText != null) {
					styledText.setForeground(color);
					styledText.append(message);
		}
    }

	public String getString() {
		return builder.toString();
	}
}