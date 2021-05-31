package main;

import java.awt.*;
import main.model.MainModel;
import main.view.MainView;

public class MainController {
  private MainView view;

  private MainModel model;

  public MainController() {
    view = new MainView();

    model = new MainModel();
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MainController controller = new MainController();
          controller.start();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void start() {
    view.start();
    model.start();
  }
}
