package com.company.model.shape;

import java.awt.*;

/**
 * Represents a shape in the animator with a position, width, height, and color.
 */
public interface Shape {

  /**
   * Gets the position of this shape.
   *
   * @return the coordinate position of the shape as a {@link PosnCart}
   */
  Posn getPosition();

  /**
   * Gets the width of this shape.
   *
   * @return the width of the shape as an integer
   */
  double getWidth();

  /**
   * Gets the height of this shape.
   *
   * @return the height of the shape as an integer
   */
  double getHeight();

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

  /**
   * Gets the interpolated shape between this shape and the given shape, at the given progress.
   *
   * @param to       the shape to interpolate to
   * @param progress a number between 0 and 1 that represents the progress between this shape and
   *                 the to shape
   * @return the interpolated shape
   * @throws IllegalArgumentException if progress is not between 0 and 1, or the from and to shapes
   *                                  are of different types
   */
  Shape interpolate(Shape to, double progress);
}
