package main.server;

import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import mini.user.UserThread;

public class Server implements IServer {

  private static final long serialVersionUID = -988067964288082839L;
  private int port;
  private Set<IChatRoom> chatRooms;



  public Server() {

  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public Chatroom getChatroomNames() {
    return null;
  }

  @Override
  public Chatroom getChatroom(String name) {
    return null;
  }

  @Override
  public void connectUser() {

  }

  @Override
  public void addUserToChatroom(String name, UserThread user) {

  }
}
