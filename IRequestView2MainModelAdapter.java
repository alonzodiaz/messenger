/**
 * 
 */
package main.mainView;

import common.IChatRoomConnector;

/**
 * The adapter from the request view to the main model
 * @author lonzd
 *
 */
public interface IRequestView2MainModelAdapter {
	
	/**
	 * After hitting request and seeing the chat rooms, user has made their choice and wants to join the selected chatroom
	 * @param room : The room the user wants to join now
	 */
	public void requestJoinChatroom(IChatRoomConnector room);

}
