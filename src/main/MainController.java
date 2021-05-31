package main;

import main.model.MainModel;
import main.view.MainView;

public class MainController {
  private MainView view;

  private MainModel model;

  public MainController() {
    view = new MainView();

    model = new MainModel();
  }

  public void start() {
    view.start();
    model.start();
  }
}
