package com.company.controller.viewactions.playeractions;

import com.company.view.player.PlayerView;

/**
 * Action to decrease the animation speed for a given player view.
 */
public class DecreaseSpeed implements PlayerAction {
  private static final double DECREASE_MULTIPLIER = 0.8;
  /**
   * Decreases the speed of the given player view by a specific proportion.
   * @param view the editor view to be acted on
   */
  @Override
  public void actOn(PlayerView view) {
    view.setSpeed(Math.max(1, (int) (view.getSpeed() * DECREASE_MULTIPLIER)));
  }
}
