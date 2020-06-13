package com.company.view.swing;

import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.Posn;
import com.company.model.shape.Shape;
import com.company.model.shape.ShapeType;
import com.company.view.swing.SwingShape.Ellipse2D;
import com.company.view.swing.SwingShape.Rectangle2D;
import com.company.view.swing.SwingShape.SwingShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/**
 * Represents a class that extends JPanel to draw the shapes from a given animation model to the
 * screen. This class doesn't keep track of timing information: its only job is drawing to the
 * screen at a time t that it is given.
 */
public class AnimationPanel extends JPanel {
  // The animation model, used to get the shapes to draw.
  private final ReadOnlyAnimatorModel model;
  // The time t to render, in seconds.
  private double t;
  // A map from the shape types to the SwingShapes they represent
  private final Map<ShapeType, SwingShape> swingShapeMap;

  /**
   * Initializes the panel given a model. Sets the time t to 0 to it starts at the beginning.
   *
   * @param model the animator model to use
   * @throws IllegalArgumentException if the model is null
   */
  public AnimationPanel(ReadOnlyAnimatorModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Null model not allowed");
    }
    this.model = model;
    this.t = 0;
    this.swingShapeMap = new HashMap<>();
    this.swingShapeMap.put(ShapeType.Rectangle, new Rectangle2D());
    this.swingShapeMap.put(ShapeType.Ellipse, new Ellipse2D());
  }

  /**
   * Draws the given shape (the model's version of Shape, not Swing's) using the given Graphics2D
   * object.
   *
   * @param g     the graphics object to use to draw the shape
   * @param shape the shape as represented by the animation, including the color
   */
  private void drawModelShape(Graphics2D g, Shape shape) {
    Color oldColor = g.getColor();

    g.setColor(shape.getColor());
    g.fill(this.toSwingShape(shape));
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
   * Adds the given delta, in seconds, to the time that determines what part of the animation is
   * drawn next time paint() is called.
   *
   * @param delta the time delta to add in seconds
   * @throws IllegalArgumentException if the delta is negative
   */
  public void addTimeDelta(double delta) {
    if (delta < 0) {
      throw new IllegalArgumentException("Time delta cannot be negative");
    } else {
      this.t += delta;
    }
  }

  public void setTime(double t) {
    this.t = t;
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
    for (Shape shape : shapes.values()) {
      this.drawModelShape(g2, shape);
    }
    // restore old affine transform
    g2.setTransform(oldAT);
  }
}
