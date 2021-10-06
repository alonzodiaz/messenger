/**
 * 
 */
package main.mainView;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import mini.miniView.MiniView;
import provided.discovery.impl.view.DiscoveryPanel;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;

/**
 * The Main View of a ChatApp program
 * @author lonzd
 * @param <TDropDownItm> The type of item for drop down box
 * @param <TEndPoint> : The type for the discovery panel
 *
 */
public class MainView<TDropDownItm, TEndPoint> extends JFrame{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 4136619672939718267L;
	
	/**
	 * overall pane
	 */
	private JPanel contentPane;
	
	/**
	 * The panel at the top of the window
	 */
	private final JPanel northPnl = new JPanel();
	/**
	 * The panel holding usernamePnl and userBtnPnl
	 */
	private final JPanel userPanel = new JPanel();
	
	/**
	 * The panel holding the user name text field
	 */
	private final JPanel usernamePnl = new JPanel();
	
	/**
	 * Text field for the user to specify their user name
	 */
	private final JTextField usernameTxtFld = new JTextField();
	
	/**
	 * The panel holding the start and quit buttons 
	 */
	private final JPanel usrBtnPnl = new JPanel();
	
	/**
	 * The start button
	 */
	private final JButton startBtn = new JButton("Start");
	
	/**
	 * The quit button
	 */
	private final JButton quitBtn = new JButton("Quit");
	
	/**
	 * The panel holding all things related to making a new chat room
	 */
	private final JPanel makeChatPnl = new JPanel();
	
	/**
	 * The text field to specify name of new chat room
	 */
	private final JTextField chatNameTxt = new JTextField();
	
	/**
	 * The button to make a new chat room
	 */
	private final JButton makeChatBtn = new JButton("Make Chatroom");
	
	/**
	 * The discovery panel
	 */
	private DiscoveryPanel<TEndPoint> discoveryPnl;
	
	/**
	 * The panel holding connectToPnl and connectedHostPnl
	 */
	private final JPanel connectPnl = new JPanel();
	
	/**
	 * The panel holding all items related to connecting to specific IP address
	 */
	private final JPanel connectToPnl = new JPanel();
	
	/**
	 * The panel holding the connected host drop down box
	 */
	private final JPanel connectedHostPnl = new JPanel();
	
	/**
	 * The text field to specify which IP Address to connect to
	 */
	private final JTextField addressTxtFld = new JTextField();
	
	/**
	 * The connect button
	 */
	private final JButton connectBtn = new JButton("Connect");
	
	/**
	 * Drop down box holding all the connected Hosts
	 */
	private final JComboBox<TDropDownItm> connectedHostsBox = new JComboBox<>();
	
	/**
	 * Panel holding invite, request, get other users button
	 */
	private final JPanel cntBtnPnl = new JPanel();
	
	/**
	 * Invite button
	 */
	private final JButton inviteBtn = new JButton("Invite");
	
	/**
	 * Request button
	 */
	private final JButton requestBtn = new JButton("Request");
	
	/**
	 * The Main View 2 Main Model Adapter
	 */
	private IView2ModelAdapter mv2MmAdapter;
	
	/**
	 * The panel that will hold all the chat rooms this ChatApp is connected to
	 */
	private final JTabbedPane chatRoomsPnl = new JTabbedPane(JTabbedPane.TOP);
	
	/**
	 * Mapping of chat room mini views to their names 
	 */
	private Map<MiniView, String> chatRooms = new HashMap<>();

	
	/**
	 * A panel for the chat room
	 */
	private final JPanel contentPnl = new JPanel();
	
	/**
	 * A text area for the chat room
	 */
	private final JTextArea txtArea = new JTextArea();
	
	/**
	 * A scoll pane for the chat room
	 */
	private final JScrollPane contentScrollPnl = new JScrollPane(txtArea);
	
	

	/**
	 * Create the frame.
	 * @param _adapter : adapter to communicate w/ model
	 * @param _discoveryPnl : The discovery server panel
	 */
	public MainView(IView2ModelAdapter _adapter, DiscoveryPanel<TEndPoint> _discoveryPnl) {
		this.mv2MmAdapter = _adapter;
		this.discoveryPnl = _discoveryPnl;
		addressTxtFld.setToolTipText("Type the address you want to directly connect to");
		addressTxtFld.setColumns(10);
		chatNameTxt.setToolTipText("Type here to name your chat room");
		chatNameTxt.setColumns(10);
		usernameTxtFld.setToolTipText("Type your desired user name here");
		usernameTxtFld.setColumns(10);
		initGUI();
	}
	
	/**
	 * gui initialization function
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(chatRoomsPnl, BorderLayout.CENTER);
		contentPnl.setToolTipText("Panel holding content");
		
		chatRoomsPnl.addTab("Info", null, contentPnl, null);
		

		contentPnl.setLayout(new GridLayout(0, 1, 0, 0));
		contentScrollPnl.setToolTipText("Panel holding scroll");
		contentPnl.add(contentScrollPnl);
		txtArea.setToolTipText("The text area");
		txtArea.setLineWrap(true);
		txtArea.setEditable(false);
		northPnl.setToolTipText("Panel holding all the things in the north");
		northPnl.setBorder(null);
		contentPane.add(northPnl, BorderLayout.NORTH);
		northPnl.setLayout(new BoxLayout(northPnl, BoxLayout.X_AXIS));
		userPanel.setToolTipText("User stuff panel. I am very sleepy it is 2:30am");
		
		northPnl.add(userPanel);
		userPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		userPanel.add(usernamePnl);
		usernamePnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		usernamePnl.add(usernameTxtFld);
		
		userPanel.add(usrBtnPnl);
		startBtn.setToolTipText("Press button to start chat app");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mv2MmAdapter.login(usernameTxtFld.getText());
			}
		});
		
		usrBtnPnl.add(startBtn);
		quitBtn.setToolTipText("Press button to quit chat app");
		quitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mv2MmAdapter.quit();
			}
		});
		
		addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                mv2MmAdapter.quit();
                // e.getWindow().dispose();
            }
        });
		
		usrBtnPnl.add(quitBtn);
		makeChatPnl.setToolTipText("Make chat panel");
		
		northPnl.add(makeChatPnl);
		makeChatPnl.setLayout(new GridLayout(2, 1, 0, 0));
		
		makeChatPnl.add(chatNameTxt);
		makeChatBtn.setToolTipText("Make chatroom when you press this button");
		
		makeChatPnl.add(makeChatBtn);
		
		
		northPnl.add(discoveryPnl, BorderLayout.SOUTH);
		connectPnl.setToolTipText("connect panel");
		
		northPnl.add(connectPnl);
		
		connectPnl.add(connectToPnl);
		
		connectToPnl.add(addressTxtFld);
		connectBtn.setToolTipText("Connect straight to the address");
		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mv2MmAdapter.connect(addressTxtFld.getText());
			}
		});
		
		connectToPnl.add(connectBtn);
		
		connectPnl.add(connectedHostPnl);
		connectedHostPnl.setLayout(new GridLayout(2, 1, 0, 0));
		connectedHostsBox.setToolTipText("Drop down holding all the users you've connected to");
		
		connectedHostPnl.add(connectedHostsBox);
		
		connectedHostPnl.add(cntBtnPnl);
		inviteBtn.setToolTipText("Invite your user to the chat room");
		inviteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				invite();
			}
		});
		
		cntBtnPnl.add(inviteBtn);
		requestBtn.setToolTipText("Request all of the chat rooms of the user");
		requestBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectorWrapper user = (ConnectorWrapper) connectedHostsBox.getSelectedItem();
				mv2MmAdapter.request(user);
			}
		});
		
		cntBtnPnl.add(requestBtn);
		
		makeChatBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chatroomName = chatNameTxt.getText();
				mv2MmAdapter.viewMakeChatroom(chatroomName);
			}
		});
		
	}
	
	/**
	 * The method called when leaving or deleting a chat room
	 * Closes the chat room from the view 
	 * @param chatroomView : The view of the chat room that will be closed
	 */
	public void closeChatroomView(MiniView chatroomView) {
		chatRoomsPnl.remove(chatroomView);
	}

	
	/**
	 * Starts the Main View
	 */
	public void start() {
		this.setVisible(true);
		discoveryPnl.start();
	}
	
	/**
	 * Called when creating or joining a new chat room
	 * Adds the new chat room view to the main view
	 * @param newChatroomView : The view of the chat room to be added
	 * @param chatroomName : The name of the chat room to be added
	 */
	public void addChatroomView(MiniView newChatroomView, String chatroomName) {
		chatRoomsPnl.add(chatroomName, newChatroomView);
		chatRooms.put(newChatroomView, chatroomName);
	}

	/**
	 * Adds the connected user to the drop down box
	 * @param newConnection : The new user that was connected to
	 */
	public void addConnectedUser(TDropDownItm newConnection) {
		
		connectedHostsBox.addItem(newConnection);
	}
	
	/**
	 * @param connectedUser - The connected user to delete from the dropdown
	 */
	public void deleteConnectedUser(TDropDownItm connectedUser) {
		connectedHostsBox.removeItem(connectedUser);
	}
	
	/**
	 * Removes a user from the drop down menu
	 * @param user : The user to be removed 
	 */
	public void removeConnectedUser(TDropDownItm user) {
		connectedHostsBox.removeItem(user);
	}
	
	/**
	 * Passes the invitation parameters to the Main View 2 Main Model Adapter
	 * Invites a user to a specified chat room
	 */
	public void invite() {
		MiniView selectedChatroom = (MiniView) chatRoomsPnl.getSelectedComponent();
		ConnectorWrapper user = (ConnectorWrapper) connectedHostsBox.getSelectedItem();
		mv2MmAdapter.invite(selectedChatroom.getID(), user);
		
	}

	/**
	 * Removes all the tabs of the chat Room panel
	 */
	public void removeAllChatroomView() {
		chatRoomsPnl.removeAll();
		
	}
	
	
	

}
