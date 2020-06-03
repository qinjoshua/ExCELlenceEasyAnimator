package com.company.model.shape;

import java.awt.*;

/**
 * Represents a shape in the animator with a position, width, height, and color.
 */
public interface Shape {

  /**
   * Gets the position of this shape.
   *
   * @return the coordinate position of the shape as a Posn.
   */
  public Posn getPosition();

  /**
   * Gets the width of this shape.
   *
   * @return the width of the shape as an integer.
   */
  public int getWidth();

  /**
   * Gets the height of this shape.
   *
   * @return the height of the shape as an integer.
   */
  public int getHeight();

  /**
   * Gets the color of this shape.
   *
   * @return the color of this shape.
   */
  public Color getColor();

  /**
   * Gets the shape type for this shape.
   *
   * @return the ShapeType of this shape.
   */
  public ShapeType getShapeType();
}
