package main.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import mini.client.IClient;

/**
 *
 *
 * */
public class Chatroom implements IChatRoom {
  private static final long serialVersionUID = -2696352386245861668L;

  private String name;

  private Set<IClient> members;

  private final UUID uuid;

  public Chatroom(Set<IClient> clients, String chatName) {
    this.name = chatName;
    this.members = clients;
    this.uuid = UUID.randomUUID();
  }

  public Chatroom(String chatName) {
    this(new HashSet<>(), chatName);

  }
  @Override
  public String getChatRoomName() {
    return this.name;
  }

  @Override
  public List<IClient> getAllMembers() {
    return new ArrayList<>(this.members);
  }

  @Override
  public void addMember(IClient newUser) {
    members.add(newUser);
  }

  @Override
  public void removeMember(IClient oldUser) {
    members.remove(oldUser);
  }

  @Override
  public UUID getID() {
    return this.uuid;
  }

  @Override
  public boolean equals(Object o) {
    if(o != null) {
      if(o instanceof Chatroom) {
        return this.getID().equals(((Chatroom) o).getID());
      }
    }
    return false;
  }

}
