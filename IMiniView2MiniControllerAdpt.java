package mini.miniView;

import java.util.UUID;
import java.util.List;

import provided.datapacket.IDataPacketID;

/**
 * @author lonzd
 *
 */
public interface IMiniView2MiniControllerAdpt {
	/**
	 * Leaves the chat room by closing out the view
	 */
	public void leaveChatroom();

	/**
	 * @return The id of the tab that is the chat room
	 */
	public UUID getID();

	/**
	* Sends a message in the chat room
	* @param msg : The msg to be sent 
	* @param msgType : the type of the msg to be sent 
	*/
	public void sendMsg(String msg, IDataPacketID msgType);

	/**
	 * @return A list of all the receiver in the chat room already
	 */
	public List<String> getAllReceiverNames();

}
