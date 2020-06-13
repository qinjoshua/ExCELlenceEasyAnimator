package com.company.model.shape.shapes;

import com.company.model.shape.AShape;
import com.company.model.shape.Posn;
import com.company.model.shape.Shape;
import com.company.model.shape.ShapeType;

import java.awt.*;

/**
 * Represents an ellipse shape with a given center coordinate, major axis length, minor axis length,
 * and color.
 */
public class Ellipse extends AShape implements Shape {
  /**
   * Constructor that initializes all the fields for this shape.
   *
   * @param posn   the center of this ellipse
   * @param width  this shape's width
   * @param height this shape's height
   * @param color  this shape's color
   */
  public Ellipse(Posn posn, double width, double height, Color color) {
    super(posn, width, height, color, ShapeType.Ellipse);
  }

  @Override
  protected AShape newShape(Posn posn, double width, double height, Color color) {
    return new Ellipse(posn, width, height, color);
  }
}
