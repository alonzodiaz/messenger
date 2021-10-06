/**
 * 
 */
package mini.miniView;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import common.IChatAppReceiver;
import mini.miniController.MiniController;
import provided.datapacket.IDataPacketID;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Map.Entry;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

/**
 * The chat room view
 * @author lonzd
 *
 */
public class MiniView extends JPanel {
	/**
	 * Serial ID
	 */

	private static final long serialVersionUID = -6834991152020273258L;

	/**
	 * The Msg that holds the text field and send msg button
	 */

	private final JPanel sendMsgPanel = new JPanel();

	/**
	 * The text field to type msgs
	 */
	private final JTextField msgTxtFld = new JTextField();

	/**
	 * The button to send the msg
	 */
	private final JButton sendMsgBtn = new JButton("Send Message");

	/**
	 * The pnl holding the userList
	 */
	private final JPanel leftPnl = new JPanel();

	/**
	 * The button to leave the chat
	 */
	private final JButton leaveChatBtn = new JButton("Leave Chat");

	/**
	 * The panel holding the dropdown box
	 */
	private final JPanel txtMsgTypePnl = new JPanel();

	/**
	 * The drop down box holding all the types of well known messages
	 */
	private final JComboBox<String> msgTypeBox = new JComboBox<String>();

	/**
	 * The panel holding the actual chat 
	 */
	private final JTabbedPane chatPnl = new JTabbedPane(JTabbedPane.TOP);

	/**
	 * The chat 
	 */

	private final JTextArea txtChatArea = new JTextArea();
	
	/**
	 * The scroll panel for the text chat area
	 */
	private final JScrollPane chatScrollPnl = new JScrollPane(txtChatArea);
	
	/**
	 * The vertical list panel for unknown msg
	 */
	public final VerticalListPanel vlp = new VerticalListPanel(1000);
	
	/**
	 * Holds the shared display info
	 */
	private final JScrollPane scrollPnl = new JScrollPane(vlp);
	

	/**
	 * The mini view 2 mini controller adapter
	 */
	private IMiniView2MiniControllerAdpt cntrlAdpt;

	/**
	 * The list model for the user list
	 */
	private DefaultListModel<String> userListModel = new DefaultListModel<String>();

	/**
	 * The user list  
	 */
	private JList<String> userList = new JList<String>(userListModel);

	/**
	 * The constructor for the mini view
	 * @param _adpt : the mini view 2 mini controller adapter
	 */
	public MiniView(IMiniView2MiniControllerAdpt _adpt) {
		msgTxtFld.setToolTipText("Type into this box your message you want to send to chat room");
		msgTxtFld.setColumns(10);
		userListModel.addAll(_adpt.getAllReceiverNames());
		cntrlAdpt = _adpt;

		initGUI();
	}

	/**
	 * Set up the GUI
	 */
	private void initGUI() {
		
		setBounds(100, 100, 1500, 150);
		setLayout(new BorderLayout(0, 0));
		add(leftPnl, BorderLayout.WEST);
		leftPnl.setLayout(new BorderLayout(0, 0));
		userList.setToolTipText("The list of the users in the chat room");
		
		
		leftPnl.add(userList);
		leaveChatBtn.setToolTipText("Press this button to leave the chat room");
		leftPnl.add(leaveChatBtn, BorderLayout.SOUTH);		

		add(txtMsgTypePnl, BorderLayout.NORTH);
		txtMsgTypePnl.setLayout(new GridLayout(1, 0, 0, 0));

		for (Entry<String, IDataPacketID> entry : MiniController.sendMsgTypes.entrySet()) {
			msgTypeBox.addItem(entry.getKey());
		}
		msgTypeBox.setToolTipText("Select the type of message you want to send");

		txtMsgTypePnl.add(msgTypeBox);

		add(chatPnl, BorderLayout.CENTER);
		chatScrollPnl.setToolTipText("Holds the chat room ");

		chatPnl.addTab("Chat", null, chatScrollPnl, null);
		txtChatArea.setToolTipText("The chat room text area");
		txtChatArea.setLineWrap(true);
		txtChatArea.setEditable(false);
		chatScrollPnl.setViewportView(txtChatArea);

		add(sendMsgPanel, BorderLayout.SOUTH);

		sendMsgPanel.add(msgTxtFld);
		sendMsgBtn.setToolTipText("Press this button to send a message");

		sendMsgPanel.add(sendMsgBtn);

		leaveChatBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cntrlAdpt.leaveChatroom();
			}
		});

		sendMsgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String txt = msgTxtFld.getText();
				String msgName = (String) msgTypeBox.getSelectedItem();
				cntrlAdpt.sendMsg(txt, MiniController.sendMsgTypes.get(msgName));
			}
		});
		

		scrollPnl.setToolTipText("Displays the components to be shared for unknown commands!");
		chatPnl.addTab("Shared Scroll View", null, scrollPnl, null);
		scrollPnl.setViewportView(vlp);
		
	}

	/**
	 * Start the mini view
	 */
	public void start() {
		this.setVisible(true);
	}

	/**
	 * @return The ID of the chat room
	 */
	public UUID getID() {
		return cntrlAdpt.getID();
	}
	/**
	 * Adds a new receiver to the user list
	 * @param newReceiver : The new receiver to be added to the user list
	 */
	public void addNewReceiverView(IChatAppReceiver newReceiver) {
		try {
			userListModel.addElement(newReceiver.getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Deletes a new receiver from the user list
	 * @param newReceiver : The new receiver to be added to the user list
	 */
	public void deleteReceiverView(IChatAppReceiver newReceiver) {
		try {
			userListModel.removeElement(newReceiver.getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Displays a string on txt chat area 
	 * @param string : The string to be displayed on the chat
	 */
	public void display(String string) {
		txtChatArea.append(string + "\n");

	}

}
