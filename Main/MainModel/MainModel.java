package main.mainModel;

import java.util.UUID;
import java.util.function.Consumer;

import common.IChatAppConnector;
import common.IChatAppReceiver;
import common.IChatRoomConnector;
import common.message.type.ILeaveMessage;
import main.mainController.MyJoinMsg;
import main.mainView.ConnectorWrapper;
import main.mainView.IRequestView2MainModelAdapter;
import main.mainView.RequestListView;
import mini.miniController.MiniController;
import mini.miniModel.ChatRoomConnector;
import mini.miniView.MiniView;
import provided.datapacket.IDataPacketID;
import provided.discovery.IEndPointData;
import provided.discovery.impl.model.DiscoveryConnector;
import provided.discovery.impl.model.RemoteAPIStubFactory;
import provided.rmiUtils.RMIUtils;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

/**
 * The Main Model behind a ChatApp program
 * 
 * @author Jacob Lu and Alonzo Diaz
 *
 */
public class MainModel {
	/**
	 * The adapter to the view.
	 */
	private IModel2ViewAdapter model2ViewAdpt;

	/**
	 * The adapter to the controller.
	 */
	private IModel2ControllerAdapter model2ControlAdpt;

	/**
	 * The hash map of the ID of chat rooms to their main model 2 mini controller
	 * adapters
	 */
	private Map<UUID, IMainModel2MiniControllerAdapter> joinedChatrooms = new HashMap<>();

	/**
	 * The hash map of users this ChatApp has connected to Mapping of the users to
	 * their friendly names
	 */
	private Map<IChatAppConnector, String> connectedUsers = new HashMap<>();

	/**
	 * The RMI Utils object
	 */
	private RMIUtils rmiUtils = new RMIUtils(msg -> {
	});

	/**
	 * The port to export stubs
	 */
	private int stubsPort;

	/**
	 * The local registry
	 */
	private Registry registry;

	/**
	 * The bound name for the user in the registry
	 */
	private String myBoundName;

	/**
	 * The user name of the ChatApp
	 */
	private String userName;

	/**
	 * The RemoteAPIStubFactory of IChatAppConnectors
	 */
	private RemoteAPIStubFactory<IChatAppConnector> remoteAPIStubFactory;

	/**
	 * The discovery connector - needed to connect to discovery server
	 */
	private DiscoveryConnector discoveryConnector;

	/**
	 * This ChatApp user
	 */
	private IChatAppConnector me;

	/**
	 * The stub of this ChatApp
	 */
	private IChatAppConnector myStub;
	
	/**
	 * Concrete implementation of ILeaveMessage
	 * 
	 * @author Jacob Lu
	 *
	 */
	private static class MyLeaveMsg implements ILeaveMessage {
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = -358295060181259995L;

		@Override
		public IDataPacketID getID() {
			return ILeaveMessage.GetID();
		}
	};

	/**
	 * The constructor for the Main Model
	 * 
	 * @param _model2ViewAdpt    : the Main Model 2 Main View Adapter
	 * @param _model2ControlAdpt : The Main Model 2 Main Controller Adapter
	 */
	public MainModel(IModel2ViewAdapter _model2ViewAdpt, IModel2ControllerAdapter _model2ControlAdpt) {
		this.model2ViewAdpt = _model2ViewAdpt;
		this.model2ControlAdpt = _model2ControlAdpt;
	}

	/**
	 * Main model method to make a new chat room
	 * 
	 * @param chatName : The string to be displayed on chat room tab
	 */
	public void modelMakeChatroom(String chatName) {
		ChatRoomConnector newRoom = new ChatRoomConnector(chatName);
		IChatAppReceiver newReceiver = this.model2ControlAdpt.setupChatroom(newRoom, userName);
		try {
			IChatAppReceiver newReceiverStub = (IChatAppReceiver) UnicastRemoteObject
					.exportObject(newReceiver, stubsPort);
			
			this.joinedChatrooms.get(newRoom.getID()).addNewMember(newReceiverStub);
			
		} catch (RemoteException e) {
			System.out.println("RemoteException");
			e.printStackTrace();
		}

	}

	/**
	 * Adds a chat room to the hash map of chat rooms
	 * 
	 * @param newChatroom : The new chat room being added
	 * @param adapter     : The adapter of the chat room to be added
	 */
	public void addChatroom(MiniController newChatroom, IMainModel2MiniControllerAdapter adapter) {
		this.joinedChatrooms.put(newChatroom.getID(), adapter);
	}

	/**
	 * Starts the Main Model
	 * 
	 * @param rmiPort   : The port to start the RMI
	 * @param stubsPort : The port to export stubs
	 */
	public void start(int rmiPort, int stubsPort) {
		// Starts the RMI at the specified RMI Port
		rmiUtils.startRMI(rmiPort);

		this.stubsPort = stubsPort;

		this.remoteAPIStubFactory = new RemoteAPIStubFactory<>(rmiUtils);
	}

	/**
	 * When the ChatApp quits
	 */
	public void quit() {
		// Remove ourselves from every chatroom
		Set<UUID> ids = new HashSet<>(joinedChatrooms.keySet());
		for (UUID id1 : ids) {
			leaveChatroom(id1, joinedChatrooms.get(id1).getView());
		}
		
		// disconnect from every connector
		for (IChatAppConnector c : this.connectedUsers.keySet()) {
			try {
				c.disconnect(me);
			} catch (RemoteException e) {
				System.out.println("RemoteException in MainModel.quit");
				e.printStackTrace();
			}
		}

		// Clear all the connected users
		connectedUsers.clear();

		// Disconnect from the discovery server
		if (!(discoveryConnector == null)) {
			discoveryConnector.disconnect();
		}

		try {
			// Unbind this ChatApp from the registry
			registry.unbind(myBoundName);
		} catch (Exception e) {
			System.out.println("Error while unbinding chat app user from registry: " + e);
			e.printStackTrace();
		}
		// Stop the RMI
		rmiUtils.stopRMI();
	}

	/**
	 * The method called when the user presses the start button
	 * Connects the user to the discovery server and create a connector
	 * 
	 * @param username : The user name of the user
	 */
	public void login(String username) {
		this.userName = username;
		this.myBoundName = this.userName + IChatAppConnector.BOUND_NAME;

		try {
			// Instantiate the discovery connected
			discoveryConnector = new DiscoveryConnector(rmiUtils, userName, myBoundName);

			// Instantiate the RemoteAPIStubFactory
			this.remoteAPIStubFactory = new RemoteAPIStubFactory<>(rmiUtils);

			// Instantiate this ChatApp program as an IChatAppConnector
			me = new IChatAppConnector() {

				@Override
				public String getName() throws RemoteException {
					return userName;
				}

				@Override
				public void invite(IChatRoomConnector connector) throws RemoteException {
					System.out.println("Starting invite from IChatAppConnector");
					ChatRoomConnector newChatConnect = new ChatRoomConnector(new HashSet<>(connector.getAllReceivers()), connector.getChatRoomName(), connector.getID());

					IChatAppReceiver newReceiver = model2ControlAdpt.setupChatroom(newChatConnect, userName);
					

					try {
						IChatAppReceiver newReceiverStub = (IChatAppReceiver) UnicastRemoteObject
								.exportObject(newReceiver, stubsPort);	
						
						newChatConnect.sendMsgToAll(new MyJoinMsg(), newReceiverStub);
						
						joinedChatrooms.get(newChatConnect.getID()).addNewMember(newReceiverStub);
						
						System.out.println("IChatAppConnector.invite finished");

					} catch (RemoteException e) {
						System.out.println("RemoteException in MainModel IChatAppConnector.invite");
						e.printStackTrace();
					}
				}

				@Override
				public List<IChatRoomConnector> request() throws RemoteException {
					ArrayList<IChatRoomConnector> result = new ArrayList<IChatRoomConnector>();
					for (UUID id : joinedChatrooms.keySet()) {
						result.add(joinedChatrooms.get(id).getChatRoomConnector());
					}

					return result;
				}

				@Override
				public void autoConnect(IChatAppConnector connector) throws RemoteException {
					// TODO: I think this won't work with multiple people

					if (!(connectedUsers.containsKey(connector))) {
						userConnect(connector, connector.getName());
					}

					for (IChatAppConnector c : connector.getIChatAppConnectors()) {
						if (!(connectedUsers.containsKey(c))) {
							userConnect(c, c.getName());
							c.autoConnect(myStub);
						}
					}
				}

				@Override
				public List<IChatAppConnector> getIChatAppConnectors() throws RemoteException {
					return new ArrayList<IChatAppConnector>(connectedUsers.keySet());
				}

				@Override
				public void disconnect(IChatAppConnector connector) throws RemoteException {
					model2ViewAdpt.removeConnectedUserView(connector);
					connectedUsers.remove(connector);

				}


			};

			// Create the stub of this ChatApp
			myStub = (IChatAppConnector) UnicastRemoteObject.exportObject(me, stubsPort);

			// Get local registry
			this.registry = rmiUtils.getLocalRegistry();

			// Bind user stub to the registry
			registry.rebind(myBoundName, myStub);

		} catch (RemoteException e) {
			System.out.println("RemoteException in MainModel.login");
			e.printStackTrace();
		}

		// Have the view start the discovery server
		model2ViewAdpt.startDiscovery();
		
		// Connect to ourselves.
		try {
			this.userConnect(myStub, myStub.getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The Main Model 2 Main View Adapter
	 */
	public IModel2ViewAdapter getModel2ViewAdapter() {
		return this.model2ViewAdpt;
	}

	/**
	 * Has the view close the chat room tab Removes the chat room from the set of
	 * joined chat rooms
	 * 
	 * @param id   : The id of the chat room to be left
	 * @param view : The mini view
	 */
	public void leaveChatroom(UUID id, MiniView view) {
		// Send leave message to all receivers
		ChatRoomConnector chatRoomConnector = (ChatRoomConnector) joinedChatrooms.get(id).getChatRoomConnector();
		IChatAppReceiver me = joinedChatrooms.get(id).getMeReceiver();
		chatRoomConnector.sendMsgToAll(new MyLeaveMsg(), me); 
		// Close chatroom
		this.model2ViewAdpt.closeChatroomView(view);
		this.joinedChatrooms.remove(id);
	}

	/**
	 * Connects to a specific IP address
	 * 
	 * @param address : The IP address to connect to
	 */
	public void straightConnect(String address) {
		try {
			// Get the user from the API Stub Factory using the input address
			IChatAppConnector user = remoteAPIStubFactory.getFromAddr(address, IChatAppConnector.BOUND_NAME);

			// Connect to the specific user
			userConnect(user, user.getName());

			// AutoConnect
			user.autoConnect(myStub);

		} catch (Exception e) {
			System.out.println("Error while connecting to user \"" + address + "\": " + e);
			e.printStackTrace();
		}

	}

	/**
	 * Connect to the discovery server
	 * 
	 * @param category       The category on the remote discovery service to monitor
	 * @param watchOnly      If True, only connect to the remote discovery service
	 *                       and watch it, otherwise also register the local
	 *                       endpoint when connecting.
	 * @param endPtsUpdateFn A Consumer of an iterable of endpoints used, for
	 *                       instance, by the DiscoveryPanel to update the list of
	 *                       active endpoints in the category.
	 */
	public void connectDiscovery(String category, boolean watchOnly, Consumer<Iterable<IEndPointData>> endPtsUpdateFn) {
		try {
			discoveryConnector.connectToDiscoveryServer(category, watchOnly, endPtsUpdateFn);
		} catch (RemoteException e) {
			System.err.println("[MainModel.connectToDiscoveryServer(" + category + ")] Exception: " + e);
			e.printStackTrace();
		}

	}

	/**
	 * Connect to an end point
	 * 
	 * @param selectedValue : The end point to connect to
	 */
	public void connectToEndPoint(IEndPointData selectedValue) {
		try {

			// Get the user from the end point data using remoteAPIStubFactory
			IChatAppConnector newConnection = remoteAPIStubFactory.get(selectedValue);

			// connect to that user
			if (!(connectedUsers.containsKey(newConnection))) {
				userConnect(newConnection, selectedValue.getFriendlyName());
			}

			// Auto-connect back
			newConnection.autoConnect(myStub);


		} catch (Exception e) {
			System.err.println("[MainModel.connectTo(" + selectedValue + ")] Exception while retrieving stub: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Adds the user and sends a message letting them know they've joined
	 * 
	 * @param newConnection : The new user to connect to
	 * @param friendlyName  : The name of the new user to connect to
	 */
	private void userConnect(IChatAppConnector newConnection, String friendlyName) {
		addUser(newConnection, friendlyName);
	}

	/**
	 * Adds a user that is being connected to
	 * 
	 * @param newConnection : New user to connect to
	 * @param friendlyName  : The name of the new user to connect to
	 */
	private void addUser(IChatAppConnector newConnection, String friendlyName) {
		if (!connectedUsers.containsKey(newConnection)) {
			connectedUsers.put(newConnection, friendlyName);
			model2ViewAdpt.addConnectedUser(newConnection, friendlyName);
		}

	}

	/**
	 * @param id          : The id of the room that the invite is for
	 * @param chatAppUser : The user that the invite is being sent to
	 */
	public void invite(UUID id, IChatAppConnector chatAppUser) {
		try {
			IChatRoomConnector newConnector = this.joinedChatrooms.get(id).getChatRoomConnector();			
			chatAppUser.invite(newConnector);

			System.out.println("Invite done from MainModel");

		} catch (RemoteException e) {
			System.out.println("RemoteException in MainModel.invite");
			e.printStackTrace();
		}
	}

	/**
	 * Requesting a list of chat rooms from the user
	 * @param user : The user who we are making a request of
	 */
	public void request(ConnectorWrapper user) {
		try {
			List<IChatRoomConnector> chatRooms = user.getConnector().request();
			RequestListView requestWindow = new RequestListView(chatRooms, new IRequestView2MainModelAdapter() {

				@Override
				public void requestJoinChatroom(IChatRoomConnector room) {
					try {
						me.invite(room);

					} catch (RemoteException e) {
						e.printStackTrace();
					}


				}

			});
			requestWindow.start();

		} catch (RemoteException e) {
			System.out.println("RemoteException in MainModel.request");
			e.printStackTrace();
		}

	}

}
