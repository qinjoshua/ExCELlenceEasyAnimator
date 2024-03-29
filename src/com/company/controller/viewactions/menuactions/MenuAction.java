package com.company.controller.viewactions.menuactions;

import com.company.view.swing.menu.MenuView;

/**
 * An action on a menu view.
 */
public interface MenuAction {

  /**
   * Runs the given action on the menu view.
   *
   * @param view the editor view to be acted on
   * @throws IllegalStateException if the action was incorrectly created
   */
  void actOn(MenuView view) throws IllegalStateException;
}