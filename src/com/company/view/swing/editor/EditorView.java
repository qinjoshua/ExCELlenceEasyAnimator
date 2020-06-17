package com.company.view.swing.editor;

import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.view.VisualView;

import java.awt.Shape;
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
  void highlightShape(Shape toBeHighlighted);

  /**
   * Sets where editor actions are handled.
   *
   * @param callback where editor actions are handled
   */
  void setCallback(Consumer<EditorAction> callback);

  /**
   * Updates the bounding box to where it's supposed to be.
   */
  void updateBoundingBox();

  /**
   * Returns the currently highlighted shape on the canvas of this editor.
   *
   * @return the currently highlighted shape
   */
  Shape getHighlightedShape();
}
