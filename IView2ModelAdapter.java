/**
 * 
 */
package main.mainView;

import java.util.UUID;


/**
 * The Main View 2 Main Model Adapter
 * @author lonzd
 *
 */
public interface IView2ModelAdapter {
	
	/**
	 * Called when user presses quit button
	 * Quits the program
	 */
	public void quit();
	
	/**
	 * The view method to make a chat room
	 * @param chatName : The name of the chat room to be created
	 */
	public void viewMakeChatroom(String chatName);
	
	/**
	 * 
	 * @param address : The IP Address to connect to
	 */
	public void connect(String address);
	
	/**
	 * Method to request another user to join their chat rooms
	 * @param user : The user to be requested of
	 */
	public void request(ConnectorWrapper user);
	
	/**
	 * The method called when user logs in
	 * @param text : The user name
	 */
	public void login(String text);

	/**
	 * Invites the user to the specified chat room
	 * @param id : The id of the chat room to be invited to
	 * @param user : The user being invited 
	 */
	public void invite(UUID id, ConnectorWrapper user);

}
