package com.company.controller.animatoractions;

import com.company.controller.viewactions.playeractions.PlayerActionConsumerImpl;
import com.company.model.AnimatorModel;
import com.company.view.swing.player.PlayerView;
import com.company.view.swing.player.PlayerViewImpl;

/**
 * Opens the preview for the animation.
 */
public class OpenPreview implements AnimatorAction {
  @Override
  public void actOn(AnimatorModel model) throws IllegalStateException {
    PlayerView view = new PlayerViewImpl(model, 20);
    view.setCallback(new PlayerActionConsumerImpl(view));
    view.renderVisual();
  }
}
