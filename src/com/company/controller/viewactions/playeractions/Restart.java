package com.company.controller.viewactions.playeractions;

import com.company.view.player.PlayerView;

/**
 * Sets playback to the beginning tick.
 */
public class Restart implements PlayerAction {
  @Override
  public void actOn(PlayerView view) {
    view.setTick(0);
  }
}