package com.company.model.shape.shapes;

import com.company.model.shape.AShape;
import com.company.model.shape.Posn;
import com.company.model.shape.Shape;
import com.company.model.shape.ShapeType;

import java.awt.*;

public class Ellipse extends AShape implements Shape {
  /**
   * Constructor that initializes all the fields for this shape.
   *
   * @param posn   this shape's position
   * @param width  this shape's width
   * @param height this shape's height
   * @param color  this shape's color
   */
  public Ellipse(Posn posn, int width, int height, Color color) {
    super(posn, width, height, color, ShapeType.Ellipse);
  }
}
