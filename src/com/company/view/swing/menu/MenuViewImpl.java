package com.company.view.swing.menu;

import com.company.controller.viewactions.menuactions.MenuAction;
import com.company.controller.viewactions.menuactions.NewProject;
import com.company.controller.viewactions.menuactions.OpenFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Implementation of a view that represents the main menu and entry point for the application.
 */
public class MenuViewImpl extends JFrame implements MenuView {
  Consumer<MenuAction> callback;

  /**
   * Constructor that sets the callback to null and initializes the rest of the menu view.
   */
  public MenuViewImpl() {
    super("Excellence Start Menu");

    JPanel main = new JPanel();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel splashScreen = new SplashScreen();

    main.setLayout(new BorderLayout());

    main.add(splashScreen, BorderLayout.CENTER);

    JPanel menuOptions = new JPanel();
    JButton openProject = new JButton("Open Project");
    JButton newProject = new JButton("New Project");

    menuOptions.add(openProject);
    menuOptions.add(newProject);

    menuOptions.setLayout(new FlowLayout());

    main.add(menuOptions, BorderLayout.SOUTH);

    this.getContentPane().add(main, BorderLayout.CENTER);

    final Action open = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openFile();
      }
    };

    final Action createNew = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new NewProject());
        }
      }
    };

    newProject.addActionListener(createNew);
    openProject.addActionListener(open);

    this.pack();
  }

  private void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      if (callback != null) {
        callback.accept(new OpenFile(selectedFile.getAbsolutePath()));
      }
    }
  }

  @Override
  public void close() {
    this.dispose();
  }

  @Override
  public void renderVisual() {
    this.setVisible(true);
  }

  @Override
  public void setCallback(Consumer<MenuAction> callback) {
    this.callback = callback;
  }

  private Image getImage() throws IOException {
    return ImageIO.read(new URL("https://i.imgur.com/bUF0FeB.png"))
        .getScaledInstance(600, 450, Image.SCALE_SMOOTH);
  }

  private class SplashScreen extends JPanel {
    public SplashScreen() {
      this.setPreferredSize(new Dimension(600, 450));
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Image img;
      try {
        img = getImage();
        g.drawImage(img, 0, 0, null);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
