package main.server;

import java.io.Serializable;
import mini.user.UserThread;

public interface IServer extends Serializable {

  public String getName();

  public Chatroom getChatroomNames();

  public Chatroom getChatroom(String name);

  public void connectUser();

  public void addUserToChatroom(String name, UserThread user);




}
