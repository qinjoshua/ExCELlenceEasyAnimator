package com.company.controller.viewactions.editoractions;

import com.company.controller.viewactions.playeractions.PlayerActionConsumerImpl;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.swing.editor.EditorView;
import com.company.view.swing.player.PlayerView;
import com.company.view.swing.player.PlayerViewImpl;

/**
 * Action to open the preview window with given model to play.
 */
public class OpenPreview implements EditorAction {
  final ReadOnlyAnimatorModel model;

  /**
   * Initializes the preview opener with a read-only model to preview with.
   *
   * @param model the read only model whose preview is to be shown.
   */
  public OpenPreview(ReadOnlyAnimatorModel model) {
    this.model = model;
  }

  @Override
  public void actOn(EditorView editorView) throws IllegalStateException {
    PlayerView view = new PlayerViewImpl(model, 20);
    view.setCallback(new PlayerActionConsumerImpl(view));
    view.renderVisual();
  }
}
