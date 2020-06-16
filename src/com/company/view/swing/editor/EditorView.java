package com.company.view.swing.editor;

import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.playeractions.PlayerAction;
import com.company.view.VisualView;

import java.util.function.Consumer;

/**
 * Represents all the functionality that a controller can perform on an editor view.
 */
public interface EditorView extends VisualView {

  /**
   * Refreshes the view to reflect any changes in the model.
   */
  void refreshView();

  /**
   * Shows that a given shape is selected, showing the bounding box around the shape.
   */
  void highlightShape();

  /**
   * Sets where editor actions are handled.
   *
   * @param callback where editor actions are handled
   */
  void setCallback(Consumer<EditorAction> callback);
}
