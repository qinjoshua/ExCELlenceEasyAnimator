package com.company.model.shape;

import java.awt.*;

/**
 * Represents a shape in the animator with a position, width, height, and color.
 */
public interface Shape {

  /**
   * Gets the position of this shape.
   *
   * @return the coordinate position of the shape as a {@link Posn}
   */
  Posn getPosition();

  /**
   * Gets the width of this shape.
   *
   * @return the width of the shape as an integer
   */
  int getWidth();

  /**
   * Gets the height of this shape.
   *
   * @return the height of the shape as an integer
   */
  int getHeight();

  /**
   * Gets the color of this shape.
   *
   * @return the color of this shape
   */
  Color getColor();

  /**
   * Gets the shape type for this shape.
   *
   * @return the {@link ShapeType} of this shape
   */
  ShapeType getShapeType();
}
