package mini.miniController;

import java.rmi.RemoteException;
import java.util.function.Supplier;

import javax.swing.JComponent;

import common.ChatAppDataPacket;
import common.IChatAppReceiver;
import common.IChatRoomConnector;
import common.ICmdToModelAdapter;
import common.message.IMessage;
import mini.miniModel.ChatRoomConnector;
import mini.miniView.MiniView;

/**
 * @author Jacob
 *
 */
public class Cmd2ModelAdpt implements ICmdToModelAdapter {
	
	/**
	 * My IChatAppReceiver
	 */
	private IChatAppReceiver myReceiver;
	
	/**
	 * My Controller
	 */
	private MiniController myController;
	
	/**
	 * My view
	 */
	private MiniView view;
	
	/**
	 * The chat room connector
	 */
	private IChatRoomConnector chatRoomConnector;
	
	/**
	 * The constructor
	 * @param _myReceiver : my receiver stub
	 * @param _myController : my controller
	 * @param _view : my mini view
	 * @param _chatRoomConnector : the chat room connector
	 */
	public Cmd2ModelAdpt(IChatAppReceiver _myReceiver, MiniController _myController, MiniView _view, IChatRoomConnector _chatRoomConnector) {
		this.myReceiver = _myReceiver;
		this.myController = _myController;
		this.view = _view;
		this.chatRoomConnector = _chatRoomConnector;
	}
	

	@Override
	public void displayComponent(Supplier<JComponent> compSupplier, String title) {
		myController.getView().vlp.addComponent(title, compSupplier);
		
	}

	@Override
	public void displayScrollComponent(Supplier<JComponent> compSupplier, String title) {
		myController.getView().vlp.addComponent(title, compSupplier);
		
	}

	@Override
	public void displayString(String text) {
		view.display(text);
		
	}

	@Override
	public String getLocalUserName() {
		try {
			return myReceiver.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			return "Invalid username";
		}
	}

	@Override
	public String getCurrentChatRoomName() {
		return myController.getChatRoomConnector().getChatRoomName();
	}

	@Override
	public void sendMessage(IMessage message) {
		((ChatRoomConnector) myController.getChatRoomConnector()).sendMsgToAll(message, myReceiver);
		
	}

	@Override
	public void sendMessage(IMessage message, IChatAppReceiver receiver) {
		ChatAppDataPacket<? extends IMessage> dp = new ChatAppDataPacket<>(message, myReceiver);
		try {
			receiver.receiveMessage(dp);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * @return the chatRoomConnector
	 */
	public IChatRoomConnector getChatRoomConnector() {
		return chatRoomConnector;
	}

}
