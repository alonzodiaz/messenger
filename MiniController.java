/**
 * 
 */
package mini.miniController;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import common.AMessageAlgoCmd;
import common.ChatAppDataPacket;
import common.IChatAppReceiver;
import common.IChatRoomConnector;
import common.message.IMessage;
import common.message.type.IJoinMessage;
import common.message.type.ILeaveMessage;
import common.message.type.IStringMessage;
import common.message.type.cmd.IAddCmdMessage;
import common.message.type.cmd.IRequestCmdMessage;
import common.message.type.error.IErrorMessage;
import common.message.type.error.IFailureMessage;
import common.message.type.error.IRejectMessage;
import main.mainModel.IMainModel2MiniControllerAdapter;
import mini.miniModel.ChatRoomConnector;
import mini.miniModel.MiniModel;
import mini.miniModel.MyStringMsg;
import mini.miniModel.IMiniModel2MiniViewAdpt;
import mini.miniView.MiniView;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.IDataPacketID;
import mini.miniView.IMiniView2MiniControllerAdpt;

/**
 * @author Alonzo Diaz and Jacob Lu
 *
 */
public class MiniController {
	/**
	 * Sendable message types
	 */
	public static Map<String, IDataPacketID> sendMsgTypes = Map.of("String", IStringMessage.GetID(), "Reverse", ReversedMsg.GetID()); // "Special", SpecialMsg.GetID()
	
	/**
	 * The mini view
	 */
	private MiniView view;

	/**
	 * The mini model
	 */
	private MiniModel model;

	/**
	 * Mini Controller to Main Model Adapter
	 */
	private IMiniController2MainModelAdpt miniControl2MainModelAdpt;

	/**
	 * The Main Model to Mini Controller Adapter
	 */
	private IMainModel2MiniControllerAdapter mainModel2MiniControlAdpt;

	/**
	 * The IChatRoomConnector
	 */
	private IChatRoomConnector chatRoomConnector;

	/**
	 * The receiver that corresponds to my stub in this chatroom
	 */
	private IChatAppReceiver myReceiver;

	/**
	 * Cache of unprocessed unknown messages, stored by ID.
	 */
	private HashMap<IDataPacketID, List<ChatAppDataPacket<IMessage>>> msgCache;

	/**
	 * For reference inside commands
	 */
	private MiniController myController = this;

	/**
	 * The visitor for the messages
	 */
	private DataPacketAlgo<Void, Void> visitAlgo = new DataPacketAlgo<Void, Void>(new AMessageAlgoCmd<IMessage>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2569896167512802287L;

		@Override
		public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
			return null;
		}

	});
	

	/**
	 * Construct a miniController
	 * 
	 * @param _miniControl2MainModelAdpt : Adpt from MiniController to MainModel
	 * @param _chatRoomConnector         : The chat room connector
	 * @param personName                 : The username of the receiver
	 * 
	 */
	public MiniController(IMiniController2MainModelAdpt _miniControl2MainModelAdpt,
			ChatRoomConnector _chatRoomConnector, String personName) {
		this.miniControl2MainModelAdpt = _miniControl2MainModelAdpt;
		this.chatRoomConnector = _chatRoomConnector;
		this.myReceiver = makeReceiver(personName);
		this.msgCache = new HashMap<>();

		// The mini-controller, upon construction, should create an adapter
		// that allows the main model to access methods on it.
		this.mainModel2MiniControlAdpt = new IMainModel2MiniControllerAdapter() {

			@Override
			public IChatRoomConnector getChatRoomConnector() {
				return chatRoomConnector;
			}

			@Override
			public String getChatName() {
				return _chatRoomConnector.getChatRoomName();
			}

			@Override
			public void addNewMember(IChatAppReceiver newMember) {
				addNewReceiverCntrl(newMember);

			}

			@Override
			public List<IChatAppReceiver> getMembers() {
				return getChatRoomConnector().getAllReceivers();
			}

			@Override
			public Set<IChatAppReceiver> getMemberSet() {
				return ((ChatRoomConnector) getChatRoomConnector()).getMemberSet();
			}

			@Override
			public DataPacketAlgo<Void, Void> getVisitAlgo() {
				return visitAlgo;
			}

			@Override
			public IChatAppReceiver getMeReceiver() {
				return myReceiver;
			}

			@Override
			public MiniView getView() {
				return view;
			}
		};

		model = new MiniModel(new IMiniModel2MiniViewAdpt() {

			@Override
			public void display(String text) {
				getView().display(text);

			}
		});

		view = new MiniView(new IMiniView2MiniControllerAdpt() {

			@Override
			public void leaveChatroom() {
				miniControl2MainModelAdpt.leaveChatroom(getID(), getView());

			}

			@Override
			public UUID getID() {
				return getChatRoomConnector().getID();
			}

			@Override
			public void sendMsg(String msg, IDataPacketID msgType) {
				if (msgType.equals(IStringMessage.GetID())) {
					((ChatRoomConnector) getChatRoomConnector()).sendMsgToAll(new MyStringMsg(msg), myReceiver);
				} else if (msgType.equals(ReversedMsg.GetID())) {
					((ChatRoomConnector) getChatRoomConnector()).sendMsgToAll(new ReversedMsg(msg), myReceiver);
				}
				
			}

			@Override
			public List<String> getAllReceiverNames() {
				ArrayList<String> result = new ArrayList<>();
				for(IChatAppReceiver c : getChatRoomConnector().getAllReceivers()) {
					try {
						result.add(c.getName());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				return result;
			}

		});

		// Setup visitor command processing for well-known message types
		setUpVisitor();
	}

	/**
	 * Creates an IChatAppReceiver
	 * 
	 * @param personName : The name of the receiver
	 * @return The IChatAppReceiver made with name personName
	 */
	public IChatAppReceiver makeReceiver(String personName) {

		IChatAppReceiver newReceiver = new IChatAppReceiver() {

			@Override
			public String getName() throws RemoteException {
				return personName;
			}

			@Override
			public void receiveMessage(ChatAppDataPacket<? extends IMessage> message) throws RemoteException {
				System.out.println("Receive Message");
				message.execute(visitAlgo);

			}

		};
		return newReceiver;
	}

	/**
	 * controller start function
	 */
	public void start() {
		getView().start();
		model.start();
	}

	/**
	 * @return The Main Model 2 Mini Controller Adapter
	 */
	public IMainModel2MiniControllerAdapter getMainModel2MiniControlAdapter() {
		return this.mainModel2MiniControlAdpt;
	}

	/**
	 * @return The mini view
	 */
	public MiniView getMiniView() {
		return getView();
	}

	/**
	 * @return The id of the chat room
	 */
	public UUID getID() {
		return this.getChatRoomConnector().getID();
	}

	/**
	 * @param newChatRoomConnector : The new Chat Room Connector to be set
	 */
	public void addChatRoomConnector(IChatRoomConnector newChatRoomConnector) {
		this.chatRoomConnector = newChatRoomConnector;

	}

	/**
	 * @return The name of the chat room
	 */
	public String getChatRoomName() {
		return this.getChatRoomConnector().getChatRoomName();
	}

	/**
	 * @return myReceiver
	 */
	public IChatAppReceiver getMyReceiver() {
		return myReceiver;
	}

	/**
	 * @param newReceiver Add a new receiver stub to the chat room
	 */
	public void deleteReceiverCntrl(IChatAppReceiver newReceiver) {
		((ChatRoomConnector) this.getChatRoomConnector()).deleteReceiver(newReceiver);
		getView().deleteReceiverView(newReceiver);
	}

	/**
	 * @param newReceiver Add a new receiver stub to the chat room
	 */
	public void addNewReceiverCntrl(IChatAppReceiver newReceiver) {
		((ChatRoomConnector) this.getChatRoomConnector()).addNewReceiver(newReceiver);
		getView().addNewReceiverView(newReceiver);
	}

	

	/**
	 * Does the set up for the commands in visitAlgo for the well-known msg types
	 */
	private void setUpVisitor() {

		// Command for the IJoinMessage
		visitAlgo.setCmd(IJoinMessage.GetID(), new AMessageAlgoCmd<IMessage>() {
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -3895104518615189542L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				System.out.println("inside join message");

				try {
					String senderName = host.getSender().getName();
					addNewReceiverCntrl(host.getSender());

					getView().display(senderName + " has joined the chat room!");

				} catch (RemoteException e) {
					e.printStackTrace();
				}

				return null;
			}
		});

		// Command for the ILeaveMessage
		visitAlgo.setCmd(ILeaveMessage.GetID(), new AMessageAlgoCmd<IMessage>() {
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -3895104518615189542L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				// Remove sender from our chatroom
				try {
					String senderName = host.getSender().getName();
					deleteReceiverCntrl(host.getSender());
					getView().display(senderName + " has left the chat room!");

				} catch (RemoteException e) {
					e.printStackTrace();
				}

				return null;
			}
		});

		// Command for the IStringMessage
		visitAlgo.setCmd(IStringMessage.GetID(), new AMessageAlgoCmd<IMessage>() {
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -3895104518615189542L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				try {
					String senderName = host.getSender().getName();
					getView().display("[" + senderName + "]: " + ((IStringMessage) host.getData()).getString());

				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		// Command for the IAddCmdMessage
		visitAlgo.setCmd(IAddCmdMessage.GetID(), new AMessageAlgoCmd<IMessage>() {
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -3895104518615189542L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				// Install the new command
				IAddCmdMessage newCmd = (IAddCmdMessage) host.getData();
				IDataPacketID newID = newCmd.getUnknownID();
				AMessageAlgoCmd<? extends IMessage> newAlgo = newCmd.getUnknownAlgoCmd();
				
				newAlgo.setCmd2ModelAdpt(new Cmd2ModelAdpt(myReceiver, myController, view, chatRoomConnector));
				visitAlgo.setCmd(newID, newAlgo);
				
				// Process the cache
				if (myController.msgCache.containsKey(newID)) {
					List<ChatAppDataPacket<IMessage>> dataPackets = myController.msgCache.remove(newID);
					for (ChatAppDataPacket<IMessage> dp : dataPackets) {
//						try {
							newAlgo.apply(newID, dp, params);
//							myReceiver.receiveMessage(dp);
							
//						} catch (RemoteException e) {
//							System.out.println("RemoteException in AddCmdMsg processing of cache");
//							e.printStackTrace();
//						}
					}
				}

				return null;
			}
		});

		// Command for the IRequestCmdMessage
		visitAlgo.setCmd(IRequestCmdMessage.GetID(), new AMessageAlgoCmd<IMessage>() {
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -3895104518615189542L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				IRequestCmdMessage requestMsg = (IRequestCmdMessage) host.getData();

				IDataPacketID cmdId = requestMsg.getUnknownID();
				
				// Send an add message back to the sender of the request

				ChatAppDataPacket<IMessage> dp = new ChatAppDataPacket<IMessage>(new MyAddMsg(cmdId, visitAlgo), myReceiver);
				
				try {
					host.getSender().receiveMessage(dp);
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		// Default
		visitAlgo.setDefaultCmd(new AMessageAlgoCmd<IMessage>() {
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = -3895104518615189542L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				// Send request to sender for the right algorithm to be installed
				IDataPacketID hostID = host.getData().getID();

				ChatAppDataPacket<? extends IMessage> dp = new ChatAppDataPacket<>(new MyRequestMsg(hostID), myReceiver);

				try {
					host.getSender().receiveMessage(dp);

				} catch (RemoteException e) {
					e.printStackTrace();
				}

				// Cache the unkown message by it's ID
				// Then when we get the new algo in AddMsg, we apply the algo
				// to the messages in the cache
				if (!(myController.msgCache.containsKey(hostID))) {
					myController.msgCache.put(hostID, new ArrayList<>());
				}
				myController.msgCache.get(hostID).add(host);
				System.out.println(myController.msgCache.get(hostID).toString());

				return null;
			}
		});
		
		visitAlgo.setCmd(IRejectMessage.GetID(), new AMessageAlgoCmd<IMessage>() {

			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = 3174343615609212270L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				try {
					String senderName = host.getSender().getName();
					getView().display("Rejected Request Message: You sent " + senderName + " a request for a well-known message type.");

				} catch (RemoteException e) {
					e.printStackTrace();
				}

				return null;
			}
		});
		
		visitAlgo.setCmd(IFailureMessage.GetID(), new AMessageAlgoCmd<IMessage>() {

			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = 3174343615609212270L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				try {
					String senderName = host.getSender().getName();
					getView().display("Failure Message: " + senderName + " sent an indication that something failed!");

				} catch (RemoteException e) {
					e.printStackTrace();
				}

				return null;
			}
		});
		
		visitAlgo.setCmd(IErrorMessage.GetID(), new AMessageAlgoCmd<IMessage>() {

			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = 3174343615609212270L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				try {
					String senderName = host.getSender().getName();
					getView().display("Error Message: " + senderName + " sent an indication that something went wrong!");

				} catch (RemoteException e) {
					e.printStackTrace();
				}

				return null;
			}
		});
		
		visitAlgo.setCmd(ReversedMsg.GetID(), new ReverseCmd());
	}

	/**
	 * @return The mini view
	 */
	public MiniView getView() {
		return view;
	}

	/**
	 * @return The chat room connector
	 */
	public IChatRoomConnector getChatRoomConnector() {
		return chatRoomConnector;
	}

}
