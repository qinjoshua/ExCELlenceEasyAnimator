package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.model.ReadOnlyAnimatorModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Provides a view for the main editor that allows animations to be made. This view provides an
 * interface that users can use to manipulate objects and create animations, but does not play
 * animations in their entirety.
 */
public class EditorViewImpl extends JFrame implements EditorView {

  // TODO: Seems to have a lot of commonalities with player view implementation. Abstract class?

  private Consumer<EditorAction> callback;
  private final Consumer<AnimatorAction> modelCallback;
  private final CanvasPanel canvas;
  private final KeyComponent keyComponent;

  private Point mouseClickedPoint;

  public EditorViewImpl(
          ReadOnlyAnimatorModel model, Consumer<AnimatorAction> modelCallback) {
    super("Excellence Editor");

    this.modelCallback = modelCallback;

    this.canvas = new CanvasPanel(model, this.modelCallback);
    this.canvas.setTick(25);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    canvas.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    this.getContentPane().add(canvas, BorderLayout.CENTER);

    this.callback = null;

    this.keyComponent = new KeyComponent();

    this.pack();
  }

  @Override
  public void renderVisual() {
    this.setVisible(true);
    this.refreshView();
  }

  @Override
  public void refreshView() {
    this.canvas.repaint();
  }

  @Override
  public void highlightShape(java.awt.Shape toBeHighlighted) {
    this.canvas.highlightShape(toBeHighlighted);
  }

  @Override
  public void setCallback(Consumer<EditorAction> callback) {
    this.callback = callback;
    this.canvas.setCallback(this.callback);
  }

  @Override
  public void updateBoundingBox() {
    this.canvas.updateBoundingBox();
  }

  @Override
  public java.awt.Shape getHighlightedShape() {
    return this.canvas.getHighlightedShape();
  }

  private static class KeyComponent extends JPanel {
    /**
     * Sets the given keystroke to the given action with the given name.
     *
     * @param key    the key used to start the action
     * @param name   the name used for the action
     * @param action the action itself
     */
    public void setCommand(KeyStroke key, String name, Action action) {
      this.getInputMap().put(key, name);
      this.getActionMap().put(name, action);
    }

    /**
     * Sets the given action with the given name.
     *
     * @param name   the name used for the action
     * @param action the action itself
     */
    public void setCommand(String name, Action action) {
      this.getActionMap().put(name, action);
    }
  }
}
