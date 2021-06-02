package main.server;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import mini.client.IClient;

public interface IChatRoom extends Serializable {

  /**
   * Get the name of the chat room. Assume a chat room name is
   * an invariant feature of a chat room.
   * @return the name of this chat room
   */
  public String getChatRoomName();

  /**
   * Get all of the clients in this chat room.
   * @return list of all participants in this chat room.
   */
  public List<IClient> getAllMembers();

  public void addMember(IClient newUser);

  public void removeMember(IClient oldUser);

  public UUID getID();


}
