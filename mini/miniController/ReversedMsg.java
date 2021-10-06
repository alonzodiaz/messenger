package mini.miniController;

import common.message.IMessage;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * @author Jacob
 *
 */
public class ReversedMsg implements IMessage {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 3629950563261729438L;
	
	/**
	 * The msg field
	 */
	private String msg;

	/**
	 * The constructor
	 * @param _msg : the string msg
	 */
	public ReversedMsg(String _msg) {
		this.msg = _msg;
	}

	@Override
	public IDataPacketID getID() {
		return ReversedMsg.GetID();
	}

	/**
	 * @return The ID of the msg
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ReversedMsg.class);
	}
	
	/**
	 * Gets the reversed string
	 * @return The reversed string.
	 */
	public String getString() {
		StringBuilder builder = new StringBuilder(this.msg);
		return builder.reverse().toString();
	}

}
