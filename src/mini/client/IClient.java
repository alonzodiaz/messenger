package mini.client;

import java.io.Serializable;
import java.util.List;
import main.server.IChatRoom;

public interface IClient extends Serializable {

  public String getName();

  public void sendMessage();

  public void receiveMessage();

  public List<IClient> getFriends();

  public List<IChatRoom> getChatrooms();
}
