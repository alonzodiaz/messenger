/**
 * 
 */
package main.mainView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.IChatRoomConnector;
import mini.miniModel.ChatRoomConnector;

import javax.swing.JScrollPane;

/**
 * @author lonzd
 * 
 *
 */
public class RequestListView extends JFrame {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7579232340016922558L;

	/**
	 * overall pane
	 */
	private JPanel contentPane;

	/**
	 * The panel holding the buttons
	 */
	private final JPanel btnPnl = new JPanel();
	/**
	 * The cancel button
	 */
	private final JButton btnCancel = new JButton("Cancel");
	/**
	 * The join button
	 */
	private final JButton btnJoin = new JButton("Join");

	/**
	 * The list model for the JList
	 */
	DefaultListModel<String> chatListModel = new DefaultListModel<String>();

	/**
	 * The JList that will be the list of chat rooms
	 */
	JList<String> chatList;

	/**
	 * The mapping of Chatrooms to their names
	 */
	HashMap<String, ChatRoomConnector> chatRooms = new HashMap<>();

	/**
	 * Panel that will hold the chat list and its scroll panel
	 */
	private final JPanel listPnl = new JPanel();
	/**
	 * The scroll panel that will hold the chat list
	 */
	private final JScrollPane scrollPane;

	/**
	 * The adapter from here to main model
	 */
	private IRequestView2MainModelAdapter adapter;

	/**
	 * Constructor for the RequestListView
	 * 
	 * @param _givenChatRooms : The list of chat rooms
	 * @param _adapter        : the adapter from here to main model
	 * 
	 */
	public RequestListView(List<IChatRoomConnector> _givenChatRooms, IRequestView2MainModelAdapter _adapter) {			
		
		for (IChatRoomConnector chatRoom : _givenChatRooms) {
			ChatRoomConnector cr = new ChatRoomConnector(new HashSet<>(chatRoom.getAllReceivers()), chatRoom.getChatRoomName(), chatRoom.getID());
			
			chatRooms.put(cr.getChatRoomName(), cr);
		}
		
		chatListModel.addAll(chatRooms.keySet());
		chatList = new JList<String>(chatListModel);
		scrollPane = new JScrollPane(chatList);
		this.adapter = _adapter;
		initGUI();
	}

	/**
	 * Initialize the GuI
	 */
	private void initGUI() {
		setDefaultCloseOperation(RequestListView.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(listPnl, BorderLayout.CENTER);
		listPnl.setLayout(new BorderLayout(0, 0));

		listPnl.add(scrollPane);
		getContentPane().add(btnPnl, BorderLayout.SOUTH);
		btnCancel.setToolTipText("Click to cancel.");
		/**
		 * Closes the window
		 */
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();

			}
		});
		btnPnl.add(btnCancel);
		/**
		 * Join the chat room
		 */
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adapter.requestJoinChatroom(chatRooms.get(chatList.getSelectedValue()));
			}
		});

		btnJoin.setToolTipText("Click to join the chat rooms selected above.");

		btnPnl.add(btnJoin);
	}

	/**
	 * Start the view
	 */
	public void start() {
		this.setVisible(true);
	}

}
