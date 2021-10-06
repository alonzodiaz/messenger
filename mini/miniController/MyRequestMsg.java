package mini.miniController;

import common.message.type.cmd.IRequestCmdMessage;
import provided.datapacket.IDataPacketID;

/**
 * Concrete implementation of RequestCmdMessage
 * 
 * @author Jacob Lu
 *
 */
public class MyRequestMsg implements IRequestCmdMessage {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -358295060181259995L;
	
	/**
	 * hostID - The host ID that has been requested
	 */
	IDataPacketID hostID;
	
	/**
	 * @param _hostID - The host ID that has been requested
	 */
	public MyRequestMsg(IDataPacketID _hostID) {
		this.hostID = _hostID;
	}

	@Override
	public IDataPacketID getID() {
		return IRequestCmdMessage.GetID();
	}

	@Override
	public IDataPacketID getUnknownID() {
		// Pass along the host ID
		return hostID;
	}

}
