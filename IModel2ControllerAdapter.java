/**
 * 
 */
package main.mainModel;

import common.IChatAppReceiver;
import mini.miniModel.ChatRoomConnector;

/**
 * The Main Model 2 Main Controller Adapter
 * @author lonzd
 *
 */
public interface IModel2ControllerAdapter {

	/**
	 * Setup a new chatroom
	 * 
	 * @param chatRoomConnector - The chatroom connector to use for this ChatApp's new room
	 * @param myUserName - My username
	 * @return the new IChatAppReceiver that we created for ourselves (myReceiver)
	 */
	public IChatAppReceiver setupChatroom(ChatRoomConnector chatRoomConnector, String myUserName);

}
