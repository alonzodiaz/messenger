/**
 * 
 */
package mini.miniModel;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import common.ChatAppDataPacket;
import common.IChatAppReceiver;
import common.IChatRoomConnector;
import common.message.IMessage;

/**
 * @author lonzd
 *
 */
public class ChatRoomConnector implements IChatRoomConnector {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -8145035265313576962L;
	/**
	 * 
	 */
	String chatName;

	/**
	 * The members of the chat room
	 */
	private Set<IChatAppReceiver> members;

	/**
	 * Unique ID
	 */
	private final UUID id;

	/**
	 * @param _members : The members of the chat room
	 * @param _chatName : The name of the chat room
	 */
	public ChatRoomConnector(Set<IChatAppReceiver> _members, String _chatName) {
		this.members = _members;
		this.chatName = _chatName;
		this.id = UUID.randomUUID();

	}
	
	/**
	 * @param _members : The members of the chat room
	 * @param _chatName : The name of the chat room
	 * @param _id : the id
	 */
	public ChatRoomConnector(Set<IChatAppReceiver> _members, String _chatName, UUID _id) {
		this.members = _members;
		this.chatName = _chatName;
		this.id = _id;

	}

	/**
	 * @param _chatName : The name of the chat room
	 */
	public ChatRoomConnector(String _chatName) {
		this(new HashSet<>(), _chatName);
	}

	@Override
	public String getChatRoomName() {
		return this.chatName;
	}

	@Override
	public UUID getID() {
		return this.id;
	}

	@Override
	public List<IChatAppReceiver> getAllReceivers() {
		List<IChatAppReceiver> result = new ArrayList<>();
		result.addAll(this.members);
		return result;
	}

	/**
	 * @param newReceiver : The new member stub to be added
	 */
	public void addNewReceiver(IChatAppReceiver newReceiver) {
		this.members.add(newReceiver);

	}

	/**
	 * @return the members
	 */
	public Set<IChatAppReceiver> getMemberSet() {
		return this.members;
	}

	/**
	 * Deletes the receiver 
	 * @param sender : The receiver to be deleted
	 */
	public void deleteReceiver(IChatAppReceiver sender) {
		this.members.remove(sender);

	}

	@Override
	public boolean equals(Object o) {
		if (null != o) {
			if (o instanceof ChatRoomConnector) {
				return this.getID().equals(((ChatRoomConnector) o).getID());
			}
		}
		return false;
	}

	/**
	 * Sends a message to all the receivers in the chat room
	 * @param msg : The message to be sent
	 * @param receiver : the sender of the message
	 */
	public void sendMsgToAll(IMessage msg, IChatAppReceiver receiver) {
		ChatAppDataPacket<? extends IMessage> dp = new ChatAppDataPacket<>(msg, receiver);
		Set<IChatAppReceiver> stubs = new HashSet<>(members);
		for (IChatAppReceiver receiverStub : stubs) {
			try {
				receiverStub.receiveMessage(dp);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}						

	}

}
