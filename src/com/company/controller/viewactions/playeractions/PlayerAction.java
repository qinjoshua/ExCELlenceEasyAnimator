package com.company.controller.viewactions.playeractions;

import com.company.view.swing.player.PlayerView;

/**
 * An action on a player view.
 */
public interface PlayerAction {

  /**
   * Runs the given action on the editor view.
   *
   * @param view the editor view to be acted on
   */
  void actOn(PlayerView view);
}