package main.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import mini.user.UserThread;

public class ServerMain {
  private int port;

  public ServerMain(int port) {
    this.port = port;
  }

  public void start() {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while(true) {
        Socket clientSocket = serverSocket.accept();
        OutputStream clientOutput = clientSocket.getOutputStream();
        System.out.println("Accepted connection from: " + clientSocket);

        UserThread newUser = new UserThread(clientSocket, this);
        newUser.start();

      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    if(args.length != 1) {
      System.out.println("Usage: java Messenger <port number>");
      System.exit(1);
    }

    int port = Integer.parseInt(args[0]);

    ServerMain server = new ServerMain(port);
    server.start();

  }
}
