package mini.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import main.server.ServerMain;

public class UserThread extends Thread{
  private Socket userSocket;
  private ServerMain server;
  private InputStream inputStream;
  private OutputStream outputStream;

  public UserThread(Socket socket, ServerMain server) {
    this.userSocket = socket;
    this.server = server;

  }

  @Override
  public void run() {
    try {
      userAction();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void userAction() throws IOException {
    this.inputStream = this.userSocket.getInputStream();
    this.outputStream = this.userSocket.getOutputStream();

    BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));
    String line;

    while ((line = reader.readLine()) != null) {
      if(line.equalsIgnoreCase("quit")) {
        break;
      }
      String msg = "You type: " + line;
      outputStream.write(msg.getBytes());
    }

    this.userSocket.close();


  }

}
