package main.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainView extends JFrame {
  // Overall pane
  private JPanel contentPane;



  public MainView() {
    initGUI();
  }

  private void initGUI() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1500, 500);
    contentPane = new JPanel();
    setContentPane(contentPane);


  }

  public void start() {
    this.setVisible(true);
    System.out.println("Main View Start");
  }
}
