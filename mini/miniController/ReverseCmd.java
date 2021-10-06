package mini.miniController;

import java.rmi.RemoteException;

import common.AMessageAlgoCmd;
import common.ChatAppDataPacket;
import common.message.IMessage;
import provided.datapacket.IDataPacketID;

/**
 * @author Jacob
 *
 */
public class ReverseCmd extends AMessageAlgoCmd<IMessage> {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 4156870660198910078L;

	@Override
	public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
		String senderName;
		try {
			senderName = host.getSender().getName();
			c2mAdapter.displayString("Unknown special message type from Jacob and Alonzo!");
			c2mAdapter.displayString(("[" + senderName + "]: " + ((ReversedMsg) host.getData()).getString()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}


}
