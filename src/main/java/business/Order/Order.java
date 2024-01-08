package business.Order;

import model.ResponseWrapper;

/**
 * Interface that defines basic methods for an order to be implemented
 * 
 * @author Rohit
 * @version build 2
 */
public interface Order {

	/**
	 * Performs execution of the specific order after order validation
	 */
	public void execute();
	
	/**
	 * Checks if entered order is valid or not
	 * @return boolean value depending on the validation of the order
	 */
	public boolean valid();
	
	/**
	 * prints occurrence of the order after execution
	 */
	public void printOrder();
	
	
	public ResponseWrapper getOrderStatus();
	
}
