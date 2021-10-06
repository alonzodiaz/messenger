/**
 * 
 */
package main.mainModel;

import java.util.List;
import java.util.Set;

import common.IChatAppReceiver;
import common.IChatRoomConnector;
import provided.datapacket.DataPacketAlgo;
import mini.miniView.MiniView;


/**
 * @author lonzd
 *
 */
public interface IMainModel2MiniControllerAdapter {
	
	/**
	 * @return The Chat Room Connector of the Mini Controller
	 */
	public IChatRoomConnector getChatRoomConnector();
	
	/**
	 * @return : Get members of chat room 
	 */
	public List<IChatAppReceiver> getMembers();
	
	/**
	 * @return : Get members of chat room in set form
	 */
	public Set<IChatAppReceiver> getMemberSet();
	/**
	 * @return The name of the chat room
	 */
	public String getChatName();

	/**
	 * Adds a new member to the member list of the chat room (both model and view)
	 * @param newMember : The new member to be added to the chat room members list
	 */
	public void addNewMember(IChatAppReceiver newMember);

	/**
	 * @return The visitor algo
	 */
	public DataPacketAlgo<Void, Void> getVisitAlgo();

	/**
	* @return The me receiver 
	*/
	public IChatAppReceiver getMeReceiver();

	/**
	 * @return the view of the controller
	 */
	public MiniView getView();


}
