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
import java.awt.geom.AffineTransform;
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
    this.t = 0;
    this.swingShapeMap = new LinkedHashMap<>();
    this.swingShapeMap.put(ShapeType.Rectangle, new Rectangle2D());
    this.swingShapeMap.put(ShapeType.Ellipse, new Ellipse2D());
  }

  /**
   * Draws the given shape (the model's version of Shape, not Swing's) using the given Graphics2D
   * object. Updates the shapes map to include the shape being drawn.
   *
   * @param g     the graphics object to use to draw the shape
   * @param shape the shape as represented by the animation, including the color
   */
  private void drawModelShape(Graphics2D g, Shape shape, String shapeName) {
    Color oldColor = g.getColor();

    g.setColor(shape.getColor());
    java.awt.Shape newShape = this.toSwingShape(shape);

    //this.shapes.put(shapeName, new ColoredShape(shape.getColor(), newShape));
    g.fill(newShape);
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
  private java.awt.Shape toSwingShape(Shape modelShape) {
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
    AffineTransform oldAT = g2.getTransform();
    g2.translate(-model.getCanvasX(), -model.getCanvasY());
    Map<String, Shape> shapes = model.shapesAt(t);

    this.shapes = new LinkedHashMap<>();
    for (Map.Entry<String, Shape> shape : shapes.entrySet()) {
      this.drawModelShape(g2, shape.getValue(), shape.getKey());
    }

    // restore old affine transform
    g2.setTransform(oldAT);
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
