package com.company.model.shape;

import java.awt.Color;

/**
 * Represents a shape in the animator with a position, width, height, and color.
 */
public interface Shape {

  /**
   * Gets the position of this shape.
   *
   * @return the coordinate position of the shape as a {@link PosnImpl}
   */
  Posn getPosition();

  /**
   * Sets the position for this shape.
   *
   * @param posn the position of this shape
   */
  void setPosn(Posn posn);

  /**
   * Gets the width of this shape.
   *
   * @return the width of the shape as an integer
   */
  double getWidth();

  /**
   * Sets the width of this shape.
   *
   * @param width the width of this shapes
   */
  void setWidth(double width);

  /**
   * Gets the height of this shape.
   *
   * @return the height of the shape as an integer
   */
  double getHeight();

  /**
   * Sets the height of this shape.
   *
   * @param height the height of the shape as a double
   */
  void setHeight(double height);

  /**
   * Gets the color of this shape.
   *
   * @return the color of this shape
   */
  Color getColor();

  /**
   * Sets the color for this shape.
   *
   * @param color the color of this shape
   */
  void setColor(Color color);

  /**
   * Gets the shape type for this shape.
   *
   * @return the {@link ShapeType} of this shape
   */
  ShapeType getShapeType();

  /**
   * Sets the type of this shape.
   *
   * @param shapeType the type of this shape
   */
  void setShapeType(ShapeType shapeType);

  /**
   * Gets the angle of the shape.
   *
   * @return the angle of the shape as a double
   */
  double getShapeAngle();

  /**
   * Sets the angle of the shape.
   *
   * @param angle the angle of the shape as a double
   */
  void setShapeAngle(double angle);

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

  /**
   * Makes a copy of itself.
   *
   * @return a shape that is a copy of itself
   */
  Shape copy();
}
