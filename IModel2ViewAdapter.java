package main.mainModel;

import common.IChatAppConnector;
import mini.miniView.MiniView;

/**
 * The Main Model 2 Main View Adapter
 * @author lonzd
 *
 */
public interface IModel2ViewAdapter {
	
	/**
	 * Sends the view the chat room view
	 * @param chatroomView : The view of the chat room
	 * @param chatroomName : The name of the chat room
	 */
	public void sendChatroomView(MiniView chatroomView, String chatroomName);
	
	/**
	 * The method called when a chat room is deleted or left
	 * @param chatroomView : The view of the chat room to be deleted
	 */
	public void closeChatroomView(MiniView chatroomView);

	/**
	 * The method to have the view start the discovery server
	 */
	public void startDiscovery();

	/**
	 * Have the view recognize that a new user was connected
	 * @param newConnection : The new user to be connected
	 * @param friendlyName : The friendly name of the user
	 */
	public void addConnectedUser(IChatAppConnector newConnection, String friendlyName);

	/**
	 * Removes All the mini views of the chat rooms
	 */
	public void removeAllChatroomView();

	/**
	 * Remove the given user from the drop down box view
	 * @param connector : the user to remove from the drop down box 
	 */
	public void removeConnectedUserView(IChatAppConnector connector);

}
