package com.company.view.swing.menu;
import com.company.controller.viewactions.menuactions.MenuAction;
import com.company.controller.viewactions.menuactions.NewProject;
import com.company.controller.viewactions.menuactions.OpenFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
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
    JPanel main = new JPanel();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel splashScreen = new JPanel();
    splashScreen.setPreferredSize(new Dimension(400, 300));

    main.add(splashScreen, BorderLayout.WEST);

    JPanel menuOptions = new JPanel();
    JButton openProject = new JButton("Open Project");
    JButton newProject = new JButton("New Project");

    menuOptions.add(openProject);
    menuOptions.add(newProject);

    menuOptions.setLayout(new BoxLayout(menuOptions, BoxLayout.Y_AXIS));

    main.add(menuOptions, BorderLayout.EAST);

    this.getContentPane().add(main, BorderLayout.CENTER);

    final Action OPEN = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openFile();
      }
    };

    final Action NEW = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new NewProject());
        }
      }
    };

    newProject.addActionListener(NEW);
    openProject.addActionListener(OPEN);

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
}
