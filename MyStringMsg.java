/**
 * 
 */
package mini.miniModel;

import common.message.type.IStringMessage;

/**
 * @author lonzd
 *
 */
public class MyStringMsg implements IStringMessage {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -5023024245552778352L;
	
	/**
	 * The msg field
	 */
	private String msg;
	
	/**
	 * Constructor 
	 * @param _msg : The msg 
	 */
	public MyStringMsg(String _msg) {
		this.msg = _msg;
	}
	
	/**
	 * @return The string of the message
	 */
	public String getString() {
		return this.msg;
	}
	

}
