package com.company.view.swing;

import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.Posn;
import com.company.model.shape.Shape;
import com.company.model.shape.ShapeType;
import com.company.view.swing.swingshape.Ellipse2D;
import com.company.view.swing.swingshape.Rectangle2D;
import com.company.view.swing.swingshape.SwingShape;

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
  protected Map<String, ColoredShape> shapes;

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
      this.shapes.put(shape.getKey(), new ColoredShape(shape.getValue().getColor(), newShape));
    }
  }

  private void drawModelShape(Graphics2D g) {
    Color oldColor = g.getColor();

    for (ColoredShape coloredShape : shapes.values()) {
      g.setColor(coloredShape.color);
      g.fill(coloredShape.shape);
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

    // Every time repaint is called, this code will update the properties of shapes to match how
    // they appear in the model
    Map<String, Shape> shapes = model.shapesAt(t);
    for (Map.Entry<String, Shape> shape : shapes.entrySet()) {
      this.shapes.get(shape.getKey()).color = shape.getValue().getColor();
      this.shapes.get(shape.getKey()).shape = this.toSwingShape(shape.getValue());
    }

    this.drawModelShape((Graphics2D) g);
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
  protected void updateShapes() {
    Map<String, com.company.model.shape.Shape> modelShapes = model.shapesAt(this.t);

    for (Map.Entry<String, com.company.model.shape.Shape> modelShape : modelShapes.entrySet()) {
      if (!this.shapes.containsKey(modelShape.getKey())) {
        java.awt.Shape newShape = this.toSwingShape(modelShape.getValue());
        this.shapes.put(modelShape.getKey(),
            new ColoredShape(modelShape.getValue().getColor(), newShape));
      }
    }

    for (Map.Entry<String, ColoredShape> shape : shapes.entrySet()) {
      if (!modelShapes.containsKey(shape.getKey())) {
        this.shapes.remove(shape.getKey());
      }
    }
  }

  /**
   * Struct-like class that stores a color associated with a shape, since Java's awt shapes don't
   * have a color.
   */
  protected static class ColoredShape {
    public Color color;
    public java.awt.Shape shape;

    public ColoredShape(Color color, java.awt.Shape shape) {
      this.color = color;
      this.shape = shape;
    }
  }
}
