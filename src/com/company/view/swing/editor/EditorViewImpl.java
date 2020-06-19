package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.ShapeType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

/**
 * Provides a view for the main editor that allows animations to be made. This view provides an
 * interface that users can use to manipulate objects and create animations, but does not play
 * animations in their entirety.
 */
public class EditorViewImpl extends JFrame implements EditorView {

  private final Consumer<AnimatorAction> modelCallback;
  // Panels
  private final CanvasPanel canvas;
  private final ToolbarPanel toolbar;
  private final TimelinesPanel timelines;
  private final KeyComponent keyComponent;
  private final ReadOnlyAnimatorModel model;
  private Consumer<EditorAction> callback;
  private PropertyPanel properties;
  private Point mouseClickedPoint;
  private int tick;
  private static final int TIMELINE_HEIGHT = 200;

  public EditorViewImpl(
      ReadOnlyAnimatorModel model, Consumer<AnimatorAction> modelCallback) {
    super("Excellence Editor");

    this.model = model;
    this.modelCallback = modelCallback;

    this.canvas = new CanvasPanel(model, this.modelCallback);

    this.properties = null;

    this.timelines = new TimelinesPanel(model, modelCallback);

    this.getContentPane().add(timelines, BorderLayout.NORTH);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    canvas.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    this.getContentPane().add(canvas, BorderLayout.CENTER);
    //this.getContentPane().add(properties, BorderLayout.EAST);

    this.toolbar = new ToolbarPanel(this.modelCallback, this.model);
    this.getContentPane().add(toolbar, BorderLayout.WEST);

    this.callback = null;
    this.keyComponent = new KeyComponent();

    this.setTick(1);
    this.setPreferredSize(new Dimension(1200, 800));
    this.pack();
  }

  @Override
  public void renderVisual() {
    this.setVisible(true);
    this.refreshView();
  }

  @Override
  public void refreshView() {
    this.canvas.updateShapes();
    this.updateBoundingBox();
    this.repaint();
  }

  private void updateProperties(String toBeHighlighted) {
    if (properties != null) {
      this.getContentPane().remove(properties);
    }
    this.properties = new PropertyPanel(toBeHighlighted, this.tick, modelCallback, model,
        callback);
    properties.setPreferredSize(new Dimension(350, model.getCanvasHeight()));
    this.getContentPane().add(properties, BorderLayout.EAST);
    this.pack();
  }

  @Override
  public void highlightShape(String toBeHighlighted) {
    this.canvas.highlightShape(toBeHighlighted);

    if (toBeHighlighted == null) {
      if (properties != null) {
        this.getContentPane().remove(this.properties);
        this.pack();
      }
    } else {
      this.updateProperties(toBeHighlighted);
    }
  }


  @Override
  public void setTick(int tick) {
    this.tick = tick;
    this.canvas.setTick(tick);
    this.timelines.setTick(tick);
    if (this.getHighlightedShapeName() != null) {
      this.updateProperties(this.getHighlightedShapeName());
    }
  }

  @Override
  public void setCallback(Consumer<EditorAction> callback) {
    this.callback = callback;
    this.canvas.setCallback(callback);
    this.toolbar.setCallback(callback);
    this.timelines.setViewCallback(callback);
  }

  @Override
  public void updateBoundingBox() {
    this.canvas.updateBoundingBox();
  }

  @Override
  public java.awt.Shape getHighlightedShape() {
    return this.canvas.getHighlightedShape();
  }

  /**
   * Gets the name of the currently highlighted shape.
   *
   * @return the name of the currently highlighted shape.
   */
  private String getHighlightedShapeName() {
    return this.canvas.getHighlightedShapeName();
  }

  @Override
  public void createShape(ShapeType type, String name) {
    this.canvas.createShape(type, name);
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
