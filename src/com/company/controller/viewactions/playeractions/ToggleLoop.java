package com.company.controller.viewactions.playeractions;

import com.company.view.swing.player.PlayerView;

/**
 * Action that enables looping if it is currently disabled and disables it if it is currently
 * enabled for the given PlayerView.
 */
public class ToggleLoop implements PlayerAction {
  /**
   * Toggles whether the PlayerView is looping.
   *
   * @param view the editor view to be acted on
   */
  @Override
  public void actOn(PlayerView view) {
    view.toggleLoop();
  }
}
