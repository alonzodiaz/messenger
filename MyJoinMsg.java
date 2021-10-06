package main.mainController;

import common.message.type.IJoinMessage;
import provided.datapacket.IDataPacketID;

/**
 * @author lonzd
 *
 */
public class MyJoinMsg implements IJoinMessage{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -358295060181259995L;

	@Override
	public IDataPacketID getID() {
		return IJoinMessage.GetID();
	}

}
