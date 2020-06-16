package com.company.controller.viewactions.playeractions;

import com.company.view.player.PlayerView;

/**
 * Action that plays if the animation is currently paused or pauses if the animation is currently
 * playing for the given PlayerView.
 */
public class TogglePlay implements PlayerAction {
  /**
   * Toggles whether the PlayerView is playing or paused.
   *
   * @param view the editor view to be acted on
   */
  @Override
  public void actOn(PlayerView view) {
    view.togglePlay();
  }
}
