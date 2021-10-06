/**
 * 
 */
package mini.miniController;

import java.util.UUID;

import mini.miniView.MiniView;

/**
 * @author lonzd
 *
 */
public interface IMiniController2MainModelAdpt {
	/**
	 * Remove user from this chatroom
	 * @param uuid : The id of the chat room
	 * @param view : The mini view of the chat room
	 */
	void leaveChatroom(UUID uuid, MiniView view);

	
}
