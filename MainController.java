/**
 * 
 */
package main.mainController;

import java.awt.EventQueue;
import java.rmi.RemoteException;
import main.mainModel.IMainModel2MiniControllerAdapter;
import main.mainModel.IModel2ControllerAdapter;
import main.mainModel.IModel2ViewAdapter;
import main.mainModel.MainModel;
import main.mainView.ConnectorWrapper;
import main.mainView.IView2ModelAdapter;
import main.mainView.MainView;
import mini.miniController.IMiniController2MainModelAdpt;
import mini.miniController.MiniController;
import mini.miniModel.ChatRoomConnector;
import mini.miniView.MiniView;
import provided.discovery.IEndPointData;
import provided.discovery.impl.view.DiscoveryPanel;
import provided.discovery.impl.view.IDiscoveryPanelAdapter;

import java.util.UUID;
import java.util.function.Consumer;

import common.IChatAppConnector;
import common.IChatAppReceiver;

/**
 * The Main Controller of ChatApp program
 * @author lonzd
 *
 */
public class MainController {
	/**
	 * The Main View
	 */
	private MainView<ConnectorWrapper, IEndPointData> view;

	/**
	 * The Main Model
	 */
	private MainModel model;

	/**
	 * The discovery panel
	 */
	private DiscoveryPanel<IEndPointData> discoveryPnl;

	/**
	 * The port to start RMI
	 */
	private final int rmiPort;

	/**
	 * The port to export stubs
	 */
	private final int stubsPort;

	/**
	 * Constructor for a Main Controller
	 * @param rmiPort : The port to start RMI
	 * @param stubsPort : The port to export stubs
	 * 
	 */
	public MainController(int rmiPort, int stubsPort) {
		this.rmiPort = rmiPort;
		this.stubsPort = stubsPort;

		// Instantiates the discovery panel
		discoveryPnl = new DiscoveryPanel<>(new IDiscoveryPanelAdapter<IEndPointData>() {
			@Override
			public void connectToDiscoveryServer(String category, boolean watchOnly, Consumer<Iterable<IEndPointData>> endPtsUpdateFn) {
				// Calls the main model's connect to discovery server method
				model.connectDiscovery(category, watchOnly, endPtsUpdateFn);
			}

			@Override
			public void connectToEndPoint(IEndPointData selectedValue) {
				// Calls the main model's connect to end point method
				model.connectToEndPoint(selectedValue);

			}

		});

		view = new MainView<ConnectorWrapper, IEndPointData>(new IView2ModelAdapter() {
			@Override
			public void quit() {
				model.quit();
			}

			@Override
			public void viewMakeChatroom(String chatName) {
				model.modelMakeChatroom(chatName);
			}

			@Override
			public void connect(String address) {
				model.straightConnect(address);
			}

			@Override
			public void login(String text) {
				model.login(text);
			}

			@Override
			public void invite(UUID id, ConnectorWrapper user) {
				model.invite(id, user.getConnector());

			}

			@Override
			public void request(ConnectorWrapper user) {
				model.request(user);

			}



		}, discoveryPnl);

		model = new MainModel(new IModel2ViewAdapter() {

			@Override
			public void sendChatroomView(MiniView chatroomView, String chatroomName) {
				view.addChatroomView(chatroomView, chatroomName);	
			}

			@Override
			public void closeChatroomView(MiniView chatroomView) {
				view.closeChatroomView(chatroomView);
			}

			@Override
			public void startDiscovery() {
				discoveryPnl.start();
			}

			@Override
			public void addConnectedUser(IChatAppConnector newConnection, String friendlyName) {
				view.addConnectedUser(new ConnectorWrapper(newConnection, friendlyName));	

			}

			@Override
			public void removeAllChatroomView() {
				view.removeAllChatroomView();

			}

			@Override
			public void removeConnectedUserView(IChatAppConnector connector) {
				try {
					view.deleteConnectedUser(new ConnectorWrapper(connector, connector.getName()));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
			}

		}, new IModel2ControllerAdapter() {

			@Override
			public IChatAppReceiver setupChatroom(ChatRoomConnector chatRoomConnector, String myUserName) {
				System.out.println("\nCreating new chatroom: " + chatRoomConnector.getChatRoomName());

				// Construct a new MiniController
				// Give it an adapter that allows mini-controller to access methods on the main model
				MiniController newChatroom = new MiniController(new IMiniController2MainModelAdpt() {

					@Override
					public void leaveChatroom(UUID uuid, MiniView view) {
						model.leaveChatroom(uuid, view);
					}

				}, chatRoomConnector, myUserName);


				// Start the mini-controller
				newChatroom.start();

				// Retrieve the adapter that allows the main model to access methods on mini-controller and send it to the main model for later use
				IMainModel2MiniControllerAdapter newMainModel2MiniControlAdpt = newChatroom.getMainModel2MiniControlAdapter();
				// Send the adapter to the main model
				model.addChatroom(newChatroom, newMainModel2MiniControlAdpt);

				// Retrieve the mini-view from the mini-controller
				MiniView newChatroomView = newChatroom.getMiniView();
				//  Send it to the main view for installation
				model.getModel2ViewAdapter().sendChatroomView(newChatroomView, chatRoomConnector.getChatRoomName());

				System.out.println("Created new chatroom");

				return newChatroom.getMyReceiver();
			}

		});
	}

	/**
	 * @param args generic arguments
	 */
	public static void main(String[] args) {
		int rmiPort = Integer.parseInt(args[0]);
		int stubsPort = Integer.parseInt(args[1]);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainController controller = new MainController(rmiPort, stubsPort);
					controller.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * controller start function
	 */
	public void start() {
		view.start();
		model.start(rmiPort, stubsPort);
	}

}
