package model;

/**
 * class for managing responses with status values and descriptions
 * @author Rohit
 * @version build 2
 */
public class ResponseWrapper {
	/**
	 * integer status value
	 */
	private int d_statusValue;
	/**
	 * string response description
	 */
	private String d_description;
	
	/**
	 * getter method for status value
	 * @return d_statusValue
	 */
	public int getStatusValue() {
		return d_statusValue;
	}
	
	/**
	 * parameterized constructor
	 * @param p_statusValue - status value/code
	 * @param p_description - description of the status
	 */
	public ResponseWrapper(int p_statusValue, String p_description) {
		this.d_statusValue = p_statusValue;
		this.d_description = p_description;
	}
	
	/**
	 * setter method for status value
	 * @param p_statusValue - value of status
	 */
	public void setStatusValue(int p_statusValue) {
		this.d_statusValue = p_statusValue;
	}
	
	/**
	 * getter method for description
	 * @return d_description - description
	 */
	public String getDescription() {
		return d_description;
	}
	
	/**
	 * setter method for description
	 * @param description - description
	 */
	public void setDescription(String description) {
		this.d_description = description;
	}
	
	
	

}
