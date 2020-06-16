package com.company.view.swing;

import com.company.model.Frame;
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
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import javax.swing.JPanel;

/**
 * Represents a class that extends JPanel to draw the shapes from a given animation model to the
 * screen. This class doesn't keep track of timing information: its only job is drawing to the
 * screen at a time t that it is given.
 */
public class AnimationPanel extends JPanel {
  // The animation model, used to get the shapes to draw.
  private final ReadOnlyAnimatorModel model;
  // The time t to render, in seconds.
  private int t;
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
   * Increments the tick rendered to the screen by 1.
   */
  public void nextTick() {
    this.t += 1;
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
  }

  /**
   * Returns whether the current animation tick is past the last keyframe in the animation: i.e.,
   * the animation is over.
   *
   * @return whether the animation is on its last frame
   */
  public boolean onLastFrame() {
    for (SortedSet<Frame> frames : model.getKeyframes().values()) {
      for (Frame frame : frames) {
        if (frame.getTime() > t) {
          return false;
        }
      }
    }
    return true;
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
