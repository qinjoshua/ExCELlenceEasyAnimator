package com.company.model.shape.shapes;

import com.company.model.shape.AShape;
import com.company.model.shape.Posn;
import com.company.model.shape.ShapeType;

import java.awt.Color;

/**
 * A rectangle with a width, height, color, and position, describing the location of the rectangle's
 * upper-left corner. Uses the {@link ShapeType} {@code Rectangle}.
 */
public class Rectangle extends AShape {
  /**
   * Creates a rectangle given the location of its upper-left corner, its width and height, and its
   * color.
   *
   * @param posn   the upper-left corner
   * @param width  the width of the rectangle
   * @param height the height of the rectangle
   * @param color  the color of the rectangle
   */
  public Rectangle(Posn posn, double width, double height, Color color) {
    super(posn, width, height, color, ShapeType.Rectangle);
  }

  @Override
  protected AShape newShape(Posn posn, double width, double height, Color color) {
    return new Rectangle(posn, width, height, color);
  }
}
