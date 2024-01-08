package logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * class for handling exceptions based on the messages passed to its method
 * @author Raghav
 * @version build 2
 */
public class GeneralException extends Exception implements Serializable {

	/**
	 * default constructor
	 */
	public GeneralException() {
	}

	private static final long serialVersionUID = 1L;
	
	/**
	 * constructor to initialize p_message
	 * @param p_message - exception message
	 */
	public GeneralException(String p_message) {
		super(p_message);
	}
	
	
	/**
	 * 
	 * Method to validate the input command.
	 * The input command cannot be null, empty string or contain a special character
	 * 
	 * @param p_message - input command
	 * @return validated - input command
	 * @throws GeneralException - if input command is invalid
	 */
	public String validateCommand(String p_message) throws GeneralException {
		
		if (p_message == null || "".equals(p_message)) {
			throw new GeneralException(" Command shouldn't be null or empty ");
		}

		List<Character> specialCharacters = new ArrayList<>();
		String chars = "!#$%&'()*+,/:;<=>?@[]^_`{|}~";
        for(int i=0;i<chars.length();i++)
        {
        	specialCharacters.add(chars.charAt(i));
             
        }
		for(int i=0; i<p_message.length();i++)
		{
			if(specialCharacters.contains(p_message.charAt(i)))
			{
				throw new GeneralException(" Command shouldn't have special character other than dash");

			}
		}
		return p_message;
		
	}
}
