package com.company.controller.viewactions.menuactions;

import com.company.controller.viewactions.playeractions.PlayerAction;
import com.company.view.swing.menu.MenuView;

import java.util.function.Consumer;

/**
 * Represents a consumer that performs an action on a player view.
 */
public class MenuActionConsumerImpl implements Consumer<MenuAction> {
  MenuView view;

  /**
   * Constructor that sets the editor view to be acted on.
   *
   * @param view editor view to be acted on
   */
  public MenuActionConsumerImpl(MenuView view) {
    this.view = view;
  }

  @Override
  public void accept(MenuAction menuAction) {
    menuAction.actOn(view);
  }
}
