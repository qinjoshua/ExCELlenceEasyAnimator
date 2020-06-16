package com.company.controller.viewactions.playeractions;

import com.company.view.swing.player.PlayerView;

/**
 * Action to increase the animation speed for a given player view.
 */
public class IncreaseSpeed implements PlayerAction {
  private static final double INCREASE_MULTIPLIER = 1.2;

  /**
   * Increases the speed of the given player view by a specific proportion.
   *
   * @param view the editor view to be acted on
   */
  @Override
  public void actOn(PlayerView view) {
    view.setSpeed((int) (view.getSpeed() * INCREASE_MULTIPLIER + 1));
  }
}
