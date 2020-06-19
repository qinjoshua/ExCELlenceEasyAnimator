package com.company.controller.viewactions.playeractions;

import com.company.view.swing.player.PlayerView;

import java.util.function.Consumer;

/**
 * Represents a consumer that performs an action on a player view.
 */
public class PlayerActionConsumerImpl implements Consumer<PlayerAction> {
  final PlayerView view;

  /**
   * Constructor that sets the editor view to be acted on.
   *
   * @param view editor view to be acted on
   */
  public PlayerActionConsumerImpl(PlayerView view) {
    this.view = view;
  }

  @Override //TODO error handling
  public void accept(PlayerAction playerAction) {
    playerAction.actOn(view);
  }
}
