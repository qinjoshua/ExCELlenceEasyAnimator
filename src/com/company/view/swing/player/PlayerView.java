package com.company.view.swing.player;

import com.company.controller.viewactions.playeractions.PlayerAction;
import com.company.view.VisualView;

import java.util.function.Consumer;

/**
 * Represents all the functionality that a controller can perform on a player view.
 */
public interface PlayerView extends VisualView {
  /**
   * Toggles whether or not the animation is playing.
   */
  void togglePlay();

  /**
   * Sets the tick that the animation should play from.
   *
   * @param tick the tick the animation should play from
   */
  void setTick(int tick);

  /**
   * Sets the speed that the animation will play at, in frames per second.
   *
   * @param speed the speed that the animation will play at, in frames per second
   */
  void setSpeed(int speed);

  /**
   * Gets the speed that the animation is currently playing at.
   *
   * @return the speed of the animation playback, in frames per second
   */
  int getSpeed();

  /**
   * Toggles whether or not the animation loops.
   */
  void toggleLoop();

  /**
   * Sets where player actions are handled.
   *
   * @param callback where player actions are handled
   */
  void setCallback(Consumer<PlayerAction> callback);
}
