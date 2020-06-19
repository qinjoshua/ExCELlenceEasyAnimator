package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.CreateKeyframe;
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
import javax.swing.KeyStroke;

/**
 * Provides a view for the main editor that allows animations to be made. This view provides an
 * interface that users can use to manipulate objects and create animations, but does not play
 * animations in their entirety.
 */
public class EditorViewImpl extends JFrame implements EditorView {

  private static final int TIMELINE_HEIGHT = 200;
  private final Consumer<AnimatorAction> modelCallback;
  private final CanvasPanel canvas;
  private final ToolbarPanel toolbar;
  private final TimelinesPanel timelines;
  private final ReadOnlyAnimatorModel model;
  // Panels
  private BannerPanel banner;
  private Consumer<EditorAction> callback;
  private final PropertyPanel properties;
  private Point mouseClickedPoint;
  private int tick;

  /**
   * Creates a new implementation of editor view with the given model and model callback.
   *
   * @param model         the read-only model for the editor view to retrieve information from
   * @param modelCallback a callback for the editorView to request changes to the model
   */
  public EditorViewImpl(
          ReadOnlyAnimatorModel model, Consumer<AnimatorAction> modelCallback) {
    super("Excellence Editor");

    this.model = model;
    this.modelCallback = modelCallback;

    this.canvas = new CanvasPanel(model, this.modelCallback);

    this.properties = new PropertyPanel();

    this.timelines = new TimelinesPanel(model, modelCallback);

    this.getContentPane().add(properties, BorderLayout.EAST);
    this.getContentPane().add(timelines, BorderLayout.SOUTH);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    canvas.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    this.getContentPane().add(canvas, BorderLayout.CENTER);
    //this.getContentPane().add(properties, BorderLayout.EAST);

    this.toolbar = new ToolbarPanel(this.model);
    this.getContentPane().add(toolbar, BorderLayout.WEST);

    this.callback = null;
    KeyComponent keyComponent = new KeyComponent();

    this.setTick(1);
    this.setPreferredSize(new Dimension(1200, 800));

    this.updateBanner(null);
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
    if (this.getHighlightedShape() != null) {
      properties.addProperties(this.getHighlightedShapeName(), tick, modelCallback, model);
    }
    this.timelines.update(this.tick);
    this.setPreferredSize(this.getSize());
    this.repaint();
    this.pack();
  }

  /**
   * Updates the properties pane to include all the details of the shape that is to be highlighted.
   *
   * @param toBeHighlighted shape that is to be highlighted
   */
  private void updateProperties(String toBeHighlighted) {
    this.properties.addProperties(toBeHighlighted, this.tick, modelCallback, model);
    this.refreshView();
  }

  private void updateBanner(String toBeHighlighted) {
    if (banner != null) {
      this.getContentPane().remove(banner);
    }
    banner = new BannerPanel(toBeHighlighted, this.tick, model, modelCallback);
    banner.setViewCallback(callback);
    this.getContentPane().add(banner, BorderLayout.NORTH);
  }

  @Override
  public void highlightShape(String toBeHighlighted) {
    this.canvas.highlightShape(toBeHighlighted);

    if (toBeHighlighted == null) {
      this.properties.hideProperties();
      this.timelines.deHighlightPanel();
    } else {
      this.modelCallback.accept(new CreateKeyframe(toBeHighlighted, tick));
      this.updateProperties(toBeHighlighted);
      this.timelines.highlightPanel(toBeHighlighted);
    }
    this.updateBanner(toBeHighlighted);
    this.refreshView();
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
    this.properties.setViewCallback(callback);
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
