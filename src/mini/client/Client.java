package mini.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import main.server.IChatRoom;

/**
 * If the class implemented Remote and is currently exported when you pass it, a remote reference
 * (stub) to the object is passed. The object stays where it is and methods can be invoked on it
 * remotely via the reference.
 *
 * <p>If the class extends Serializable, a copy of the object is passed. Any methods invoked on the
 * object will be invoked on the local copy.
 */
public class Client implements IClient {

  private static final long serialVersionUID = -7363838860744751801L;

  private String name;
  private Set<IClient> friends;
  private Set<IChatRoom> chatRooms;

  public Client(Set<IClient> friends, Set<IChatRoom> rooms, String username) {
    this.name = username;
    this.friends = friends;
    this.chatRooms = rooms;
  }

  public  Client(String userName)  {
    this(new HashSet<>(), new HashSet<>(), userName);
  }


  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void sendMessage() {

  }

  @Override
  public void receiveMessage() {

  }

  @Override
  public List<IClient> getFriends() {
    return new ArrayList<>(this.friends);
  }

  @Override
  public List<IChatRoom> getChatrooms() {
    return new ArrayList<>(this.chatRooms);
  }
}
