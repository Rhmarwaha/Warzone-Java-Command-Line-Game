package logger;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;


/**
 * class that keeps track and modifies log messages from the console
 * 
 * @author Raghav
 * @version build 2
 *
 */
public class ConsoleWriter implements Observer, Serializable {

	private String d_logMsg;
	
	/**
	 * Method to update the current object's log message
	 * 
	 * @param p_observable - Object that is the source of change 
	 * @param p_logObject - Object that contains the information about the change
	 */
	@Override
	public void update(Observable p_observable, Object p_logObject) {
		this.setLogMsg((String) p_logObject);
		System.out.println(d_logMsg);
	}
	
	/**
	 * Getter method to retrieve log message
	 * @return logMsg log message
	 */
	public String getLogMsg() {
		return d_logMsg;
	}
	
	/**
	 * Setter method to set the log message
	 * @param logMsg log message
	 */
	public void setLogMsg(String logMsg) {
		this.d_logMsg = logMsg;
	}
	
}