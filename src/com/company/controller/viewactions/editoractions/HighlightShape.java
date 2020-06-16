package com.company.controller.viewactions.editoractions;

import com.company.view.swing.editor.EditorView;

import java.awt.Shape;

/**
 * Action that tells an editor view to highlight the given shape.
 */
public class HighlightShape implements EditorAction {
  private Shape toBeHighlighted;

  public HighlightShape(Shape toBeHighlighted) {
    this.toBeHighlighted = toBeHighlighted;
  }

  @Override
  public void actOn(EditorView view) {

  }
}
