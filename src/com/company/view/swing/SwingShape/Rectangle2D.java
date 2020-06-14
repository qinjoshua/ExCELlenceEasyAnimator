package com.company.view.swing.SwingShape;

import java.awt.Color;

/**
 * Represents a 2D Rectangle using the Swing {@link java.awt.geom.Rectangle2D.Double} class.
 */
public class Rectangle2D implements SwingShape {
  /**
   * Creates a new rectangle with the given values in Cartesian coordinates.
   *
   * @param x      the x coordinate of the shape
   * @param y      the y coordinate of the shape
   * @param width  the width of the shape
   * @param height the height of the shape
   * @return a new rectangle with the given parameters
   */
  @Override
  public Shape createShape(double x, double y, double width, double height) {
    // subtract the y given by the height to account for the fact that it's given in Cartesian
    // coordinates
    return new java.awt.geom.Rectangle2D.Double(x, y, width, height);
  }
}
