package com.company.view.swing.menu;

import com.company.controller.viewactions.menuactions.MenuAction;

import java.util.function.Consumer;

/**
 * A menu view is a view that displays the main menu options at the start of application launch.
 * Supports only the ability to close itself; the rest should be handled in the model.
 */
public interface MenuView {
  /**
   * Close the main menu prompt.
   */
  void close();

  /**
   * Makes the view visible.
   */
  void renderVisual();

  /**
   * Sets the callback for the view.
   *
   * @param callback the call back for the view
   */
  void setCallback(Consumer<MenuAction> callback);
}
