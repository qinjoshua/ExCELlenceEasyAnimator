package com.company.view.swing.editor;

import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.swing.AShapesPanel;
import com.company.view.swing.player.PlayerViewImpl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the canvas portion of the editor, the main area where the animation can be modified
 * and worked on. This canvas panel allows shapes to be dragged on top of it, repositioned, and
 * resized, and displays what any given keyframe looks like.
 */
public class CanvasPanel extends AShapesPanel {
  Shape highlightedShape;
  EditorView view;

  /**
   * Initializes the panel given a model and editor view. Sets the time t to 0 to it starts at the
   * beginning.
   *
   * @param model the animator model to use
   * @param view the view to use
   * @throws IllegalArgumentException if the model is null
   */
  public CanvasPanel(ReadOnlyAnimatorModel model, EditorView view) {
    super(model);
    this.view = view;

    this.highlightedShape = null;

    this.addMouseListener(new CanvasMouseAdapter());
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  private class CanvasMouseAdapter extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      Shape toHighlight = null;

      // highlightedShape will end up on the value of the last shape in the list (the topmost
      // shape) that contains the mouse cursor, when it was clicked
      for (AShapesPanel.ColoredShape coloredShape : shapes.values()) {
        if (coloredShape.shape.contains(e.getPoint())) {
          toHighlight = highlightedShape;
        }
      }


    }
  }
}
