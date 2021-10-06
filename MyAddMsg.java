package mini.miniController;

import common.AMessageAlgoCmd;
import common.ChatAppDataPacket;
import common.message.IMessage;
import common.message.type.cmd.IAddCmdMessage;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.IDataPacketID;

/**
 * Concrete implementation of IAddCmdMessage
 * 
 * @author Jacob Lu
 *
 */
public class MyAddMsg implements IAddCmdMessage {
	
	/**
	 * Command ID
	 */
	IDataPacketID cmdId;
	
	/**
	 * Visitor algo to get the command from
	 */
	DataPacketAlgo<Void, Void> visitAlgo;
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -8856261005119191587L;
	
	/**
	 * @param _cmdId - The ID of the command to add
	 * @param _visitAlgo - The visitor algo to get the command from
	 */
	public MyAddMsg(IDataPacketID _cmdId, DataPacketAlgo<Void, Void> _visitAlgo) {
		this.cmdId = _cmdId;
		this.visitAlgo = _visitAlgo;
	}

	@Override
	public IDataPacketID getUnknownID() {
		return cmdId;
	}

	@Override
	public AMessageAlgoCmd<? extends IMessage> getUnknownAlgoCmd() {
		return new AMessageAlgoCmd<IMessage>() {

			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = 7218230904680697914L;

			@Override
			public Void apply(IDataPacketID index, ChatAppDataPacket<IMessage> host, Void... params) {
				return visitAlgo.getCmd(cmdId).apply(index, host, params);
			}
			
		};
	}

}
