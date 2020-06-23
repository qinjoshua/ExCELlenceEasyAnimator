package com.company.view.swing;

import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.Posn;
import com.company.model.shape.Shape;
import com.company.model.shape.ShapeType;
import com.company.view.swing.editor.Ellipse2D;
import com.company.view.swing.editor.Rectangle2D;
import com.company.view.swing.editor.SwingShape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;

/**
 * Represents a panel that displays shapes from an animator model.
 */
public abstract class AShapesPanel extends JPanel {
  // The animation model, used to get the shapes to draw.
  protected final ReadOnlyAnimatorModel model;
  // A map from the shape types to the SwingShapes they represent
  protected final Map<ShapeType, SwingShape> swingShapeMap;
  // The tick t to render.
  protected int t;
  // Map that maps a shape name to a colored shape, which contains a reference to the shape that
  // is drawn on screen
  protected Map<String, DecoratedShape> shapes;

  /**
   * Initializes the panel given a model. Sets the time t to 0 to it starts at the beginning.
   *
   * @param model the animator model to use
   * @throws IllegalArgumentException if the model is null
   */
  public AShapesPanel(ReadOnlyAnimatorModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Null model not allowed");
    }
    this.model = model;

    this.t = 1;
    this.swingShapeMap = new LinkedHashMap<>();
    this.swingShapeMap.put(
        ShapeType.Rectangle, new Rectangle2D(model.getCanvasX(), model.getCanvasY()));
    this.swingShapeMap.put(
        ShapeType.Ellipse, new Ellipse2D(model.getCanvasX(), model.getCanvasY()));

    // Initializes the shapes
    Map<String, Shape> modelShapes = model.shapesAt(t);

    this.shapes = new LinkedHashMap<>();
    for (Map.Entry<String, Shape> shape : modelShapes.entrySet()) {
      java.awt.Shape newShape = this.toSwingShape(shape.getValue());
      this.shapes.put(shape.getKey(), new DecoratedShape(shape.getValue().getColor(), newShape,
              shape.getValue().getShapeAngle()));
    }
  }

  private void drawModelShape(Graphics2D g) {
    Color oldColor = g.getColor();

    for (DecoratedShape decoratedShape : shapes.values()) {
      g.setColor(decoratedShape.color);
      g.rotate(-decoratedShape.angle, decoratedShape.shape.getBounds2D().getCenterX(),
              decoratedShape.shape.getBounds2D().getCenterY());

      g.fill(decoratedShape.shape);

      g.rotate(decoratedShape.angle, decoratedShape.shape.getBounds2D().getCenterX(),
              decoratedShape.shape.getBounds2D().getCenterY());
    }

    // restore previous fill color
    g.setColor(oldColor);
  }

  /**
   * Converts the model's representation of a shape to the Swing version. Color information is
   * necessarily lost, because Swing shapes don't handle color internally.
   *
   * @param modelShape the shape object to convert
   * @throws IllegalArgumentException if the shape type cannot be recognized
   */
  protected final java.awt.Shape toSwingShape(Shape modelShape) {
    Posn posn = modelShape.getPosition();
    double width = modelShape.getWidth();
    double height = modelShape.getHeight();
    ShapeType type = modelShape.getShapeType();

    if (swingShapeMap.containsKey(type)) {
      return swingShapeMap.get(type).createShape(posn.getX(), posn.getY(), width, height);
    } else {
      throw new IllegalArgumentException("Invalid shape type");
    }
  }

  /**
   * Paints this object's animation model at this object's time in seconds to the given graphics
   * object.
   *
   * @param g the graphics object to draw to
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    this.drawCanvas(g);
    this.drawModelShape((Graphics2D) g);
  }

  /**
   * Draws a canvas between a panel and the shapes.
   *
   * @param g the graphics object to draw to
   */
  protected void drawCanvas(Graphics g) {
    // Default behavior does nothing; can be overwritten to draw a canvas onto the screen.
    return;
  }

  /**
   * Gets the current tick.
   *
   * @return the current tick
   */
  public int getTick() {
    return t;
  }

  /**
   * Sets the tick for the animation to start playing from to the given tick.
   *
   * @param t the tick that the animation should start playing from
   * @throws IllegalArgumentException if the given tick is less than zero
   */
  public void setTick(int t) {
    if (t < 0) {
      throw new IllegalArgumentException("Tick was set to less than zero");
    }
    this.t = t;
    this.updateShapes();
  }

  /**
   * Checks to see if the model has changed, and updates the shapes map to reflect all the shapes
   * that are drawn on the screen.
   */
  public void updateShapes() {
    // Every time repaint is called, this code will update the properties of shapes to match how
    // they appear in the model
    Map<String, Shape> modelShapes = model.shapesAt(t);
    for (Map.Entry<String, Shape> shape : modelShapes.entrySet()) {
      if (this.shapes.containsKey(shape.getKey())) {
        this.shapes.get(shape.getKey()).color = shape.getValue().getColor();
        this.shapes.get(shape.getKey()).angle = shape.getValue().getShapeAngle();
        this.shapes.get(shape.getKey()).shape = this.toSwingShape(shape.getValue());
      } else {
        java.awt.Shape newShape = this.toSwingShape(shape.getValue());
        this.shapes.put(shape.getKey(),
                new DecoratedShape(
                        shape.getValue().getColor(), newShape, shape.getValue().getShapeAngle()));
      }
    }

    Map<String, DecoratedShape> toRemove = new LinkedHashMap<>();
    for (Map.Entry<String, DecoratedShape> shape : shapes.entrySet()) {
      if (!modelShapes.containsKey(shape.getKey())) {
        toRemove.put(shape.getKey(), shape.getValue());
      }
    }

    for (String shapeName : toRemove.keySet()) {
      shapes.remove(shapeName);
    }
  }
}
